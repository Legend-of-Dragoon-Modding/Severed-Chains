package legend.game.unpacker;

import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import legend.core.Async;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.StreamSupport;

public final class Loader {
  private Loader() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(Loader.class);

  private static final Pattern MRG_ENTRY = Pattern.compile("[=;]");

  private static final AtomicInteger LOADING_COUNT = new AtomicInteger();

  public static Path resolve(final String name) {
    return Unpacker.ROOT.resolve(fixPath(name));
  }

  public static Path resolve(final Path name) {
    return Unpacker.ROOT.resolve(name);
  }

  public static FileData loadFileSync(final Path path) {
    LOGGER.info("Loading file %s", path);

    try {
      return new FileData(Files.readAllBytes(path));
    } catch(final IOException e) {
      throw new RuntimeException("Failed to load file " + path, e);
    }
  }

  public static FileData loadFileSync(final String name) {
    return loadFileSync(Unpacker.ROOT.resolve(fixPath(name)));
  }

  public static CompletableFuture<FileData> loadFile(final Path path) {
    final int total = LOADING_COUNT.incrementAndGet();
    final StackWalker.StackFrame frame = DebugHelper.getCallerFrame();
    LOGGER.info("Queueing file %s (total queued: %d) from %s.%s(%s:%d)", path, total, frame.getClassName(), frame.getMethodName(), frame.getFileName(), frame.getLineNumber());

    return Async
      .run(() -> loadFileSync(path))
      .exceptionally(t -> onFileLoadingException(t, path.toString()))
      .whenComplete((result, exception) -> {
        final int remaining = LOADING_COUNT.decrementAndGet();
        LOGGER.info("File %s loaded (remaining queued: %d)", path, remaining);
      })
    ;
  }

  public static CompletableFuture<FileData> loadFile(final String name) {
    final int total = LOADING_COUNT.incrementAndGet();
    LOGGER.info("Queueing file %s (total queued: %d)", name, total);

    return Async
      .run(() -> loadFileSync(name))
      .exceptionally(t -> onFileLoadingException(t, name))
      .whenComplete((result, exception) -> {
        final int remaining = LOADING_COUNT.decrementAndGet();
        LOGGER.info("File %s loaded (remaining queued: %d)", name, remaining);
      })
    ;
  }

  public static CompletableFuture<List<FileData>> loadFiles(final String... files) {
    final int total = LOADING_COUNT.updateAndGet(i -> i + files.length);
    LOGGER.info("Queueing files %s (total queued: %d)", Arrays.toString(files), total);

    final FileData[] data = new FileData[files.length];
    final CompletableFuture<FileData>[] futures = new CompletableFuture[files.length];
    for(int i = 0; i < files.length; i++) {
      final int finalI = i;
      futures[i] = Async.run(() -> data[finalI] = loadFileSync(files[finalI]));
    }

    return CompletableFuture.allOf(futures)
      .thenApply(v -> List.of(data))
      .exceptionally(t -> onFileLoadingException(t, Arrays.toString(files)))
      .whenComplete((result, exception) -> {
        final int remaining = LOADING_COUNT.updateAndGet(i -> i - files.length);
        LOGGER.info("Files %s loaded (remaining queued: %d)", Arrays.toString(files), remaining);
      })
    ;
  }

  public static CompletableFuture<List<FileData>> loadFiles(final Path... files) {
    final int total = LOADING_COUNT.updateAndGet(i -> i + files.length);
    LOGGER.info("Queueing files %s (total queued: %d)", Arrays.toString(files), total);

    final FileData[] data = new FileData[files.length];
    final CompletableFuture<FileData>[] futures = new CompletableFuture[files.length];
    for(int i = 0; i < files.length; i++) {
      final int finalI = i;
      futures[i] = Async.run(() -> data[finalI] = loadFileSync(files[finalI]));
    }

    return CompletableFuture.allOf(futures)
      .thenApply(v -> List.of(data))
      .exceptionally(t -> onFileLoadingException(t, Arrays.toString(files)))
      .whenComplete((result, exception) -> {
        final int remaining = LOADING_COUNT.updateAndGet(i -> i - files.length);
        LOGGER.info("Files %s loaded (remaining queued: %d)", Arrays.toString(files), remaining);
      })
    ;
  }

  public static CompletableFuture<List<FileData>> loadDirectory(final String name) {
    final int total = LOADING_COUNT.incrementAndGet();
    LOGGER.info("Queueing directory %s (total queued: %d)", name, total);

    return Async
      .run(() -> loadDirectorySync(name))
      .exceptionally(t -> onFileLoadingException(t, name))
      .whenComplete((result, exception) -> {
        final int remaining = LOADING_COUNT.decrementAndGet();
        LOGGER.info("Directory %s loaded (remaining queued: %d)", name, remaining);
      })
    ;
  }

  public static CompletableFuture<List<FileData>> loadDirectory(final Path dir) {
    final int total = LOADING_COUNT.incrementAndGet();

    final StackWalker.StackFrame frame = DebugHelper.getCallerFrame();
    LOGGER.info("Queueing directory %s (total queued: %d) from %s.%s(%s:%d)", dir, total, frame.getClassName(), frame.getMethodName(), frame.getFileName(), frame.getLineNumber());

    return Async.run(() -> loadDirectorySync(dir))
      .exceptionally(t -> onFileLoadingException(t, dir.toString()))
      .whenComplete((result, exception) -> {
        final int remaining = LOADING_COUNT.decrementAndGet();
        LOGGER.info("Directory %s loaded (remaining queued: %d)", dir, remaining);
      })
    ;
  }

  private static <T> T onFileLoadingException(final Throwable t, final String path) {
    LOGGER.error("Failed to load " + path, t);
    return null;
  }

  public static List<FileData> loadDirectorySync(final String name) {
    return loadDirectorySync(Unpacker.ROOT.resolve(fixPath(name)));
  }

  public static List<FileData> loadDirectorySync(final Path dir) {
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

    final Path ref = dir.resolve("ref");

    if(Files.exists(ref)) {
      return processRef(dir, ref);
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

  private static final Path FILES = Path.of(".", "files").normalize();

  private static List<FileData> processRef(final Path dir, final Path ref) {
    try(final BufferedReader reader = Files.newBufferedReader(ref)) {

      final Int2ObjectMap<FileData> realFiles = new Int2ObjectArrayMap<>();
      final Int2IntMap virtualFiles = new Int2IntArrayMap();

      reader.lines().forEach(line -> {
        final String[] parts = MRG_ENTRY.split(line);

        if(parts.length != 3) {
          throw new RuntimeException("Invalid REF entry! " + line);
        }

        final int currentIndex = Integer.parseInt(parts[0]);

        final String rawPath = parts[1];

        if(rawPath.isBlank()) {
          return;
        }

        if(rawPath.startsWith("@")) {
          virtualFiles.put(currentIndex, Integer.parseInt(rawPath.substring(1)));
          return;
        }

        final Path resolved = rawPath.startsWith("/")
          ? FILES.resolve(rawPath.substring(1))
          : dir.resolve(rawPath);

        final Path normalized = resolved.normalize();

        if(!normalized.startsWith(FILES)) {
          throw new RuntimeException("REF path escapes files directory! " + rawPath);
        }

        try {
          if(Files.isRegularFile(normalized)) {
            realFiles.put(currentIndex, new FileData(Files.readAllBytes(normalized)));
          } else if (Files.isDirectory(normalized)) {
            realFiles.put(currentIndex, new FileData(new byte[0]));
          } else {
            throw new RuntimeException("REF entry is not a file or directory! " + normalized);
          }
        } catch(final IOException e) {
          throw new RuntimeException("Failed to load REF file " + normalized, e);
        }
      });

      int maxKey = -1;

      for(final int key : realFiles.keySet()) {
        maxKey = Math.max(maxKey, key);
      }

      for(final int key : virtualFiles.keySet()) {
        maxKey = Math.max(maxKey, key);
      }

      final FileData[] files = new FileData[maxKey + 1];

      for(int i = 0; i < files.length; i++) {
        if(realFiles.containsKey(i)) {
          files[i] = realFiles.get(i);

        } else if (virtualFiles.containsKey(i)){
          final int fileIndex = virtualFiles.get(i);

          files[i] = FileData.virtual(realFiles.get(fileIndex), 0, fileIndex);
        }
      }

      return Arrays.stream(files).toList();
    } catch(final IOException e) {
      throw new RuntimeException("Failed to load REF directory " + dir, e);
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

    if(name.startsWith("\\") || name.startsWith("/")) {
      name = name.substring(1);
    }

    return name.replace('\\', '/');
  }
}
