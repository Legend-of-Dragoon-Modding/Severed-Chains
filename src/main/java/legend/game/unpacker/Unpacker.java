package legend.game.unpacker;

import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import legend.core.IoHelper;
import legend.core.MathHelper;
import legend.core.Tuple;
import legend.core.audio.xa.XaTranscoder;
import legend.game.Scus94491BpeSegment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static legend.game.Scus94491BpeSegment.getCharacterName;

public final class Unpacker {
  private static final Pattern MRG_ENTRY = Pattern.compile("[=;]");

  private Unpacker() { }

  private static final String[] DISK_IDS = {"SCUS94491", "SCUS94584", "SCUS94585", "SCUS94586"};
  private static final List<String> OTHER_REGION_IDS = List.of("SCES03043", "SCES13043", "SCES23043", "SCES33043", "SCES03044", "SCES13044", "SCES23044", "SCES33044", "SCES03045", "SCES13045", "SCES23045", "SCES33045", "SCES03046", "SCES13046", "SCES23046", "SCES33046", "SCES03047", "SCES13047", "SCES23047", "SCES33047", "SCPS10119", "SCPS10120", "SCPS10121", "SCPS10122", "SCPS45461", "SCPS45462", "SCPS45463", "SCPS45464");
  private static final int PVD_SECTOR = 16;

  private static final Pattern ROOT_MRG = Pattern.compile("^SECT/DRGN0\\.BIN/\\d{4}/\\d+$");
  private static final Pattern DRGN0_FILE = Pattern.compile("^SECT/DRGN0.BIN/\\d+/.*");
  private static final Pattern ITEM_SCRIPT = Pattern.compile("^SECT/DRGN0.BIN/\\d+/1.*");

  /** Update this any time we make a breaking change */
  private static final int VERSION = 3;

  static {
    System.setProperty("log4j.skipJansi", "false");
    System.setProperty("log4j2.configurationFile", "log4j2.xml");
  }

  public static final Logger LOGGER = LogManager.getFormatterLogger(Unpacker.class);

  public static Path ROOT = Path.of(".", "files");

  private static final FileData EMPTY_DIRECTORY_SENTINEL = new FileData(new byte[0]);

  private static final int availableProcessors = Runtime.getRuntime().availableProcessors();
  private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(availableProcessors);
  private static final AtomicInteger loadingCount = new AtomicInteger();

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

    transformers.put(Unpacker::engineOverlayDiscriminator, Unpacker::engineOverlayExtractor);

    transformers.put(Unpacker::drgn21_402_3_patcherDiscriminator, Unpacker::drgn21_402_3_patcher);
    transformers.put(Unpacker::drgn21_693_0_patcherDiscriminator, Unpacker::drgn21_693_0_patcher);
    transformers.put(Unpacker::drgn0_142_animPatcherDiscriminator, Unpacker::drgn0_142_animPatcher);

    // Item, equipment, spells, XP, and TIMs from lod_engine
    transformers.put(Unpacker::lodEngineDiscriminator, Unpacker::lodEngineExtractor);
    transformers.put(Unpacker::equipmentAndXpDiscriminator, Unpacker::equipmentAndXpExtractor);
    transformers.put(Unpacker::spellsDiscriminator, Unpacker::spellsExtractor);

    // Savepoint etc. from SMAP
    transformers.put(Unpacker::smapAssetDiscriminator, Unpacker::smapAssetExtractor);

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
    transformers.put(Unpacker::uiPatcherDiscriminator, Unpacker::uiPatcherTransformer);
    transformers.put(CtmdTransformer::ctmdDiscriminator, CtmdTransformer::ctmdTransformer);

    // Remove damage caps from scripts
    transformers.put(Unpacker::playerScriptDamageCapsDiscriminator, Unpacker::playerScriptDamageCapsTransformer);
    transformers.put(Unpacker::enemyScriptDamageCapDiscriminator, Unpacker::enemyAndItemScriptDamageCapPatcher);
    transformers.put(Unpacker::itemScriptDamageCapDiscriminator, Unpacker::enemyAndItemScriptDamageCapPatcher);

    transformers.put(Unpacker::xaDiscriminator, Unpacker::xaTransformer);
  }

  private static final List<Transformer> postTransformers = new ArrayList<>();
  static {
    // Convert submap PXLs into individual TIMs
    postTransformers.add(SubmapPxlTransformer::transform);
  }

  private static Consumer<String> statusListener = status -> { };
  private static boolean shouldStop;

  public static void main(final String[] args) throws UnpackerException {
    unpack();
    EXECUTOR.shutdown();
  }

  public static void stop() {
    shouldStop = true;
  }

  public static void setStatusListener(final Consumer<String> listener) {
    statusListener = listener;
  }

  public static void shutdownLoader() {
    EXECUTOR.shutdown();
  }

  public static FileData loadFile(final String name) {
    LOGGER.info("Loading file %s", name);

    try {
      return new FileData(Files.readAllBytes(ROOT.resolve(fixPath(name))));
    } catch(final IOException e) {
      throw new RuntimeException("Failed to load file " + name, e);
    }
  }

  public static void loadFile(final String name, final Consumer<FileData> onCompletion) {
    final int total = loadingCount.incrementAndGet();
    LOGGER.info("Queueing file %s (total queued: %d)", name, total);
    EXECUTOR.execute(() -> {
      onCompletion.accept(loadFile(name));
      final int remaining = loadingCount.decrementAndGet();
      LOGGER.info("File %s loaded (remaining queued: %d)", name, remaining);
    });
  }

  public static void loadFiles(final Consumer<List<FileData>> onCompletion, final String... files) {
    final int total = loadingCount.updateAndGet(i -> i + files.length);
    LOGGER.info("Queueing files %s (total queued: %d)", Arrays.toString(files), total);

    EXECUTOR.execute(() -> {
      final List<FileData> fileData = new ArrayList<>();
      for(final String file : files) {
        final FileData data = Unpacker.loadFile(file);
        fileData.add(data);
      }

      onCompletion.accept(fileData);
      final int remaining = loadingCount.updateAndGet(i -> i - files.length);
      LOGGER.info("Files %s loaded (remaining queued: %d)", Arrays.toString(files), remaining);
    });
  }

  public static void loadDirectory(final String name, final Consumer<List<FileData>> onCompletion) {
    final int total = loadingCount.incrementAndGet();
    LOGGER.info("Queueing directory %s (total queued: %d)", name, total);
    EXECUTOR.execute(() -> {
      onCompletion.accept(loadDirectory(name));
      final int remaining = loadingCount.decrementAndGet();
      LOGGER.info("Directory %s loaded (remaining queued: %d)", name, remaining);
    });
  }

  public static List<FileData> loadDirectory(final String name) {
    LOGGER.info("Loading directory %s", name);

    final Path dir = ROOT.resolve(fixPath(name));
    final Path mrg = dir.resolve("mrg");

    if(Files.exists(mrg)) {
      try(final BufferedReader reader = Files.newBufferedReader(mrg)) {
        final Int2IntMap fileMap = new Int2IntArrayMap();
        final Int2IntMap virtualSizeMap = new Int2IntArrayMap();

        reader.lines().forEach(line -> {
          final String[] parts = MRG_ENTRY.split(line);

          if(parts.length != 3) {
            throw new RuntimeException("Invalid MRG entry! " + line);
          }

          final int virtual = Integer.parseInt(parts[0]);

          // Indicates no file
          if(parts[1].isBlank()) {
            fileMap.put(virtual, -1);
            virtualSizeMap.put(virtual, 0);
            return;
          }

          final int real = Integer.parseInt(parts[1]);
          fileMap.put(virtual, real);
          virtualSizeMap.put(virtual, Integer.parseInt(parts[2]));
        });

        final List<FileData> files = new ArrayList<>();

        // Add real files
        for(final var entry : fileMap.int2IntEntrySet()) {
          final int virtual = entry.getIntKey();
          final int real = entry.getIntValue();

          // No file
          if(real == -1) {
            files.add(null);
            continue;
          }

          try {
            final Path file = dir.resolve(String.valueOf(real));
            if(Files.isRegularFile(file)) {
              if(virtual == real) {
                files.add(new FileData(Files.readAllBytes(file)));
              } else {
                files.add(null);
              }
            } else if(Files.isDirectory(file)) {
              files.add(new FileData(new byte[0]));
            }
          } catch(final IOException e) {
            throw new RuntimeException("Failed to load directory " + name, e);
          }
        }

        // Add virtual files
        for(final var entry : fileMap.int2IntEntrySet()) {
          final int virtual = entry.getIntKey();
          int real = entry.getIntValue();

          if(virtual == real || real == -1) {
            continue;
          }

          // Resolve to the realest file
          while(fileMap.get(real) != real) {
            real = fileMap.get(real);
          }

          final Path file = dir.resolve(String.valueOf(real));
          if(Files.isRegularFile(file)) {
            files.set(virtual, FileData.virtual(files.get(real), virtualSizeMap.get(virtual), real));
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

  public static int getLoadingFileCount() {
    return loadingCount.get();
  }

  public static boolean exists(final String name) {
    return Files.exists(ROOT.resolve(fixPath(name)));
  }

  public static boolean isDirectory(final String name) {
    return Files.isDirectory(ROOT.resolve(fixPath(name)));
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
    try {
      Files.createDirectories(ROOT);
      Files.createDirectories(Path.of("./isos"));

      if(getUnpackVersion() != VERSION) {
        final long start = System.nanoTime();

        statusListener.accept("Deleting old unpacked files...");
        LOGGER.info("Deleting old unpacked files...");
        deleteUnpack();
        LOGGER.info("Files deleted in %d seconds", (System.nanoTime() - start) / 1_000_000_000L);
      }

      final long start = System.nanoTime();
      final IsoReader[] readers = getIsoReaders();
      final DirectoryEntry[] roots = new DirectoryEntry[4];
      final DirectoryEntry root = loadRoot(readers[3], null);

      for(int i = 0; i < roots.length - 1; i++) {
        loadRoot(readers[i], root);
      }

      statusListener.accept("Loading disk images...");

      final long fileTreeTime = System.nanoTime();
      LOGGER.info("Populating initial file tree...");

      final PathNode files = new PathNode("", "", null, null);
      final Queue<PathNode> transformationQueue = new LinkedList<>();
      populateInitialFileTree(root, "", files, transformationQueue);

      LOGGER.info("Initial file tree populated in %fs", (System.nanoTime() - fileTreeTime) / 1_000_000_000.0f);

      if(!transformationQueue.isEmpty()) {
        statusListener.accept("Transforming files...");

        final long leafTransformTime = System.nanoTime();
        LOGGER.info("Performing leaf transformations...");

        final Transformations transformations = new Transformations(files, transformationQueue);
        final Set<String> flags = Collections.synchronizedSet(new HashSet<>());
        final AtomicInteger activeThreads = new AtomicInteger();
        final Object lock = new Object();

        // Submit queued files to the executor in batches of 100
        executeInParallel(activeThreads, lock, () -> {
          int i = 0;
          PathNode nodeToTransform;
          while(i < 100 && (nodeToTransform = transformations.poll()) != null) {
            transform(nodeToTransform, transformations, flags);
            i++;
          }
        }, () -> !transformationQueue.isEmpty());

        LOGGER.info("Leaf transformations completed in %fs", (System.nanoTime() - leafTransformTime) / 1_000_000_000.0f);

        statusListener.accept("Transforming directories...");

        final long branchTransformTime = System.nanoTime();
        LOGGER.info("Performing branch transformations...");

        postTransformers.parallelStream().forEach(transformer -> transformer.transform(files, transformations, flags));

        LOGGER.info("Branch transformations completed in %fs", (System.nanoTime() - branchTransformTime) / 1_000_000_000.0f);

        final Deque<PathNode> all = new ConcurrentLinkedDeque<>();
        files.flatten(all);

        final long writeTime = System.nanoTime();
        LOGGER.info("Writing %d files...", all.size());

        executeInParallel(activeThreads, lock, () -> {
          statusListener.accept("Writing %d files...".formatted(all.size()));

          int i = 0;
          PathNode node;
          while(i < 100 && (node = all.poll()) != null) {
            writeFile(node);
            i++;
          }
        }, () -> !all.isEmpty());

        LOGGER.info("Files written in %fs", (System.nanoTime() - writeTime) / 1_000_000_000.0f);

        Files.writeString(ROOT.resolve("version"), Integer.toString(VERSION));
      }

      statusListener.accept("");
      LOGGER.info("Files unpacked in %fs", (System.nanoTime() - start) / 1_000_000_000.0f);
    } catch(final UnpackerException e) {
      throw e;
    } catch(final Throwable e) {
      throw new UnpackerException(e);
    }
  }

  private static void executeInParallel(final AtomicInteger activeThreads, final Object lock, final Runnable action, final BooleanSupplier condition) throws InterruptedException {
    do {
      if(activeThreads.get() >= availableProcessors) {
        synchronized(lock) {
          lock.wait(100);
        }
      } else {
        activeThreads.incrementAndGet();

        EXECUTOR.execute(() -> {
          action.run();
          activeThreads.decrementAndGet();

          synchronized(lock) {
            lock.notifyAll();
          }
        });
      }
    } while(condition.getAsBoolean() || activeThreads.get() > 0);
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

  private static void deleteUnpack() throws IOException {
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
  }

  private static IsoReader[] getIsoReaders() throws IOException {
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

    return readers;
  }

  private static Tuple<IsoReader, Integer> getIsoReader(final Path path) throws IOException {
    final long fileSize = Files.size(path);

    if(fileSize < (PVD_SECTOR + 1) * IsoReader.SECTOR_SIZE) {
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

  private static void populateInitialFileTree(final DirectoryEntry root, final String path, final PathNode parent, final Queue<PathNode> transformationQueue) {
    final String filename = path + root.name();

    if(!root.isDirectory()) {
      if(!Files.exists(ROOT.resolve(filename))) {
        final PathNode file = new PathNode(filename, root.name(), readFile(filename, root), parent);
        parent.addChild(file);
        transformationQueue.add(file);
      }
    } else {
      final PathNode dir;
      final String newPath;

      if(".".equals(root.name())) {
        dir = parent;
        newPath = "";
      } else {
        dir = new PathNode(filename, root.name(), null, null);
        newPath = filename + '/';
        parent.addChild(dir);
      }

      for(final DirectoryEntry entry : root.children().values()) {
        populateInitialFileTree(entry, newPath, dir, transformationQueue);
      }
    }
  }

  private static FileData readFile(final String filename, final DirectoryEntry entry) {
    synchronized(entry.reader()) {
      final byte[] fileData = entry.reader().readSectors(entry.sector(), entry.length(), filename.endsWith(".IKI") || filename.endsWith(".XA"));
      return new FileData(fileData);
    }
  }

  private static void transform(final PathNode node, final Transformations transformations, final Set<String> flags) {
    if(shouldStop) {
      throw new UnpackerStoppedRuntimeException("Unpacking cancelled");
    }

    for(final var entry : transformers.entrySet()) {
      final var discriminator = entry.getKey();
      final var transformer = entry.getValue();

      if(discriminator.matches(node, flags)) {
        node.parent.children.remove(node.pathSegment);
        transformer.transform(node, transformations, flags);
        break;
      }
    }
  }

  private static boolean decompressDiscriminator(final PathNode node, final Set<String> flags) {
    if(ROOT_MRG.matcher(node.fullPath).matches()) {
      final int dirNum = Integer.parseInt(node.fullPath.substring(15, 19));
      if(dirNum >= 4031 && dirNum < 4103) {
        return false;
      }
    }

    return node.data.size() >= 8 && node.data.readInt(0x4) == 0x1a455042;
  }

  private static void decompress(final PathNode node, final Transformations transformations, final Set<String> flags) {
    transformations.replaceNode(node, new FileData(Unpacker.decompress(node.data)));
  }

  private static boolean mrgDiscriminator(final PathNode node, final Set<String> flags) {
    return node.data.size() >= 8 && node.data.readInt(0x0) == MrgArchive.MAGIC;
  }

  private static void unmrg(final PathNode node, final Transformations transformations, final Set<String> flags) {
    final MrgArchive archive = new MrgArchive(node.data, node.pathSegment.matches("^DRGN(?:0|1|2[1234])?.BIN$"));

    if(archive.getCount() == 0) {
      transformations.addNode(node.fullPath, EMPTY_DIRECTORY_SENTINEL);
    }

    int i = 0;
    for(final MrgArchive.Entry entry : archive) {
      if(!entry.virtual()) {
        transformations.addChild(node, Integer.toString(i), node.data.slice(entry.offset(), entry.size()));
      }

      i++;
    }

    final FileMap map = new FileMap();

    i = 0;
    for(final MrgArchive.Entry entry : archive) {
      map.addFile(Integer.toString(i), Integer.toString(entry.virtual() ? entry.parent() : i), entry.virtualSize());
      i++;
    }

    transformations.addChild(node, "mrg", map.build());
  }

  private static boolean deffDiscriminator(final PathNode node, final Set<String> flags) {
    return node.data.size() >= 8 && node.data.readInt(0x0) == 0x46464544;
  }

  private static void undeff(final PathNode node, final Transformations transformations, final Set<String> flags) {
    final DeffArchive archive = new DeffArchive(node.data);

    if(archive.getCount() == 0) {
      transformations.addNode(node.fullPath, EMPTY_DIRECTORY_SENTINEL);
    }

    int i = 0;
    for(final FileData entry : archive) {
      transformations.addChild(node, Integer.toString(i), entry).flags.add("DEFF");
      i++;
    }
  }

  private static boolean engineOverlayDiscriminator(final PathNode node, final Set<String> flags) {
    return "SCUS_944.91".equals(node.pathSegment) && !flags.contains(node.pathSegment);
  }

  private static void engineOverlayExtractor(final PathNode node, final Transformations transformations, final Set<String> flags) {
    flags.add(node.pathSegment);
    transformations.addNode(node);
    transformations.addNode("lod_engine", node.data.slice(0xa00, 0x36228));
  }

  /**
   * DRGN21.402.3 is a submap object script for the screen of the Dragon's Nest
   * with the weird plant you need to use the spring water on. That script has a
   * bug which causes it to jump out of the bounds of the script into the next
   * script. This no longer works in the decomp because the files are no longer
   * adjacent in a MRG file. This patch extends the script to be long enough to
   * contain the jump and just returns.
   */
  private static boolean drgn21_402_3_patcherDiscriminator(final PathNode node, final Set<String> flags) {
    return ("SECT/DRGN21.BIN/402/3".equals(node.fullPath) || "SECT/DRGN22.BIN/402/3".equals(node.fullPath)) && node.data.size() == 0xee4;
  }

  private static void drgn21_402_3_patcher(final PathNode node, final Transformations transformations, final Set<String> flags) {
    final byte[] newData = new byte[0x107c];
    node.data.copyFrom(newData);
    newData[0x1078] = 0x49;
    transformations.replaceNode(node, new FileData(newData));
  }

  /**
   * DRGN21.693.0 is the submap controller for Hoax post-Kongol. If you select
   * "I still don't know..." as your response during the dialog, the script tries
   * to push an inline parameter that is past the end of the script. This patcher
   * extends the script and fills with ffffffff, which we read as a sentinel value
   * in {@link Scus94491BpeSegment#scriptReadGlobalFlag2}, and return 0 there.
   */
  private static boolean drgn21_693_0_patcherDiscriminator(final PathNode node, final Set<String> flags) {
    return "SECT/DRGN21.BIN/693/0".equals(node.fullPath) && node.data.size() == 0x188;
  }

  private static void drgn21_693_0_patcher(final PathNode node, final Transformations transformations, final Set<String> flags) {
    final byte[] newData = new byte[0x190];
    node.data.copyFrom(newData);
    MathHelper.set(newData, 0x188, 4, 0xffffffffL);
    MathHelper.set(newData, 0x18c, 4, 0xffffffffL);
    transformations.replaceNode(node, new FileData(newData));
  }

  /**
   * The Meru model in the post-Divine-Dragon fight cutscene has a corrupt
   * animation file, missing animations for 2 out of her 22 objects. If you
   * look closely at the top of her leg, you can see it. This injects extra
   * animation data for those missing objects made by DooMMetaL.
   */
  private static boolean drgn0_142_animPatcherDiscriminator(final PathNode node, final Set<String> flags) {
    return "SECT/DRGN0.BIN/142".equals(node.fullPath) && node.data.size() == 0x1f0;
  }

  private static void drgn0_142_animPatcher(final PathNode node, final Transformations transformations, final Set<String> flags) {
    final byte[] newData = new byte[node.data.size() + 0xc * 2 * 2]; // 2 objects, 2 keyframes, 0xc table pitch
    final byte[] frame = {0x0e, (byte)0xe0, (byte)0xfe, (byte)0xde, (byte)0xff, (byte)0x9d, 0x1d, 0x00, 0x28, 0x03, (byte)0xcb, 0x00};
    // Note: we only create data for object 21, object 22 can be all 0's since it's not visible

    node.data.copyFrom(0, newData, 0, 0x100);
    System.arraycopy(frame, 0, newData, 0x100, frame.length); // obj 21
    node.data.copyFrom(0x100, newData, 0x118, 0xf0);
    System.arraycopy(frame, 0, newData, 0x208, frame.length); // obj 21
    newData[0xc] = 22;

    transformations.replaceNode(node, new FileData(newData));
  }

  private static boolean equipmentAndXpDiscriminator(final PathNode node, final Set<String> flags) {
    return "OVL/S_ITEM.OV_".equals(node.fullPath) && !flags.contains(node.fullPath);
  }

  private static void equipmentAndXpExtractor(final PathNode node, final Transformations transformations, final Set<String> flags) {
    flags.add(node.fullPath);

    transformations.addNode(node);
    transformations.addNode("characters/kongol/xp", node.data.slice(0x17d78, 61 * 4));
    transformations.addNode("characters/dart/xp", node.data.slice(0x17e6c, 61 * 4));
    transformations.addNode("characters/haschel/xp", node.data.slice(0x17f60, 61 * 4));
    transformations.addNode("characters/meru/xp", node.data.slice(0x18054, 61 * 4));
    transformations.addNode("characters/lavitz/xp", node.data.slice(0x18148, 61 * 4));
    transformations.addNode("characters/albert/xp", node.data.slice(0x18148, 61 * 4));
    transformations.addNode("characters/rose/xp", node.data.slice(0x1823c, 61 * 4));
    transformations.addNode("characters/shana/xp", node.data.slice(0x18330, 61 * 4));
    transformations.addNode("characters/miranda/xp", node.data.slice(0x18330, 61 * 4));

    for(int i = 0; i < 192; i++) {
      transformations.addNode("equipment/%d.deqp".formatted(i), node.data.slice(0x16878 + i * 0x1c, 0x1c));
    }
  }

  private static boolean spellsDiscriminator(final PathNode node, final Set<String> flags) {
    return "OVL/BTTL.OV_".equals(node.fullPath) && !flags.contains(node.fullPath);
  }

  private static void spellsExtractor(final PathNode node, final Transformations transformations, final Set<String> flags) {
    flags.add(node.fullPath);

    transformations.addNode(node);

    for(int i = 0; i < 128; i++) {
      transformations.addNode("spells/%d.dspl".formatted(i), node.data.slice(0x33a30 + i * 0xc, 0xc));
    }
  }

  private static boolean smapAssetDiscriminator(final PathNode node, final Set<String> flags) {
    return "OVL/SMAP.OV_".equals(node.fullPath) && !flags.contains("SMAP");
  }

  private static void smapAssetExtractor(final PathNode node, final Transformations transformations, final Set<String> flags) {
    flags.add("SMAP");

    transformations.addNode(node);
    transformations.addNode("SUBMAP/savepoint", node.data.slice(0x10694, 0x904));
    transformations.addNode("SUBMAP/darker_shadow.tim", getTimSize(node.data.slice(0x100b4)));
    transformations.addNode("SUBMAP/alert.tim", getTimSize(node.data.slice(0x10214)));
    transformations.addNode("SUBMAP/big_arrow.tim", getTimSize(node.data.slice(0x10f98)));
    transformations.addNode("SUBMAP/small_arrow.tim", getTimSize(node.data.slice(0x115d8)));
    transformations.addNode("SUBMAP/savepoint.tim", getTimSize(node.data.slice(0x11858)));
    transformations.addNode("SUBMAP/800d8520.tim", getTimSize(node.data.slice(0x11e98)));
    transformations.addNode("SUBMAP/800d85e0.tim", getTimSize(node.data.slice(0x11f58)));
    transformations.addNode("SUBMAP/savepoint_big_circle.tim", getTimSize(node.data.slice(0x12098)));
    transformations.addNode("SUBMAP/dust.tim", getTimSize(node.data.slice(0x122d8)));
    transformations.addNode("SUBMAP/left_foot.tim", getTimSize(node.data.slice(0x12518)));
    transformations.addNode("SUBMAP/right_foot.tim", getTimSize(node.data.slice(0x12658)));
    transformations.addNode("SUBMAP/smoke_1.tim", getTimSize(node.data.slice(0x12798)));
    transformations.addNode("SUBMAP/smoke_2.tim", getTimSize(node.data.slice(0x129d8)));
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
  private static boolean drgn0_5546_1_patcherDiscriminator(final PathNode node, final Set<String> flags) {
    return "SECT/DRGN0.BIN/5546/1".equals(node.fullPath) && node.data.size() == 0x721c;
  }

  private static void drgn0_5546_1_patcher(final PathNode node, final Transformations transformations, final Set<String> flags) {
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

    node.data.copyFrom(0, newData, 0, 0x47c0);
    MathHelper.set(newData, 0x47c0, 4, jump);
    System.arraycopy(address1, 0, newData, 0x47c4, address1.length);
    node.data.copyFrom(0x47cd, newData, 0x47cd, 0x73f);
    MathHelper.set(newData, 0x4f0c, 4, jump);
    System.arraycopy(address2, 0, newData, 0x4f10, address2.length);
    node.data.copyFrom(0x4f15, newData, 0x4f15, 0x2307);
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
    transformations.replaceNode(node, new FileData(newData));
  }

  /**
   * The lava fish animation where it swims around the player is missing the last object
   */
  private static boolean drgn0_3667_16_animPatcherDiscriminator(final PathNode node, final Set<String> flags) {
    return "SECT/DRGN0.BIN/3667/16".equals(node.fullPath) && node.data.size() == 0x538;
  }

  private static void drgn0_3667_16_animPatcher(final PathNode node, final Transformations transformations, final Set<String> flags) {
    transformations.replaceNode(node, new FileData(patchAnimation(node.data, 11)));
  }

  /**
   * The lava fish animation where it swims around the player is missing the last object
   */
  private static boolean drgn0_3667_17_animPatcherDiscriminator(final PathNode node, final Set<String> flags) {
    return "SECT/DRGN0.BIN/3667/17".equals(node.fullPath) && node.data.size() == 0x178;
  }

  private static void drgn0_3667_17_animPatcher(final PathNode node, final Transformations transformations, final Set<String> flags) {
    transformations.replaceNode(node, new FileData(patchAnimation(node.data, 11)));
  }

  /**
   * The lava fish animation where it swims around the player is missing the last object
   */
  private static boolean drgn0_3750_16_animPatcherDiscriminator(final PathNode node, final Set<String> flags) {
    return "SECT/DRGN0.BIN/3750/16".equals(node.fullPath) && node.data.size() == 0x538;
  }

  private static void drgn0_3750_16_animPatcher(final PathNode node, final Transformations transformations, final Set<String> flags) {
    transformations.replaceNode(node, new FileData(patchAnimation(node.data, 11)));
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
  private static boolean enemyScriptDamageCapDiscriminator(final PathNode node, final Set<String> flags) {
    return node.fullPath.startsWith("SECT/DRGN1.BIN/") && !flags.contains(node.fullPath);
  }

  private static void enemyAndItemScriptDamageCapPatcher(final PathNode node, final Transformations transformations, final Set<String> flags) {
    flags.add(node.fullPath);

    for(int i = 0; i < node.data.size(); i += 4) {
      if(node.data.readUShort(i) == 9999 && node.data.readUShort(i + 0x10) == 9999) {
        node.data.writeInt(i, 999999999);
        node.data.writeInt(i + 0x10, 999999999);
        i += 0x10;
      }
    }

    transformations.addNode(node);
  }

  /**
   * Every single item script has damage caps build into it for some reason
   */
  private static boolean itemScriptDamageCapDiscriminator(final PathNode node, final Set<String> flags) {
    if(flags.contains(node.fullPath)) {
      return false;
    }

    if(ITEM_SCRIPT.matcher(node.fullPath).matches()) {
      final int fileId = Integer.parseInt(node.fullPath, 15, node.fullPath.indexOf('/', 16), 10);

      return ((fileId & 0x1) == 0) && fileId >= 4140 && fileId <= 5500;
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
  private static boolean drgn1_343_patcherDiscriminator(final PathNode node, final Set<String> flags) {
    return "SECT/DRGN1.BIN/343".equals(node.fullPath) && node.data.size() == 0x3ab4 && node.data.readByte(0x3a70) == 0x8;
  }

  private static void drgn1_343_patcher(final PathNode node, final Transformations transformations, final Set<String> flags) {
    node.data.writeByte(0x3a70, 0x3);
    transformations.addNode(node);
  }

  private static boolean playerCombatSoundEffectsDiscriminator(final PathNode node, final Set<String> flags) {
    if(DRGN0_FILE.matcher(node.fullPath).matches()) {
      final int fileId = Integer.parseInt(node.fullPath, 15, node.fullPath.indexOf('/', 16), 10);

      return fileId >= 752 && fileId <= 772;
    }

    return false;
  }

  private static void playerCombatSoundEffectsTransformer(final PathNode node, final Transformations transformations, final Set<String> flags) {
    final String end = node.fullPath.substring(node.fullPath.lastIndexOf('/') + 1);

    if(node.fullPath.startsWith("SECT/DRGN0.BIN/752/0/")) {
      transformations.addNode("characters/dart/sounds/combat/" + end, node.data);
    } else if(node.fullPath.startsWith("SECT/DRGN0.BIN/752/1/")) {
      transformations.addNode("characters/lavitz/sounds/combat/" + end, node.data);
    } else if(node.fullPath.startsWith("SECT/DRGN0.BIN/752/2/")) {
      transformations.addNode("characters/shana/sounds/combat/" + end, node.data);
    } else if(node.fullPath.startsWith("SECT/DRGN0.BIN/753/2/")) {
      transformations.addNode("characters/rose/sounds/combat/" + end, node.data);
    } else if(node.fullPath.startsWith("SECT/DRGN0.BIN/754/2/")) {
      transformations.addNode("characters/haschel/sounds/combat/" + end, node.data);
    } else if(node.fullPath.startsWith("SECT/DRGN0.BIN/755/2/")) {
      transformations.addNode("characters/meru/sounds/combat/" + end, node.data);
    } else if(node.fullPath.startsWith("SECT/DRGN0.BIN/756/2/")) {
      transformations.addNode("characters/kongol/sounds/combat/" + end, node.data);
    } else if(node.fullPath.startsWith("SECT/DRGN0.BIN/757/2/")) {
      transformations.addNode("characters/miranda/sounds/combat/" + end, node.data);
    } else if(node.fullPath.startsWith("SECT/DRGN0.BIN/772/1/")) {
      transformations.addNode("characters/albert/sounds/combat/" + end, node.data);
    }
  }

  private static boolean playerCombatModelsAndTexturesDiscriminator(final PathNode node, final Set<String> flags) {
    if(DRGN0_FILE.matcher(node.fullPath).matches()) {
      final int fileId = Integer.parseInt(node.fullPath, 15, node.fullPath.indexOf('/', 16), 10);

      return fileId >= 3993 && fileId <= 4010 && (!node.fullPath.endsWith("mrg") || fileId % 2 == 0);
    }

    return false;
  }

  private static void playerCombatModelsAndTexturesTransformer(final PathNode node, final Transformations transformations, final Set<String> flags) {
    for(int charId = 0; charId < 9; charId++) {
      final String charName = getCharacterName(charId).toLowerCase();

      if(node.fullPath.startsWith("SECT/DRGN0.BIN/%d".formatted(3993 + charId * 2))) {
        transformations.addNode("characters/%s/textures/combat".formatted(charName), node.data);
      } else if(node.fullPath.startsWith("SECT/DRGN0.BIN/%d".formatted(3994 + charId * 2))) {
        transformations.addNode("characters/%s/models/combat/%s".formatted(charName, node.fullPath.substring(node.fullPath.lastIndexOf('/') + 1)), node.data);
      }
    }
  }

  private static boolean dragoonCombatModelsAndTexturesDiscriminator(final PathNode node, final Set<String> flags) {
    if(DRGN0_FILE.matcher(node.fullPath).matches()) {
      final int fileId = Integer.parseInt(node.fullPath, 15, node.fullPath.indexOf('/', 16), 10);

      return fileId >= 4011 && fileId <= 4030 && (!node.fullPath.endsWith("mrg") || fileId % 2 == 0);
    }

    return false;
  }

  private static void dragoonCombatModelsAndTexturesTransformer(final PathNode node, final Transformations transformations, final Set<String> flags) {
    for(int charId = 0; charId < 10; charId++) {
      final String charName = getCharacterName(charId).toLowerCase();

      if(node.fullPath.startsWith("SECT/DRGN0.BIN/%d".formatted(4011 + charId * 2))) {
        transformations.addNode("characters/%s/textures/dragoon".formatted(charName), node.data);
      } else if(node.fullPath.startsWith("SECT/DRGN0.BIN/%d".formatted(4012 + charId * 2))) {
        transformations.addNode("characters/%s/models/dragoon/%s".formatted(charName, node.fullPath.substring(node.fullPath.lastIndexOf('/') + 1)), node.data);
      }
    }
  }

  private static boolean skipPartyPermutationsDiscriminator(final PathNode node, final Set<String> flags) {
    if(DRGN0_FILE.matcher(node.fullPath).matches()) {
      final int fileId = Integer.parseInt(node.fullPath, 15, node.fullPath.indexOf('/', 16), 10);

      return fileId >= 3537 && fileId <= 3592;
    }

    return false;
  }

  private static void skipPartyPermutationsTransformer(final PathNode node, final Transformations transformations, final Set<String> flags) {

  }

  /** Extracts some S_BTLD tables/scripts */
  private static boolean extractBtldDataDiscriminator(final PathNode node, final Set<String> flags) {
    return "OVL/S_BTLD.OV_".equals(node.fullPath) && !flags.contains(node.fullPath);
  }

  private static void extractBtldDataTransformer(final PathNode node, final Transformations transformations, final Set<String> flags) {
    flags.add(node.fullPath);

    transformations.addNode(node);
    transformations.addNode("encounters", node.data.slice(0x68d8, 0x7000));
    transformations.addNode("player_combat_script", node.data.slice(0x4, 0x68d4));
  }

  private static boolean playerScriptDamageCapsDiscriminator(final PathNode node, final Set<String> flags) {
    return "player_combat_script".equals(node.fullPath) && node.data.readInt(4) != MrgArchive.MAGIC && !flags.contains(node.fullPath);
  }

  private static void playerScriptDamageCapsTransformer(final PathNode node, final Transformations transformations, final Set<String> flags) {
    flags.add(node.fullPath);

    node.data.writeInt(0x9f0, 999999999);
    node.data.writeInt(0xa00, 999999999);
    node.data.writeInt(0x82b0, 999999999);
    node.data.writeInt(0x82c0, 999999999);
    node.data.writeInt(0xe190, 999999999);
    node.data.writeInt(0xe1a0, 999999999);
    transformations.addNode(node);
  }

  private static boolean uiPatcherDiscriminator(final PathNode node, final Set<String> flags) {
    return "SECT/DRGN0.BIN/6666".equals(node.fullPath) && node.data.readByte(0x24a8) != 0;
  }

  private static void uiPatcherTransformer(final PathNode node, final Transformations transformations, final Set<String> flags) {
    // Remove the baked-in slashes in the fractions for character cards
    for(int i = 0; i < 3; i++) {
      node.data.writeByte(0x24a8 + i * 0x14, 0);
      node.data.writeByte(0x24aa + i * 0x14, 0);
    }

    transformations.addNode(node);
  }

  private static boolean lodEngineDiscriminator(final PathNode node, final Set<String> flags) {
    return "lod_engine".equals(node.fullPath);
  }

  private static void lodEngineExtractor(final PathNode node, final Transformations transformations, final Set<String> flags) {
    for(int i = 0; i < 64; i++) {
      transformations.addNode("items/%d.ditm".formatted(i), node.data.slice(0x3f2ac + i * 0xc, 0xc));
    }

    transformations.addNode("shadow.ctmd", node.data.slice(0x3d0, 0x14c));
    transformations.addNode("shadow.anim", node.data.slice(0x51c, 0x28));
    transformations.addNode("shadow.tim", getTimSize(node.data.slice(0x544)));
    transformations.addNode("font.tim", getTimSize(node.data.slice(0xb6744)));
  }

  private static boolean xaDiscriminator(final PathNode node, final Set<String> flags) {
    return node.fullPath.endsWith(".XA");
  }

  private static void xaTransformer(final PathNode node, final Transformations transformations, final Set<String> flags) {
    XaTranscoder.transform(node, transformations);
  }

  private static FileData getTimSize(final FileData data) {
    final int flags = data.readInt(0x4);
    final int imageAddr;

    if((flags & 0b1000) == 0) { // No CLUT
      imageAddr = 0x14;
    } else { // Has CLUT
      imageAddr = 0x8 + data.readInt(0x8);
    }

    return data.slice(0x0, imageAddr + data.readUShort(imageAddr));
  }

  private static void writeFile(final PathNode node) {
    if(node.data.realFileIndex() != -1) {
      return;
    }

    final Path path = ROOT.resolve(node.fullPath);

    try {
      if(node.data == EMPTY_DIRECTORY_SENTINEL) {
        Files.createDirectories(path);
        return;
      }

      Files.createDirectories(path.getParent());

      try(final OutputStream writer = Files.newOutputStream(path, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
        writer.write(node.data.data(), node.data.offset(), node.data.size());
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

//    LOGGER.info("Decompressing BPE segment");

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

//    LOGGER.info("Archive size: %d, decompressed size: %d", archiveOffset, totalSize);

    return dest;
  }

  @FunctionalInterface
  public interface Discriminator {
    boolean matches(final PathNode node, final Set<String> flags);
  }

  @FunctionalInterface
  public interface Transformer {
    void transform(final PathNode node, final Transformations transformations, final Set<String> flags);
  }
}
