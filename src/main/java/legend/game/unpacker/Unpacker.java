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
import java.util.Deque;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.StreamSupport;

import static legend.game.Scus94491BpeSegment.getCharacterName;

public final class Unpacker {
  private Unpacker() { }

  private static final Pattern ROOT_MRG = Pattern.compile("^SECT/DRGN0\\.BIN/\\d{4}/\\d+$");

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
    transformers.put(Unpacker::drgn0_142_patcherDiscriminator, Unpacker::drgn0_142_patcher);
    transformers.put(Unpacker::drgn1_343_patcherDiscriminator, Unpacker::drgn1_343_patcher);
    transformers.put(Unpacker::playerCombatSoundEffectsDiscriminator, Unpacker::playerCombatSoundEffectsTransformer);
    transformers.put(Unpacker::playerCombatModelsAndTexturesDiscriminator, Unpacker::playerCombatModelsAndTexturesTransformer);
    transformers.put(Unpacker::dragoonCombatModelsAndTexturesDiscriminator, Unpacker::dragoonCombatModelsAndTexturesTransformer);
    transformers.put(Unpacker::skipPartyPermutationsDiscriminator, Unpacker::skipPartyPermutationsTransformer);
    transformers.put(Unpacker::extractBtldDataDiscriminator, Unpacker::extractBtldDataTransformer);
    transformers.put(Unpacker::extractItemDataDiscriminator, Unpacker::extractItemDataTransformer);
    transformers.put(Unpacker::uiPatcherDiscriminator, Unpacker::uiPatcherTransformer);
    transformers.put(CtmdTransformer::ctmdDiscriminator, CtmdTransformer::ctmdTransformer);
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
        final long start = System.nanoTime();

        final DirectoryEntry[] roots = new DirectoryEntry[4];
        final String[] ids = {"SCUS94491", "SCUS94584", "SCUS94585", "SCUS94586"};

        final IsoReader reader4 = new IsoReader(Path.of(".", "isos", "4.iso"));
        final DirectoryEntry root = loadRoot(reader4, ids[3], null);

        for(int i = 0; i < roots.length; i++) {
          final IsoReader reader = new IsoReader(Path.of(".", "isos", (i + 1) + ".iso"));
          loadRoot(reader, ids[i], root);
        }

        final Map<String, DirectoryEntry> files = new HashMap<>();
        getFiles(root, "", files);

        files.entrySet()
          .stream()
          .filter(entry -> !Files.exists(ROOT.resolve(entry.getKey())))
          .map(Unpacker::readFile)
          .map(e -> transform(e.a(), e.b(), EnumSet.noneOf(Flags.class)))
          .forEach(Unpacker::writeFiles);

        LOGGER.info("Files unpacked in %d seconds", (System.nanoTime() - start) / 1_000_000_000L);
      } catch(final Throwable e) {
        throw new UnpackerException(e);
      }
    }
  }

  private static DirectoryEntry loadRoot(final IsoReader reader, final String id, @Nullable final DirectoryEntry destinationTree) throws IOException, UnpackerException {
    final byte[] sectorData = new byte[0x800];
    final ByteBuffer sectorBuffer = ByteBuffer.wrap(sectorData);
    sectorBuffer.order(ByteOrder.LITTLE_ENDIAN);

    synchronized(reader) {
      reader.seekSector(16);
      reader.advance(12);
      reader.read(sectorData);
    }

    if(sectorBuffer.get() != 1) {
      throw new UnpackerException("Invalid volume descriptor, expected primary");
    }

    if(!"CD001".equals(IoHelper.readString(sectorBuffer, 5))) {
      throw new UnpackerException("Invalid volume descriptor, expected CD001");
    }

    if(sectorBuffer.get() != 0x1) {
      throw new UnpackerException("Invalid volume descriptor, expected version 1");
    }

    if(!"PLAYSTATION".equals(IoHelper.readString(sectorBuffer, 32).trim())) {
      throw new UnpackerException("Invalid volume descriptor, expected PLAYSTATION");
    }

    if(!id.equals(IoHelper.readString(sectorBuffer, 32).trim())) {
      throw new UnpackerException("Invalid volume descriptor, expected " + id);
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

  private static Map<String, FileData> transform(final String name, final FileData data, final Set<Flags> flags) {
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

  private static boolean decompressDiscriminator(final String name, final FileData data, final Set<Flags> flags) {
    if(ROOT_MRG.matcher(name).matches()) {
      final int dirNum = Integer.parseInt(name.substring(15, 19));
      if(dirNum >= 4031 && dirNum < 4103) {
        return false;
      }
    }

    return data.size() >= 8 && MathHelper.get(data.data(), data.offset() + 4, 4) == 0x1a455042;
  }

  private static Map<String, FileData> decompress(final String name, final FileData data, final Set<Flags> flags) {
    return Map.of(name, new FileData(Unpacker.decompress(data)));
  }

  private static boolean mrgDiscriminator(final String name, final FileData data, final Set<Flags> flags) {
    return data.size() >= 8 && MathHelper.get(data.data(), data.offset(), 4) == 0x1a47524d;
  }

  private static Map<String, FileData> unmrg(final String name, final FileData data, final Set<Flags> flags) {
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

  private static boolean deffDiscriminator(final String name, final FileData data, final Set<Flags> flags) {
    return data.size() >= 8 && MathHelper.get(data.data(), data.offset(), 4) == 0x46464544;
  }

  private static Map<String, FileData> undeff(final String name, final FileData data, final Set<Flags> flags) {
    flags.add(Flags.DEFF);

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
  private static boolean drgn21_402_3_patcherDiscriminator(final String name, final FileData data, final Set<Flags> flags) {
    return "SECT/DRGN21.BIN/402/3".equals(name) && data.size() == 0xee4;
  }

  private static Map<String, FileData> drgn21_402_3_patcher(final String name, final FileData data, final Set<Flags> flags) {
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
  private static boolean drgn21_693_0_patcherDiscriminator(final String name, final FileData data, final Set<Flags> flags) {
    return "SECT/DRGN21.BIN/693/0".equals(name) && data.size() == 0x188;
  }

  private static Map<String, FileData> drgn21_693_0_patcher(final String name, final FileData data, final Set<Flags> flags) {
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
  private static boolean drgn0_142_patcherDiscriminator(final String name, final FileData data, final Set<Flags> flags) {
    return "SECT/DRGN0.BIN/142".equals(name) && data.size() == 0x1f0;
  }

  private static Map<String, FileData> drgn0_142_patcher(final String name, final FileData data, final Set<Flags> flags) {
    final byte[] newData = new byte[data.size() + 0xc * 2 * 2]; // 2 objects, 2 frames, 0xc table pitch
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
   * In the first Faust battle script file (used for the battle itself), the script
   * tries to access a coord2ArrPtr at index 8 at some moments, but there are only
   * 6 objects in the model. The index is a param hardcoded into the script. This
   * alters the value of that param from 8 to 3, which is the index set by the same
   * function (800ee3c0) when the battle starts.
   */
  private static boolean drgn1_343_patcherDiscriminator(final String name, final FileData data, final Set<Flags> flags) {
    return "SECT/DRGN1.BIN/343".equals(name) && data.size() == 0x3ab4 && data.readByte(0x3a70) == 0x8;
  }

  private static Map<String, FileData> drgn1_343_patcher(final String name, final FileData data, final Set<Flags> flags) {
    data.writeByte(0x3a70, 0x3);

    return Map.of(name, data);
  }

  private static boolean playerCombatSoundEffectsDiscriminator(final String name, final FileData data, final Set<Flags> flags) {
    for(int i = 752; i <= 772; i++) {
      if(name.startsWith("SECT/DRGN0.BIN/" + i + '/')) {
        return true;
      }
    }

    return false;
  }

  private static Map<String, FileData> playerCombatSoundEffectsTransformer(final String name, final FileData data, final Set<Flags> flags) {
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

  private static boolean playerCombatModelsAndTexturesDiscriminator(final String name, final FileData data, final Set<Flags> flags) {
    for(int i = 3993; i <= 4010; i++) {
      if(name.startsWith("SECT/DRGN0.BIN/" + i + '/') && (!name.endsWith("mrg") || i % 2 == 0)) { // Only copy MRG files for models
        return true;
      }
    }

    return false;
  }

  private static Map<String, FileData> playerCombatModelsAndTexturesTransformer(final String name, final FileData data, final Set<Flags> flags) {
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

  private static boolean dragoonCombatModelsAndTexturesDiscriminator(final String name, final FileData data, final Set<Flags> flags) {
    for(int i = 4011; i <= 4030; i++) {
      if(name.startsWith("SECT/DRGN0.BIN/" + i + '/') && (!name.endsWith("mrg") || i % 2 == 0)) { // Only copy MRG files for models
        return true;
      }
    }

    return false;
  }

  private static Map<String, FileData> dragoonCombatModelsAndTexturesTransformer(final String name, final FileData data, final Set<Flags> flags) {
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

  private static boolean skipPartyPermutationsDiscriminator(final String name, final FileData data, final Set<Flags> flags) {
    for(int i = 3537; i <= 3592; i++) {
      if(name.startsWith("SECT/DRGN0.BIN/" + i + '/')) {
        return true;
      }
    }

    return false;
  }

  private static Map<String, FileData> skipPartyPermutationsTransformer(final String name, final FileData data, final Set<Flags> flags) {
    return Map.of();
  }

  /** TODO this is pretty bad */
  private static boolean btldDataDiscriminatorLatch = true;
  private static boolean itemDataDiscriminatorLatch = true;
  /** Extracts table at 80102050 */
  private static boolean extractBtldDataDiscriminator(final String name, final FileData data, final Set<Flags> flags) {
    return btldDataDiscriminatorLatch && "OVL/S_BTLD.OV_".equals(name);
  }

  private static Map<String, FileData> extractBtldDataTransformer(final String name, final FileData data, final Set<Flags> flags) {
    btldDataDiscriminatorLatch = false;
    final Map<String, FileData> files = new HashMap<>();
    files.put(name, data);
    files.put("encounters", new FileData(data.data(), 0x68d8, 0x7000));
    return files;
  }

  private static boolean extractItemDataDiscriminator(final String name, final FileData data, final Set<Flags> flags) {
    return itemDataDiscriminatorLatch && "OVL/S_ITEM.OV_".equals(name);
  }

  private static Map<String, FileData> extractItemDataTransformer(final String name, final FileData data, final Set<Flags> flags) {
    itemDataDiscriminatorLatch = false;
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

  private static boolean uiPatcherDiscriminator(final String name, final FileData data, final Set<Flags> flags) {
    return "SECT/DRGN0.BIN/6666".equals(name) && data.readByte(0x24a8) != 0;
  }

  private static Map<String, FileData> uiPatcherTransformer(final String name, final FileData data, final Set<Flags> flags) {
    // Remove the baked-in slashes in the fractions for character cards
    for(int i = 0; i < 3; i++) {
      data.writeByte(0x24a8 + i * 14, 0);
      data.writeByte(0x24aa + i * 14, 0);
    }

    return Map.of(name, data);
  }

  private static void writeFiles(final Map<String, FileData> files) {
    files.forEach(Unpacker::writeFile);
  }

  private static void writeFile(final String name, final FileData data) {
    if(!data.real()) {
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

  public enum Flags {
    DEFF,
  }

  @FunctionalInterface
  public interface Discriminator {
    boolean matches(final String name, final FileData data, final Set<Flags> flags);
  }

  @FunctionalInterface
  public interface Transformer {
    Map<String, FileData> transform(final String name, final FileData data, final Set<Flags> flags);
  }
}
