package legend.game.unpacker;

import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import legend.core.IoHelper;
import legend.core.MathHelper;
import legend.core.Tuple;
import legend.game.Scus94491BpeSegment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static legend.game.Scus94491BpeSegment.getCharacterName;

public final class Unpacker {
  private Unpacker() { }

  private static final String[] DISK_IDS = {"SCUS94491", "SCUS94584", "SCUS94585", "SCUS94586"};
  private static final List<String> OTHER_REGION_IDS = List.of("SCES03043", "SCES13043", "SCES23043", "SCES33043", "SCES03044", "SCES13044", "SCES23044", "SCES33044", "SCES03045", "SCES13045", "SCES23045", "SCES33045", "SCES03046", "SCES13046", "SCES23046", "SCES33046", "SCES03047", "SCES13047", "SCES23047", "SCES33047", "SCPS10119", "SCPS10120", "SCPS10121", "SCPS10122", "SCPS45461", "SCPS45462", "SCPS45463", "SCPS45464");
  private static final int PVD_SECTOR = 16;

  private static final Pattern ROOT_MRG = Pattern.compile("^SECT/DRGN0\\.BIN/\\d{4}/\\d+$");

  /** Update this any time we make a breaking change */
  private static final int VERSION = 1;

  static {
    System.setProperty("log4j.skipJansi", "false");
    System.setProperty("log4j2.configurationFile", "log4j2.xml");
  }

  public static final Logger LOGGER = LogManager.getFormatterLogger(Unpacker.class);

  public static Path ROOT = Path.of(".", "files");

  private static final FileData EMPTY_DIRECTORY_SENTINEL = new FileData(new byte[0]);

  private static final Object IO_LOCK = new Object();

  /**
   * Note: the transformation pipeline is recursive and after a transformation, the file will be placed back into the transformation queue
   * until no more transformers apply to it. Discriminators must be able to recognize their own changes and return false, or the file will
   * be transformed infinitely.
   */
  private static final Map<Discriminator, Transformer> transformers = new LinkedHashMap<>();
  static {
    transformers.put(Unpacker::decompressDiscriminator, Unpacker::decompress);
    transformers.put(Unpacker::mrgDiscriminator, Unpacker::unmrg);
    transformers.put(Unpacker::deffDiscriminator, Unpacker::undeff);
    transformers.put(Unpacker::drgn21_402_3_patcherDiscriminator, Unpacker::drgn21_402_3_patcher);
    transformers.put(Unpacker::drgn21_693_0_patcherDiscriminator, Unpacker::drgn21_693_0_patcher);
    transformers.put(Unpacker::drgn0_142_animPatcherDiscriminator, Unpacker::drgn0_142_animPatcher);

    // Give Dart his hand back during oof
    transformers.put(Unpacker::drgn0_5546_1_patcherDiscriminator, Unpacker::drgn0_5546_1_patcher);

    // Yes there are 3 different magma fish files to patch
    transformers.put(Unpacker::drgn0_3667_16_animPatcherDiscriminator, Unpacker::drgn0_3667_16_animPatcher);
    transformers.put(Unpacker::drgn0_3667_17_animPatcherDiscriminator, Unpacker::drgn0_3667_17_animPatcher);
    transformers.put(Unpacker::drgn0_3750_16_animPatcherDiscriminator, Unpacker::drgn0_3750_16_animPatcher);

    transformers.put(Unpacker::drgn1_343_patcherDiscriminator, Unpacker::drgn1_343_patcher);
    transformers.put(Unpacker::playerCombatSoundEffectsDiscriminator, Unpacker::playerCombatSoundEffectsTransformer);
    transformers.put(Unpacker::playerCombatModelsAndTexturesDiscriminator, Unpacker::playerCombatModelsAndTexturesTransformer);
    transformers.put(Unpacker::dragoonCombatModelsAndTexturesDiscriminator, Unpacker::dragoonCombatModelsAndTexturesTransformer);
    transformers.put(Unpacker::skipPartyPermutationsDiscriminator, Unpacker::skipPartyPermutationsTransformer);
    transformers.put(Unpacker::extractBtldDataDiscriminator, Unpacker::extractBtldDataTransformer);
    transformers.put(Unpacker::extractItemDataDiscriminator, Unpacker::extractItemDataTransformer);
    transformers.put(Unpacker::uiPatcherDiscriminator, Unpacker::uiPatcherTransformer);
    transformers.put(CtmdTransformer::ctmdDiscriminator, CtmdTransformer::ctmdTransformer);

    // Remove damage caps from scripts
    transformers.put(Unpacker::playerScriptDamageCapsDiscriminator, Unpacker::playerScriptDamageCapsTransformer);
    transformers.put(Unpacker::enemyScriptDamageCapDiscriminator, Unpacker::enemyAndItemScriptDamageCapPatcher);
    transformers.put(Unpacker::itemScriptDamageCapDiscriminator, Unpacker::enemyAndItemScriptDamageCapPatcher);
  }

  private static Consumer<String> statusListener = status -> { };
  private static boolean shouldStop;

  public static void main(final String[] args) throws UnpackerException {
    unpack();
  }

  public static void stop() {
    shouldStop = true;
  }

  public static void setStatusListener(final Consumer<String> listener) {
    statusListener = listener;
  }

  public static FileData loadFile(final String name) {
    LOGGER.info("Loading file %s", name);

    synchronized(IO_LOCK) {
      try {
        return new FileData(Files.readAllBytes(ROOT.resolve(fixPath(name))));
      } catch(final IOException e) {
        throw new RuntimeException("Failed to load file " + name, e);
      }
    }
  }

  public static List<FileData> loadDirectory(final String name) {
    LOGGER.info("Loading directory %s", name);

    synchronized(IO_LOCK) {
      final Path dir = ROOT.resolve(fixPath(name));
      final Path mrg = dir.resolve("mrg");

      if(Files.exists(mrg)) {
        try(final BufferedReader reader = Files.newBufferedReader(mrg)) {
          final Int2IntMap fileMap = new Int2IntArrayMap();

          reader.lines().forEach(line -> {
            final String[] parts = line.split("=");

            if(parts.length != 2) {
              throw new RuntimeException("Invalid MRG entry! " + line);
            }

            final int virtual = Integer.parseInt(parts[0]);
            final int real = Integer.parseInt(parts[1]);
            fileMap.put(virtual, real);
          });

          final List<FileData> files = new ArrayList<>();

          // Add real files
          for(final var entry : fileMap.int2IntEntrySet()) {
            final int virtual = entry.getIntKey();
            final int real = entry.getIntValue();

            try {
              final Path file = dir.resolve(String.valueOf(real));
              if(Files.isRegularFile(file)) {
                if(virtual == real) {
                  files.add(new FileData(Files.readAllBytes(file)));
                } else {
                  files.add(null);
                }
              }
            } catch(final IOException e) {
              throw new RuntimeException("Failed to load directory " + name, e);
            }
          }

          // Add virtual files
          for(final var entry : fileMap.int2IntEntrySet()) {
            final int virtual = entry.getIntKey();
            int real = entry.getIntValue();

            if(virtual == real) {
              continue;
            }

            // Resolve to the realest file
            while(fileMap.get(real) != real) {
              real = fileMap.get(real);
            }

            final Path file = dir.resolve(String.valueOf(real));
            if(Files.isRegularFile(file)) {
              files.set(virtual, FileData.virtual(files.get(real), real));
            }
          }

          return files;
        } catch(final IOException e) {
          throw new RuntimeException("Failed to load directory " + name, e);
        }
      } else {
        try(final DirectoryStream<Path> ds = Files.newDirectoryStream(ROOT.resolve(fixPath(name)))) {
          final List<FileData> files = new ArrayList<>();

          StreamSupport.stream(ds.spliterator(), false)
            .filter(Files::isRegularFile)
            .sorted((path1, path2) -> {
              final String filename1 = path1.getFileName().toString();
              final String filename2 = path2.getFileName().toString();

              try {
                return Integer.compare(Integer.parseInt(filename1), Integer.parseInt(filename2));
              } catch(final NumberFormatException ignored) { }

              return String.CASE_INSENSITIVE_ORDER.compare(filename1, filename2);
            })
            .forEach(child -> {
              try {
                files.add(new FileData(Files.readAllBytes(child)));
              } catch(final IOException e) {
                throw new RuntimeException("Failed to load directory " + name, e);
              }
            });

          return files;
        } catch(final IOException e) {
          throw new RuntimeException("Failed to load directory " + name, e);
        }
      }
    }
  }

  public static boolean exists(final String name) {
    synchronized(IO_LOCK) {
      return Files.exists(ROOT.resolve(fixPath(name)));
    }
  }

  public static boolean isDirectory(final String name) {
    synchronized(IO_LOCK) {
      return Files.isDirectory(ROOT.resolve(fixPath(name)));
    }
  }

  private static String fixPath(String name) {
    if(name.contains(";")) {
      name = name.substring(0, name.lastIndexOf(';'));
    }

    if(name.startsWith("\\")) {
      name = name.substring(1);
    }

    return name.replace('\\', '/');
  }

  public static void unpack() throws UnpackerException {
    synchronized(IO_LOCK) {
      try {
        Files.createDirectories(ROOT);

        if(getUnpackVersion() != VERSION) {
          final long start = System.nanoTime();

          statusListener.accept("Deleting old unpacked files...");
          final Path gitIgnore = ROOT.resolve(".gitignore");

          try(final Stream<Path> files = Files.walk(ROOT)) {
            files
              .sorted(Comparator.reverseOrder())
              .filter(path -> {
                try {
                  return !Files.isSameFile(path, gitIgnore);
                } catch(final IOException ignored) {
                  return true;
                }
              })
              .forEach(path -> {
                try {
                  if(!Files.isSameFile(path, ROOT)) {
                    Files.delete(path);
                  }
                } catch(final IOException e) {
                  throw new UnpackerException("Failed to delete old unpacked files", e);
                }
              });
          }

          LOGGER.info("Files deleted in %d seconds", (System.nanoTime() - start) / 1_000_000_000L);
        }

        final long start = System.nanoTime();

        final IsoReader[] readers = new IsoReader[4];
        int diskCount = 0;

        try(final DirectoryStream<Path> children = Files.newDirectoryStream(Path.of("isos"))) {
          for(final Path child : children) {
            final Tuple<IsoReader, Integer> tuple = getIsoReader(child);

            if(tuple != null) {
              final int diskNum = tuple.b();

              if(readers[diskNum] != null) {
                LOGGER.warn("Found duplicate disk %d: %s", diskNum + 1, child);
                continue;
              }

              LOGGER.info("Found disk %d: %s", diskNum + 1, child);
              readers[diskNum] = tuple.a();
              diskCount++;
            }
          }
        }

        if(diskCount < 4) {
          for(int i = 0; i < readers.length; i++) {
            if(readers[i] == null) {
              LOGGER.error("Failed to find disk %d!", i + 1);
            }
          }

          throw new UnpackerException("Failed to locate disk images");
        }

        final DirectoryEntry[] roots = new DirectoryEntry[4];
        final DirectoryEntry root = loadRoot(readers[3], null);

        for(int i = 0; i < roots.length - 1; i++) {
          loadRoot(readers[i], root);
        }

        final Map<String, DirectoryEntry> files = new HashMap<>();
        getFiles(root, "", files);

        files.entrySet()
          .stream()
          .filter(entry -> !Files.exists(ROOT.resolve(entry.getKey())))
          .map(Unpacker::readFile)
          .map(e -> transform(e.a(), e.b(), new HashSet<>()))
          .forEach(Unpacker::writeFiles);

        Files.writeString(ROOT.resolve("version"), Integer.toString(VERSION));

        LOGGER.info("Files unpacked in %d seconds", (System.nanoTime() - start) / 1_000_000_000L);
      } catch(final Throwable e) {
        throw new UnpackerException(e);
      }
    }
  }

  private static int getUnpackVersion() throws IOException {
    final Path versionFile = ROOT.resolve("version");

    if(!Files.isRegularFile(versionFile)) {
      return 0;
    }

    final String versionString = Files.readString(versionFile).strip();

    try {
      return Integer.parseInt(versionString);
    } catch(final NumberFormatException e) {
      return 0;
    }
  }

  private static Tuple<IsoReader, Integer> getIsoReader(final Path path) throws IOException {
    final long fileSize = Files.size(path);

    if(fileSize < PVD_SECTOR * IsoReader.SYNC_PATTER_SIZE) {
      return null;
    }

    final byte[] sectorData = new byte[0x800];
    final ByteBuffer sectorBuffer = ByteBuffer.wrap(sectorData);
    sectorBuffer.order(ByteOrder.LITTLE_ENDIAN);

    final IsoReader reader = new IsoReader(path);
    reader.seekSector(PVD_SECTOR);
    reader.advance(IsoReader.SYNC_PATTER_SIZE);
    reader.read(sectorData);

    if(sectorBuffer.get() != 1 || !"CD001".equals(IoHelper.readString(sectorBuffer, 5)) || sectorBuffer.get() != 0x1 || !"PLAYSTATION".equals(IoHelper.readString(sectorBuffer, 32).trim())) {
      return null;
    }

    final String readId = IoHelper.readString(sectorBuffer, 32).trim();

    for(int i = 0; i < DISK_IDS.length; i++) {
      if(DISK_IDS[i].equals(readId)) {
        return new Tuple<>(reader, i);
      }
    }

    if(OTHER_REGION_IDS.contains(readId)) {
      LOGGER.warn("Found disk %s from another region: %s", readId, path);
    }

    return null;
  }

  private static DirectoryEntry loadRoot(final IsoReader reader, @Nullable final DirectoryEntry destinationTree) throws IOException, UnpackerException {
    final byte[] sectorData = new byte[0x800];
    final ByteBuffer sectorBuffer = ByteBuffer.wrap(sectorData);
    sectorBuffer.order(ByteOrder.LITTLE_ENDIAN);

    synchronized(reader) {
      reader.seekSector(PVD_SECTOR);
      reader.advance(IsoReader.SYNC_PATTER_SIZE);
      reader.read(sectorData);
    }

    final DirectoryEntry rootDir = DirectoryEntry.fromArray(reader, sectorData, 0x9c);
    populateDirectoryTree(rootDir, destinationTree == null ? rootDir : destinationTree);
    return rootDir;
  }

  private static void populateDirectoryTree(final DirectoryEntry source, final DirectoryEntry destinationTree) throws IOException {
    final byte[] sectorData = new byte[0x800];

    synchronized(source.reader()) {
      source.reader().seekSector(source.sector());
      source.reader().advance(12);
      source.reader().read(sectorData);
    }

    int sectorOffset = 0;

    while(sectorData[sectorOffset] != 0) {
      final DirectoryEntry directory = DirectoryEntry.fromArray(source.reader(), sectorData, sectorOffset);

      if(!".".equals(directory.name()) && !"..".equals(directory.name())) {
        destinationTree.children().putIfAbsent(directory.name(), directory);

        if(directory.isDirectory()) {
          populateDirectoryTree(directory, destinationTree.children().get(directory.name()));
        }
      }

      sectorOffset += directory.entryLength();
    }
  }

  private static void getFiles(final DirectoryEntry root, final String path, final Map<String, DirectoryEntry> files) {
    if(!root.isDirectory()) {
      files.put(path + root.name(), root);
    } else {
      for(final DirectoryEntry entry : root.children().values()) {
        if(".".equals(root.name())) {
          getFiles(entry, path, files);
        } else {
          getFiles(entry, path + root.name() + '/', files);
        }
      }
    }
  }

  private static Tuple<String, FileData> readFile(final Map.Entry<String, DirectoryEntry> e) {
    statusListener.accept("Unpacking %s...".formatted(e.getKey()));

    final DirectoryEntry entry = e.getValue();

    synchronized(entry.reader()) {
      final byte[] fileData = entry.reader().readSectors(entry.sector(), entry.length(), e.getKey().endsWith(".IKI") || e.getKey().endsWith(".XA"));
      return new Tuple<>(e.getKey(), new FileData(fileData));
    }
  }

  private static Map<String, FileData> transform(final String name, final FileData data, final Set<String> flags) {
    if(shouldStop) {
      throw new UnpackerStoppedRuntimeException("Unpacking cancelled");
    }

    final Map<String, FileData> entries = new HashMap<>();
    boolean wasTransformed = false;

    for(final var entry : transformers.entrySet()) {
      final var discriminator = entry.getKey();
      final var transformer = entry.getValue();

      if(discriminator.matches(name, data, flags)) {
        wasTransformed = true;
        transformer.transform(name, data, flags)
          .entrySet().stream()
          .map(e -> transform(e.getKey(), e.getValue(), flags))
          .forEach(entries::putAll);
        break;
      }
    }

    if(!wasTransformed) {
      entries.put(name, data);
    }

    return entries;
  }

  private static boolean decompressDiscriminator(final String name, final FileData data, final Set<String> flags) {
    if(ROOT_MRG.matcher(name).matches()) {
      final int dirNum = Integer.parseInt(name.substring(15, 19));
      if(dirNum >= 4031 && dirNum < 4103) {
        return false;
      }
    }

    return data.size() >= 8 && MathHelper.get(data.data(), data.offset() + 4, 4) == 0x1a455042;
  }

  private static Map<String, FileData> decompress(final String name, final FileData data, final Set<String> flags) {
    return Map.of(name, new FileData(Unpacker.decompress(data)));
  }

  private static boolean mrgDiscriminator(final String name, final FileData data, final Set<String> flags) {
    return data.size() >= 8 && MathHelper.get(data.data(), data.offset(), 4) == MrgArchive.MAGIC;
  }

  private static Map<String, FileData> unmrg(final String name, final FileData data, final Set<String> flags) {
    final MrgArchive archive = new MrgArchive(data, name.matches("^SECT/DRGN(?:0|1|2[1234])?.BIN$"));

    if(archive.getCount() == 0) {
      return Map.of(name, EMPTY_DIRECTORY_SENTINEL);
    }

    final Map<String, FileData> files = new HashMap<>();
    int i = 0;
    for(final MrgArchive.Entry entry : archive) {
      if(!entry.virtual()) {
        files.put(name + '/' + i, data.slice(entry.offset(), entry.size()));
      }

      i++;
    }

    final StringBuilder sb = new StringBuilder();

    i = 0;
    for(final MrgArchive.Entry entry : archive) {
      sb.append(i).append('=').append(entry.virtual() ? entry.parent() : i).append('\n');
      i++;
    }

    files.put(name + "/mrg", new FileData(sb.toString().getBytes(StandardCharsets.US_ASCII)));

    return files;
  }

  private static boolean deffDiscriminator(final String name, final FileData data, final Set<String> flags) {
    return data.size() >= 8 && MathHelper.get(data.data(), data.offset(), 4) == 0x46464544;
  }

  private static Map<String, FileData> undeff(final String name, final FileData data, final Set<String> flags) {
    flags.add("DEFF");

    final DeffArchive archive = new DeffArchive(data);

    if(archive.getCount() == 0) {
      return Map.of(name, EMPTY_DIRECTORY_SENTINEL);
    }

    final Map<String, FileData> files = new HashMap<>();
    int i = 0;
    for(final FileData entry : archive) {
      files.put(name + '/' + i, entry);
      i++;
    }

    return files;
  }

  /**
   * DRGN21.402.3 is a submap object script for the screen of the Dragon's Nest
   * with the weird plant you need to use the spring water on. That script has a
   * bug which causes it to jump out of the bounds of the script into the next
   * script. This no longer works in the decomp because the files are no longer
   * adjacent in a MRG file. This patch extends the script to be long enough to
   * contain the jump and just returns.
   */
  private static boolean drgn21_402_3_patcherDiscriminator(final String name, final FileData data, final Set<String> flags) {
    return "SECT/DRGN21.BIN/402/3".equals(name) && data.size() == 0xee4;
  }

  private static Map<String, FileData> drgn21_402_3_patcher(final String name, final FileData data, final Set<String> flags) {
    final byte[] newData = new byte[0x107c];
    System.arraycopy(data.data(), data.offset(), newData, 0, data.size());
    newData[0x1078] = 0x49;
    return Map.of(name, new FileData(newData));
  }

  /**
   * DRGN21.693.0 is the submap controller for Hoax post-Kongol. If you select
   * "I still don't know..." as your response during the dialog, the script tries
   * to push an inline parameter that is past the end of the script. This patcher
   * extends the script and fills with ffffffff, which we read as a sentinel value
   * in {@link Scus94491BpeSegment#scriptReadGlobalFlag2}, and return 0 there.
   */
  private static boolean drgn21_693_0_patcherDiscriminator(final String name, final FileData data, final Set<String> flags) {
    return "SECT/DRGN21.BIN/693/0".equals(name) && data.size() == 0x188;
  }

  private static Map<String, FileData> drgn21_693_0_patcher(final String name, final FileData data, final Set<String> flags) {
    final byte[] newData = new byte[0x190];
    System.arraycopy(data.data(), data.offset(), newData, 0, data.size());
    MathHelper.set(newData, 0x188, 4, 0xffffffffL);
    MathHelper.set(newData, 0x18c, 4, 0xffffffffL);
    return Map.of(name, new FileData(newData));
  }

  /**
   * The Meru model in the post-Divine-Dragon fight cutscene has a corrupt
   * animation file, missing animations for 2 out of her 22 objects. If you
   * look closely at the top of her leg, you can see it. This injects extra
   * animation data for those missing objects made by DooMMetaL.
   */
  private static boolean drgn0_142_animPatcherDiscriminator(final String name, final FileData data, final Set<String> flags) {
    return "SECT/DRGN0.BIN/142".equals(name) && data.size() == 0x1f0;
  }

  private static Map<String, FileData> drgn0_142_animPatcher(final String name, final FileData data, final Set<String> flags) {
    final byte[] newData = new byte[data.size() + 0xc * 2 * 2]; // 2 objects, 2 keyframes, 0xc table pitch
    final byte[] frame = {0x0e, (byte)0xe0, (byte)0xfe, (byte)0xde, (byte)0xff, (byte)0x9d, 0x1d, 0x00, 0x28, 0x03, (byte)0xcb, 0x00};
    // Note: we only create data for object 21, object 22 can be all 0's since it's not visible

    data.copyFrom(0, newData, 0, 0x100);
    System.arraycopy(frame, 0, newData, 0x100, frame.length); // obj 21
    data.copyFrom(0x100, newData, 0x118, 0xf0);
    System.arraycopy(frame, 0, newData, 0x208, frame.length); // obj 21
    newData[0xc] = 22;

    return Map.of(name, new FileData(newData));
  }

  /**
   * During oof, Dart's left hand and forearm are missing for the last parts of the scene.
   * This is because they were turned off to be replaced by the fancy hand for the hand
   * clasp close-up, but were never turned back on. This patch replaces a command to turn
   * off the sword (again, for some reason) with a jump to the end of the script, and
   * injects extra script ops at the end of file to add the hand and arm back and then
   * jump back to the original next op.
   * <p>
   * They also left Lavitz's damaged chest plate visible behind Albert in the distance.
   * This patch also replaces a command to turn an object on with a jump to the end and
   * injects additional script ops to change the z offset of the object to stop it rendering
   * and then move it back.
   */
  private static boolean drgn0_5546_1_patcherDiscriminator(final String name, final FileData data, final Set<String> flags) {
    return "SECT/DRGN0.BIN/5546/1".equals(name) && data.size() == 0x721c;
  }

  private static Map<String, FileData> drgn0_5546_1_patcher(final String name, final FileData data, final Set<String> flags) {
    final byte[] newData = new byte[0x7284];
    final int jump = 0x0000_0140;
    final byte[] address1 = {(byte)0x97, 0x0a, 0x00, 0x09, 0x00, 0x00, 0x00, 0x00, 0x00};
    final byte[] address2 = {(byte)0xcd, 0x08, 0x00, 0x09, 0x00};
    final int address3 = 0x0900_f566;
    final int address4 = 0x0900f728;

    final byte[] subfunc160a = {0x38, 0x03, 0x60, 0x01, 0x0c, 0x00, 0x00, 0x02};
    final byte[] subfunc160b = {0x38, 0x03, 0x60, 0x01, 0x2d, 0x0c, 0x00, 0x0f};
    final byte[] subfunc160params12a = {0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00};
    final byte[] subfunc160params12b = {0x0e, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
    final byte[] subfunc160params12c = {0x05, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00};
    final int subfunc273 = 0x0273_0238;
    final int subfunc273params0 = 0x0f00_1d2d;
    final int subfunc273params1a = 0xffff_f000;
    final int subfunc273params1b = 0xffff_fffa;

    data.copyFrom(0, newData, 0, 0x47c0);
    MathHelper.set(newData, 0x47c0, 4, jump);
    System.arraycopy(address1, 0, newData, 0x47c4, address1.length);
    data.copyFrom(0x47cd, newData, 0x47cd, 0x73f);
    MathHelper.set(newData, 0x4f0c, 4, jump);
    System.arraycopy(address2, 0, newData, 0x4f10, address2.length);
    data.copyFrom(0x4f15, newData, 0x4f15, 0x2307);
    System.arraycopy(subfunc160a, 0, newData, 0x721c, subfunc160a.length);
    System.arraycopy(subfunc160params12a, 0, newData, 0x7224, subfunc160params12a.length);
    MathHelper.set(newData, 0x722c, 4, subfunc273);
    MathHelper.set(newData, 0x7230, 4, subfunc273params0);
    MathHelper.set(newData, 0x7234, 4, subfunc273params1a);
    MathHelper.set(newData, 0x7238, 4, jump);
    MathHelper.set(newData, 0x723c, 4, address3);
    System.arraycopy(subfunc160b, 0, newData, 0x7240, subfunc160b.length);
    System.arraycopy(subfunc160params12b, 0, newData, 0x7248, subfunc160params12b.length);
    System.arraycopy(subfunc160b, 0, newData, 0x7250, subfunc160b.length);
    System.arraycopy(subfunc160params12c, 0, newData, 0x7258, subfunc160params12c.length);
    System.arraycopy(subfunc160b, 0, newData, 0x7260, subfunc160b.length);
    System.arraycopy(subfunc160params12a, 0, newData, 0x7268, subfunc160params12a.length);
    MathHelper.set(newData, 0x7270, 4, subfunc273);
    MathHelper.set(newData, 0x7274, 4, subfunc273params0);
    MathHelper.set(newData, 0x7278, 4, subfunc273params1b);
    MathHelper.set(newData, 0x727c, 4, jump);
    MathHelper.set(newData, 0x7280, 4, address4);
    return Map.of(name, new FileData(newData));
  }

  /**
   * The lava fish animation where it swims around the player is missing the last object
   */
  private static boolean drgn0_3667_16_animPatcherDiscriminator(final String name, final FileData data, final Set<String> flags) {
    return "SECT/DRGN0.BIN/3667/16".equals(name) && data.size() == 0x538;
  }

  private static Map<String, FileData> drgn0_3667_16_animPatcher(final String name, final FileData data, final Set<String> flags) {
    return Map.of(name, new FileData(patchAnimation(data, 11)));
  }

  /**
   * The lava fish animation where it swims around the player is missing the last object
   */
  private static boolean drgn0_3667_17_animPatcherDiscriminator(final String name, final FileData data, final Set<String> flags) {
    return "SECT/DRGN0.BIN/3667/17".equals(name) && data.size() == 0x178;
  }

  private static Map<String, FileData> drgn0_3667_17_animPatcher(final String name, final FileData data, final Set<String> flags) {
    return Map.of(name, new FileData(patchAnimation(data, 11)));
  }

  /**
   * The lava fish animation where it swims around the player is missing the last object
   */
  private static boolean drgn0_3750_16_animPatcherDiscriminator(final String name, final FileData data, final Set<String> flags) {
    return "SECT/DRGN0.BIN/3750/16".equals(name) && data.size() == 0x538;
  }

  private static Map<String, FileData> drgn0_3750_16_animPatcher(final String name, final FileData data, final Set<String> flags) {
    return Map.of(name, new FileData(patchAnimation(data, 11)));
  }

  /**
   * @param expectedObjects Expected number of objects
   */
  private static byte[] patchAnimation(final FileData data, final int expectedObjects) {
    final int actualObjects = data.readUShort(0xc);
    final int keyframes = data.readUShort(0xe) / 2;

    final byte[] newData = new byte[data.size() + 0xc * keyframes * (expectedObjects - actualObjects)];

    data.copyFrom(0, newData, 0, 0x10);

    for(int i = 0; i < keyframes; i++) {
      // This will fill the new objects with the first objects from the next keyframe to emulate retail behaviour. The last keyframe is an exception - it gets zero-filled.
      data.copyFrom(0x10 + i * 0xc * actualObjects, newData, 0x10 + i * 0xc * expectedObjects, Math.min(0xc * expectedObjects, data.size() - (0x10 + i * 0xc * actualObjects)));
    }

    newData[0xc] = (byte)expectedObjects;
    return newData;
  }

  /**
   * Every single enemy script has damage caps build into it for some reason
   */
  private static boolean enemyScriptDamageCapDiscriminator(final String name, final FileData data, final Set<String> flags) {
    return name.startsWith("SECT/DRGN1.BIN/") && !flags.contains(name);
  }

  private static Map<String, FileData> enemyAndItemScriptDamageCapPatcher(final String name, final FileData data, final Set<String> flags) {
    flags.add(name);

    for(int i = 0; i < data.size(); i += 4) {
      if(data.readUShort(i) == 9999 && data.readUShort(i + 0x10) == 9999) {
        data.writeInt(i, 999999999);
        data.writeInt(i + 0x10, 999999999);
        i += 0x10;
      }
    }

    return Map.of(name, data);
  }

  /**
   * Every single item script has damage caps build into it for some reason
   */
  private static boolean itemScriptDamageCapDiscriminator(final String name, final FileData data, final Set<String> flags) {
    if(flags.contains(name)) {
      return false;
    }

    for(int i = 4140; i <= 5500; i += 2) {
      if(name.startsWith("SECT/DRGN0.BIN/" + i + "/1")) {
        return true;
      }
    }

    return false;
  }

  /**
   * In the first Faust battle script file (used for the battle itself), the script
   * tries to access a coord2ArrPtr at index 8 at some moments, but there are only
   * 6 objects in the model. The index is a param hardcoded into the script. This
   * alters the value of that param from 8 to 3, which is the index set by the same
   * function (800ee3c0) when the battle starts.
   */
  private static boolean drgn1_343_patcherDiscriminator(final String name, final FileData data, final Set<String> flags) {
    return "SECT/DRGN1.BIN/343".equals(name) && data.size() == 0x3ab4 && data.readByte(0x3a70) == 0x8;
  }

  private static Map<String, FileData> drgn1_343_patcher(final String name, final FileData data, final Set<String> flags) {
    data.writeByte(0x3a70, 0x3);

    return Map.of(name, data);
  }

  private static boolean playerCombatSoundEffectsDiscriminator(final String name, final FileData data, final Set<String> flags) {
    for(int i = 752; i <= 772; i++) {
      if(name.startsWith("SECT/DRGN0.BIN/" + i + '/')) {
        return true;
      }
    }

    return false;
  }

  private static Map<String, FileData> playerCombatSoundEffectsTransformer(final String name, final FileData data, final Set<String> flags) {
    final Map<String, FileData> files = new HashMap<>();

    if(name.startsWith("SECT/DRGN0.BIN/752/0/")) {
      files.put("characters/dart/sounds/combat/" + name.substring(name.lastIndexOf('/') + 1), data);
    } else if(name.startsWith("SECT/DRGN0.BIN/752/1/")) {
      files.put("characters/lavitz/sounds/combat/" + name.substring(name.lastIndexOf('/') + 1), data);
    } else if(name.startsWith("SECT/DRGN0.BIN/752/2/")) {
      files.put("characters/shana/sounds/combat/" + name.substring(name.lastIndexOf('/') + 1), data);
    } else if(name.startsWith("SECT/DRGN0.BIN/753/2/")) {
      files.put("characters/rose/sounds/combat/" + name.substring(name.lastIndexOf('/') + 1), data);
    } else if(name.startsWith("SECT/DRGN0.BIN/754/2/")) {
      files.put("characters/haschel/sounds/combat/" + name.substring(name.lastIndexOf('/') + 1), data);
    } else if(name.startsWith("SECT/DRGN0.BIN/755/2/")) {
      files.put("characters/meru/sounds/combat/" + name.substring(name.lastIndexOf('/') + 1), data);
    } else if(name.startsWith("SECT/DRGN0.BIN/756/2/")) {
      files.put("characters/kongol/sounds/combat/" + name.substring(name.lastIndexOf('/') + 1), data);
    } else if(name.startsWith("SECT/DRGN0.BIN/757/2/")) {
      files.put("characters/miranda/sounds/combat/" + name.substring(name.lastIndexOf('/') + 1), data);
    } else if(name.startsWith("SECT/DRGN0.BIN/772/1/")) {
      files.put("characters/albert/sounds/combat/" + name.substring(name.lastIndexOf('/') + 1), data);
    }

    return files;
  }

  private static boolean playerCombatModelsAndTexturesDiscriminator(final String name, final FileData data, final Set<String> flags) {
    for(int i = 3993; i <= 4010; i++) {
      if(name.startsWith("SECT/DRGN0.BIN/" + i + '/') && (!name.endsWith("mrg") || i % 2 == 0)) { // Only copy MRG files for models
        return true;
      }
    }

    return false;
  }

  private static Map<String, FileData> playerCombatModelsAndTexturesTransformer(final String name, final FileData data, final Set<String> flags) {
    final Map<String, FileData> files = new HashMap<>();

    for(int charId = 0; charId < 9; charId++) {
      final String charName = getCharacterName(charId).toLowerCase();

      if(name.startsWith("SECT/DRGN0.BIN/%d".formatted(3993 + charId * 2))) {
        files.put("characters/%s/textures/combat".formatted(charName), data);
      } else if(name.startsWith("SECT/DRGN0.BIN/%d".formatted(3994 + charId * 2))) {
        files.put("characters/%s/models/combat/%s".formatted(charName, name.substring(name.lastIndexOf('/') + 1)), data);
      }
    }

    return files;
  }

  private static boolean dragoonCombatModelsAndTexturesDiscriminator(final String name, final FileData data, final Set<String> flags) {
    for(int i = 4011; i <= 4030; i++) {
      if(name.startsWith("SECT/DRGN0.BIN/" + i + '/') && (!name.endsWith("mrg") || i % 2 == 0)) { // Only copy MRG files for models
        return true;
      }
    }

    return false;
  }

  private static Map<String, FileData> dragoonCombatModelsAndTexturesTransformer(final String name, final FileData data, final Set<String> flags) {
    final Map<String, FileData> files = new HashMap<>();

    for(int charId = 0; charId < 10; charId++) {
      final String charName = getCharacterName(charId).toLowerCase();

      if(name.startsWith("SECT/DRGN0.BIN/%d".formatted(4011 + charId * 2))) {
        files.put("characters/%s/textures/dragoon".formatted(charName), data);
      } else if(name.startsWith("SECT/DRGN0.BIN/%d".formatted(4012 + charId * 2))) {
        files.put("characters/%s/models/dragoon/%s".formatted(charName, name.substring(name.lastIndexOf('/') + 1)), data);
      }
    }

    return files;
  }

  private static boolean skipPartyPermutationsDiscriminator(final String name, final FileData data, final Set<String> flags) {
    for(int i = 3537; i <= 3592; i++) {
      if(name.startsWith("SECT/DRGN0.BIN/" + i + '/')) {
        return true;
      }
    }

    return false;
  }

  private static Map<String, FileData> skipPartyPermutationsTransformer(final String name, final FileData data, final Set<String> flags) {
    return Map.of();
  }

  /** Extracts some S_BTLD tables/scripts */
  private static boolean extractBtldDataDiscriminator(final String name, final FileData data, final Set<String> flags) {
    return "OVL/S_BTLD.OV_".equals(name) && !flags.contains("S_BTLD");
  }

  private static Map<String, FileData> extractBtldDataTransformer(final String name, final FileData data, final Set<String> flags) {
    flags.add("S_BTLD");

    final Map<String, FileData> files = new HashMap<>();
    files.put(name, data);
    files.put("encounters", data.slice(0x68d8, 0x7000));
    files.put("player_combat_script", data.slice(0x4, 0x68d4));
    return files;
  }

  private static boolean playerScriptDamageCapsDiscriminator(final String name, final FileData data, final Set<String> flags) {
    return "player_combat_script".equals(name) && data.readInt(4) != MrgArchive.MAGIC && !flags.contains(name);
  }

  private static Map<String, FileData> playerScriptDamageCapsTransformer(final String name, final FileData data, final Set<String> flags) {
    flags.add(name);

    data.writeInt(0x9f0, 999999999);
    data.writeInt(0xa00, 999999999);
    data.writeInt(0x82b0, 999999999);
    data.writeInt(0x82c0, 999999999);
    data.writeInt(0xe190, 999999999);
    data.writeInt(0xe1a0, 999999999);

    return Map.of(name, data);
  }

  private static boolean extractItemDataDiscriminator(final String name, final FileData data, final Set<String> flags) {
    return "OVL/S_ITEM.OV_".equals(name) && !flags.contains("S_ITEM");
  }

  private static Map<String, FileData> extractItemDataTransformer(final String name, final FileData data, final Set<String> flags) {
    flags.add("S_ITEM");

    final Map<String, FileData> files = new HashMap<>();
    files.put(name, data);
    files.put("characters/kongol/xp", new FileData(data.data(), 0x17d78, 61 * 4));
    files.put("characters/dart/xp", new FileData(data.data(), 0x17e6c, 61 * 4));
    files.put("characters/haschel/xp", new FileData(data.data(), 0x17f60, 61 * 4));
    files.put("characters/meru/xp", new FileData(data.data(), 0x18054, 61 * 4));
    files.put("characters/lavitz/xp", new FileData(data.data(), 0x18148, 61 * 4));
    files.put("characters/albert/xp", new FileData(data.data(), 0x18148, 61 * 4));
    files.put("characters/rose/xp", new FileData(data.data(), 0x1823c, 61 * 4));
    files.put("characters/shana/xp", new FileData(data.data(), 0x18330, 61 * 4));
    files.put("characters/miranda/xp", new FileData(data.data(), 0x18330, 61 * 4));
    return files;
  }

  private static boolean uiPatcherDiscriminator(final String name, final FileData data, final Set<String> flags) {
    return "SECT/DRGN0.BIN/6666".equals(name) && data.readByte(0x24a8) != 0;
  }

  private static Map<String, FileData> uiPatcherTransformer(final String name, final FileData data, final Set<String> flags) {
    // Remove the baked-in slashes in the fractions for character cards
    for(int i = 0; i < 3; i++) {
      data.writeByte(0x24a8 + i * 0x14, 0);
      data.writeByte(0x24aa + i * 0x14, 0);
    }

    return Map.of(name, data);
  }

  private static void writeFiles(final Map<String, FileData> files) {
    files.forEach(Unpacker::writeFile);
  }

  private static void writeFile(final String name, final FileData data) {
    if(data.realFileIndex() != -1) {
      return;
    }

    final Path path = ROOT.resolve(name);

    try {
      if(data == EMPTY_DIRECTORY_SENTINEL) {
        Files.createDirectories(path);
        return;
      }

      Files.createDirectories(path.getParent());

      try(final OutputStream writer = Files.newOutputStream(path, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
        writer.write(data.data(), data.offset(), data.size());
      }
    } catch(final IOException ex) {
      throw new UnpackerException(ex);
    }
  }

  public static byte[] decompress(final FileData archive) {
    // Check BPE header - this check is in the BPE block method but not the main EXE method
    if(archive.readInt(0x4) != 0x1a455042) {
      throw new RuntimeException("Attempted to decompress non-BPE segment");
    }

    LOGGER.info("Decompressing BPE segment");

    final byte[] dest = new byte[archive.readInt(0)];

    final Deque<Byte> unresolved_byte_list = new LinkedList<>();
    final byte[] dict_leftch = new byte[0x100];
    final byte[] dict_rightch = new byte[0x100];

    int totalSize = 0;
    int archiveOffset = 0x8;
    int destinationOffset = 0;

    // Each block is preceded by 4-byte int up to 0x800 giving the number
    // of decompressed bytes in the block. 0x00000000 indicates that there
    // are no further blocks and decompression is complete.
    int bytes_remaining_in_block = archive.readInt(archiveOffset);
    archiveOffset += 4;

    while(bytes_remaining_in_block != 0) {
      if(bytes_remaining_in_block > 0x800) {
        LOGGER.error("Decompress: 0x%08x at offset 0x%08x is an invalid block size", bytes_remaining_in_block, archiveOffset - 4);
        throw new RuntimeException("Decompression error");
      }

      totalSize += bytes_remaining_in_block;

      // Build the initial dictionary/lookup table. The left-character dict
      // is filled so that each key contains itself as a value, while the
      // right-character dict is filled with empty values.
      Arrays.fill(dict_rightch, (byte)0);
      for(int i = 0; i < 0x100; i++) {
        dict_leftch[i] = (byte)i;
      }

      // Build adaptive dictionary.
      int key = 0;
      while(key < 0x100) {
        // Dictionary is 256 bytes long. Loop until all keys filled.
        // If byte_pairs_to_read is >= 0x80, then only the next byte will
        // be read into the dictionary, placed at the index value calculated
        // using the below formula. Otherwise, the byte indicates how many
        // sequential bytes to read into the dictionary.
        int byte_pairs_to_read = archive.readUByte(archiveOffset);
        archiveOffset++;

        if(byte_pairs_to_read >= 0x80) {
          key = key - 0x7f + byte_pairs_to_read;
          byte_pairs_to_read = 0;
        }

        // For each byte/byte pair to read, read the next byte and add it
        // to the leftch dict at the current key. If the character matches
        // the key it's at, increment key and continue. If it does not,
        // read the next character and add it to the same key in the
        // rightch dict before incrementing key and continuing.
        if(key < 0x100) {
          // Check that dictionary length not exceeded.
          for(int i = 0; i < byte_pairs_to_read + 1; i++) {
            dict_leftch[key] = archive.readByte(archiveOffset);
            archiveOffset++;

            if((dict_leftch[key] & 0xff) != key) {
              dict_rightch[key] = archive.readByte(archiveOffset);
              archiveOffset++;
            }

            key++;
          }
        }
      }

      // Decompress block
      // On each pass, read one byte and add it to a list of unresolved bytes.
      while(bytes_remaining_in_block > 0) {
        unresolved_byte_list.clear();
        unresolved_byte_list.push(archive.readByte(archiveOffset));
        archiveOffset++;

        // Pop the first item in the list of unresolved bytes. If the
        // byte key == value in dict_leftch, append it to the list of
        // decompressed bytes. If the byte key !=value in dict_leftch,
        // insert the leftch followed by rightch to the unresolved byte
        // list. Loop until the unresolved byte list is empty.
        while(!unresolved_byte_list.isEmpty()) {
          final byte compressed_byte = unresolved_byte_list.pop();
          if(compressed_byte == dict_leftch[compressed_byte & 0xff]) {
            dest[destinationOffset] = compressed_byte;
            destinationOffset++;
            bytes_remaining_in_block--;
          } else {
            unresolved_byte_list.push(dict_rightch[compressed_byte & 0xff]);
            unresolved_byte_list.push(dict_leftch[compressed_byte & 0xff]);
          }
        }
      }

      if(archiveOffset % 4 != 0) {
        // Word - align the pointer.
        archiveOffset = archiveOffset + 4 - archiveOffset % 4;
      }

      bytes_remaining_in_block = archive.readInt(archiveOffset);
      archiveOffset += 4;
    }

    LOGGER.info("Archive size: %d, decompressed size: %d", archiveOffset, totalSize);

    return dest;
  }

  @FunctionalInterface
  public interface Discriminator {
    boolean matches(final String name, final FileData data, final Set<String> flags);
  }

  @FunctionalInterface
  public interface Transformer {
    Map<String, FileData> transform(final String name, final FileData data, final Set<String> flags);
  }
}
