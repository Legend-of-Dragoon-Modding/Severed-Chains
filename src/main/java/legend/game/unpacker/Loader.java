package legend.game.unpacker;

import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import legend.core.DebugHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.StreamSupport;

public final class Loader {
  private Loader() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(Loader.class);

  private static final Pattern MRG_ENTRY = Pattern.compile("[=;]");

  private static final int AVAILABLE_PROCESSORS = Runtime.getRuntime().availableProcessors();
  private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(AVAILABLE_PROCESSORS);
  private static final AtomicInteger LOADING_COUNT = new AtomicInteger();

  public static Path resolve(final String name) {
    return Unpacker.ROOT.resolve(fixPath(name));
  }

  public static FileData loadFile(final Path path) {
    LOGGER.info("Loading file %s", path);

    try {
      return new FileData(Files.readAllBytes(path));
    } catch(final IOException e) {
      throw new RuntimeException("Failed to load file " + path, e);
    }
  }

  public static FileData loadFile(final String name) {
    return loadFile(Unpacker.ROOT.resolve(fixPath(name)));
  }

  public static void loadFile(final Path path, final Consumer<FileData> onCompletion) {
    final int total = LOADING_COUNT.incrementAndGet();
    final StackWalker.StackFrame frame = DebugHelper.getCallerFrame();
    LOGGER.info("Queueing file %s (total queued: %d) from %s.%s(%s:%d)", path, total, frame.getClassName(), frame.getMethodName(), frame.getFileName(), frame.getLineNumber());

    EXECUTOR.execute(() -> {
      onCompletion.accept(loadFile(path));
      final int remaining = LOADING_COUNT.decrementAndGet();
      LOGGER.info("File %s loaded (remaining queued: %d)", path, remaining);
    });
  }

  public static void loadFile(final String name, final Consumer<FileData> onCompletion) {
    final int total = LOADING_COUNT.incrementAndGet();
    LOGGER.info("Queueing file %s (total queued: %d)", name, total);
    EXECUTOR.execute(() -> {
      onCompletion.accept(loadFile(name));
      final int remaining = LOADING_COUNT.decrementAndGet();
      LOGGER.info("File %s loaded (remaining queued: %d)", name, remaining);
    });
  }

  public static void loadFiles(final Consumer<List<FileData>> onCompletion, final String... files) {
    final int total = LOADING_COUNT.updateAndGet(i -> i + files.length);
    LOGGER.info("Queueing files %s (total queued: %d)", Arrays.toString(files), total);

    EXECUTOR.execute(() -> {
      final List<FileData> fileData = new ArrayList<>();
      for(final String file : files) {
        final FileData data = loadFile(file);
        fileData.add(data);
      }

      onCompletion.accept(fileData);
      final int remaining = LOADING_COUNT.updateAndGet(i -> i - files.length);
      LOGGER.info("Files %s loaded (remaining queued: %d)", Arrays.toString(files), remaining);
    });
  }

  public static void loadDirectory(final String name, final Consumer<List<FileData>> onCompletion) {
    final int total = LOADING_COUNT.incrementAndGet();
    LOGGER.info("Queueing directory %s (total queued: %d)", name, total);
    EXECUTOR.execute(() -> {
      onCompletion.accept(loadDirectory(name));
      final int remaining = LOADING_COUNT.decrementAndGet();
      LOGGER.info("Directory %s loaded (remaining queued: %d)", name, remaining);
    });
  }

  public static void loadDirectory(final Path dir, final Consumer<List<FileData>> onCompletion) {
    final int total = LOADING_COUNT.incrementAndGet();

    final StackWalker.StackFrame frame = DebugHelper.getCallerFrame();
    LOGGER.info("Queueing directory %s (total queued: %d) from %s.%s(%s:%d)", dir, total, frame.getClassName(), frame.getMethodName(), frame.getFileName(), frame.getLineNumber());

    EXECUTOR.execute(() -> {
      onCompletion.accept(loadDirectory(dir));
      final int remaining = LOADING_COUNT.decrementAndGet();
      LOGGER.info("Directory %s loaded (remaining queued: %d)", dir, remaining);
    });
  }

  public static List<FileData> loadDirectory(final String name) {
    return loadDirectory(Unpacker.ROOT.resolve(fixPath(name)));
  }

  public static List<FileData> loadDirectory(final Path dir) {
    LOGGER.info("Loading directory %s", dir);

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
            throw new RuntimeException("Failed to load directory " + dir, e);
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
        throw new RuntimeException("Failed to load directory " + dir, e);
      }
    }

    try(final DirectoryStream<Path> ds = Files.newDirectoryStream(dir)) {
      final List<FileData> files = new ArrayList<>();

      StreamSupport.stream(ds.spliterator(), false)
        .filter(Files::isRegularFile)
        .sorted((path1, path2) -> {
          final String filename1 = path1.getFileName().toString();
          final String filename2 = path2.getFileName().toString();

          try {
            return Integer.compare(Integer.parseInt(filename1), Integer.parseInt(filename2));
          } catch(final NumberFormatException ignored) {
          }

          return String.CASE_INSENSITIVE_ORDER.compare(filename1, filename2);
        })
        .forEach(child -> {
          try {
            files.add(new FileData(Files.readAllBytes(child)));
          } catch(final IOException e) {
            throw new RuntimeException("Failed to load directory " + dir, e);
          }
        });

      return files;
    } catch(final IOException e) {
      throw new RuntimeException("Failed to load directory " + dir, e);
    }
  }

  public static int getLoadingFileCount() {
    return LOADING_COUNT.get();
  }

  public static boolean exists(final String name) {
    return Files.exists(Unpacker.ROOT.resolve(fixPath(name)));
  }

  public static boolean isDirectory(final String name) {
    return Files.isDirectory(Unpacker.ROOT.resolve(fixPath(name)));
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

  public static void shutdownLoader() {
    EXECUTOR.shutdown();
  }
}
