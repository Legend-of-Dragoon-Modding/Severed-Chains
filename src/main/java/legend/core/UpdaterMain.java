package legend.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Standalone updater entry point. Designed to be packaged as a separate JAR
 * ({@code updater.jar}) and launched by the main game process when the user
 * chooses "Auto-Update".
 *
 * <p>Usage: {@code java -jar updater.jar <downloadUrl> <gameDir> [parentPid] [currentVersion]}
 *
 * <p>The updater:
 * <ol>
 *   <li>Waits for the parent game process (if pid provided) to exit</li>
 *   <li>Downloads the release archive</li>
 *   <li>Extracts it to a staging directory</li>
 *   <li>Copies non-preserved files over the game directory</li>
 *   <li>Cleans up staging/temp files</li>
 * </ol>
 *
 * <p>This is a single cross-platform JAR no platform-specific scripts needed.
 */
public final class UpdaterMain {
  private UpdaterMain() { }

  /** Directories and files that should never be overwritten by an update */
  private static final Set<String> PRESERVED = Set.of(
    "saves",
    "mods",
    "isos",
    "files",
    "jdk25",
    "config.dcnf",
    "launch.conf",
    "updater.jar"
  );

  private static final DateTimeFormatter TIMESTAMP_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  public static void main(final String[] args) {
    if(args.length < 2) {
      System.err.println("Usage: java -jar updater.jar <downloadUrl> <gameDir> [parentPid] [currentVersion]");
      System.exit(1);
    }

    final String downloadUrl = args[0];
    final Path gameDir = Path.of(args[1]).toAbsolutePath().normalize();

    long parentPid = -1;
    if(args.length >= 3) {
      try {
        parentPid = Long.parseLong(args[2]);
      } catch(final NumberFormatException e) {
        System.err.println("[Updater] Warning: could not parse parent PID '" + args[2] + "', proceeding without waiting.");
      }
    }

    String currentVersion;
    try {
      currentVersion = args.length >= 4 ? args[3] : detectCurrentVersion(gameDir);
    } catch(final Exception e) {
      currentVersion = "unknown";
    }

    // tracking lists for the update log
    final List<String> addedFiles = new ArrayList<>();
    final List<String> modifiedFiles = new ArrayList<>();
    final List<String> cleanedUpFiles = new ArrayList<>();

    try {
      // step 0: Wait for the parent game process to exit ---
      if(parentPid > 0) {
        System.out.println("[Updater] Waiting for game process (PID " + parentPid + ") to exit...");
        waitForProcessExit(parentPid, 60_000);
      } else {
        // give a brief pause for the launching process to close
        Thread.sleep(2000);
      }

      final Path staging = gameDir.resolve(".update-staging");
      final Path archivePath = gameDir.resolve(".update-download.tmp");

      // clean up any previous failed update
      deleteRecursive(staging);
      Files.deleteIfExists(archivePath);
      Files.createDirectories(staging);

      System.out.println("[Updater] Downloading update from " + downloadUrl);
      downloadFile(downloadUrl, archivePath);

      // extract ---
      System.out.println("[Updater] Extracting update archive...");
      final String lowerUrl = downloadUrl.toLowerCase(Locale.US);
      if(lowerUrl.endsWith(".zip")) {
        extractZip(archivePath, staging);
      } else if(lowerUrl.endsWith(".tar.gz") || lowerUrl.endsWith(".tgz")) {
        extractTarGz(archivePath, staging);
      } else {
        try {
          extractZip(archivePath, staging);
        } catch(final Exception e) {
          deleteRecursive(staging);
          Files.createDirectories(staging);
          extractTarGz(archivePath, staging);
        }
      }

      //collect new release file inventory for cleanup
      final Path sourceDir = findActualRoot(staging);
      final Set<String> newLibsFiles = collectFilenames(sourceDir.resolve("libs"));
      final Set<String> newRootJars = collectRootJars(sourceDir);

      //apply copy 
      System.out.println("[Updater] Applying update files...");
      applyFiles(staging, gameDir, addedFiles, modifiedFiles);

      // clean up orphaned files
      System.out.println("[Updater] Cleaning up orphaned files...");
      cleanupOrphanedFiles(gameDir.resolve("libs"), newLibsFiles, cleanedUpFiles);
      cleanupOrphanedRootJars(gameDir, newRootJars, cleanedUpFiles);

      // clean up staging
      deleteRecursive(staging);
      Files.deleteIfExists(archivePath);

      System.out.println("[Updater] Update applied successfully!");

      //Write update log
      try {
        final String newVersion = extractVersionFromUrl(downloadUrl);
        writeUpdateLog(gameDir, currentVersion, newVersion, addedFiles, modifiedFiles, cleanedUpFiles);
      } catch(final Exception logEx) {
        System.err.println("[Updater] Warning: could not write update log: " + logEx.getMessage());
      }

      System.out.println("[Updater] Update complete. Please restart Severed Chains.");

    } catch(final Exception e) {
      System.err.println("[Updater] Update failed: " + e.getMessage());
      e.printStackTrace();
      // Write a failure entry to the log
      try {
        final String newVersion = extractVersionFromUrl(downloadUrl);
        writeFailureLog(gameDir, currentVersion, newVersion, e);
      } catch(final Exception logEx) {
        System.err.println("[Updater] Could not write update log: " + logEx.getMessage());
      }
      System.err.println("[Updater] Press Enter to exit...");
      try { System.in.read(); } catch(final IOException ignored) { }
      System.exit(1);
    }
  }

  //  Version detection 

  /**
   * this extracts the release tag from a gitHub download url.
   *example: ".../download/devbuild-2026-02-08-06-33-05/Severed_Chains_Windows.zip" → "devbuild-2026-02-08-06-33-05"
   */
  private static String extractVersionFromUrl(final String url) {
    try {
      // URL pattern: .../download/{tag}/{filename}
      final String path = URI.create(url).getPath();
      final String[] segments = path.split("/");
      for(int i = 0; i < segments.length - 1; i++) {
        if("download".equals(segments[i])) {
          return segments[i + 1];
        }
      }
    } catch(final Exception ignored) { }
    return "unknown";
  }

  /**
   * Gets current build version from the existing game JAR filename.
   * example "lod-game-0d87016.jar" → "0d87016"
   */
  private static String detectCurrentVersion(final Path gameDir) {
    try(final DirectoryStream<Path> stream = Files.newDirectoryStream(gameDir, "lod-game-*.jar")) {
      for(final Path entry : stream) {
        final String name = entry.getFileName().toString();
        // Strip "lod-game-" prefix and ".jar" suffix
        return name.substring(9, name.length() - 4);
      }
    } catch(final Exception ignored) { }
    return "unknown";
  }

  //Update log 

  /**
   * Writes a structured update log to update_log.txt in the game directory.
   * Appends to the file so previous update history is preserved.
   */
  private static void writeUpdateLog(
    final Path gameDir,
    final String oldVersion,
    final String newVersion,
    final List<String> addedFiles,
    final List<String> modifiedFiles,
    final List<String> cleanedUpFiles
  ) {
    final Path logFile = gameDir.resolve("update_log.txt");
    try(final PrintWriter pw = new PrintWriter(Files.newBufferedWriter(logFile,
        StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {

      pw.println("==================================================");
      pw.println(LocalDateTime.now().format(TIMESTAMP_FMT));
      pw.println("Update: " + oldVersion + " -> " + newVersion);
      pw.println("==================================================");

      if(!addedFiles.isEmpty()) {
        pw.println();
        pw.println("Added files (" + addedFiles.size() + "):");
        for(final String f : addedFiles) {
          pw.println("  + " + f);
        }
      }

      if(!modifiedFiles.isEmpty()) {
        pw.println();
        pw.println("Modified files (" + modifiedFiles.size() + "):");
        for(final String f : modifiedFiles) {
          pw.println("  ~ " + f);
        }
      }

      if(!cleanedUpFiles.isEmpty()) {
        pw.println();
        pw.println("Cleaned up (" + cleanedUpFiles.size() + "):");
        for(final String f : cleanedUpFiles) {
          pw.println("  - " + f);
        }
      }

      pw.println();
      pw.println("Total: " + addedFiles.size() + " added, " + modifiedFiles.size() + " modified, " + cleanedUpFiles.size() + " removed");
      pw.println("Completed: " + LocalDateTime.now().format(TIMESTAMP_FMT));
      pw.println();

      System.out.println("[Updater] Update log written to " + logFile);
    } catch(final IOException e) {
      System.err.println("[Updater] Warning: could not write update log: " + e.getMessage());
    }
  }

  /**
   * writes a failure entry to the update log.
   */
  private static void writeFailureLog(final Path gameDir, final String oldVersion, final String newVersion, final Exception error) {
    final Path logFile = gameDir.resolve("update_log.txt");
    try(final PrintWriter pw = new PrintWriter(Files.newBufferedWriter(logFile,
        StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {

      pw.println("==================================================");
      pw.println(LocalDateTime.now().format(TIMESTAMP_FMT));
      pw.println("FAILED Update: " + oldVersion + " -> " + newVersion);
      pw.println("==================================================");
      pw.println("Error: " + error.getMessage());
      pw.println();

    } catch(final IOException e) {
      System.err.println("[Updater] Warning: could not write failure log: " + e.getMessage());
    }
  }

  //  process waiting 

  /**
   * Waits for a process with the given PID to exit polling every 500ms.
   * Times out after {@code timeoutMs} milliseconds.
   */
  private static void waitForProcessExit(final long pid, final long timeoutMs) throws InterruptedException {
    final long deadline = System.currentTimeMillis() + timeoutMs;

    while(System.currentTimeMillis() < deadline) {
      final var optProcess = ProcessHandle.of(pid);
      if(optProcess.isEmpty() || !optProcess.get().isAlive()) {
        System.out.println("[Updater] Game process has exited.");
        // Brief extra pause to let file handles release
        Thread.sleep(1000);
        return;
      }
      Thread.sleep(500);
    }

    System.out.println("[Updater] Timed out waiting for game to exit, proceeding anyway...");
  }

  //download 

  private static void downloadFile(final String url, final Path destination) throws IOException, InterruptedException {
    try(final HttpClient client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.ALWAYS).build()) {
      // Try to get total size via HEAD
      long totalSize = -1;
      try {
        final HttpRequest headRequest = HttpRequest.newBuilder()
          .uri(URI.create(url))
          .method("HEAD", HttpRequest.BodyPublishers.noBody())
          .build();
        final HttpResponse<Void> headResponse = client.send(headRequest, HttpResponse.BodyHandlers.discarding());
        totalSize = headResponse.headers().firstValueAsLong("Content-Length").orElse(-1);
      } catch(final Exception ignored) { }

      final HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .GET()
        .build();

      final HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

      if(response.statusCode() / 100 != 2) {
        throw new IOException("Download failed with HTTP " + response.statusCode());
      }

      if(totalSize <= 0) {
        totalSize = response.headers().firstValueAsLong("Content-Length").orElse(-1);
      }

      try(final InputStream in = response.body();
          final var out = Files.newOutputStream(destination)) {
        final byte[] buffer = new byte[65536];
        long downloaded = 0;
        int bytesRead;

        while((bytesRead = in.read(buffer)) != -1) {
          out.write(buffer, 0, bytesRead);
          downloaded += bytesRead;

          if(totalSize > 0) {
            final long pct = downloaded * 100 / totalSize;
            System.out.printf("\r[Updater] Downloading... %d%% (%d / %d MB)", pct, downloaded / 1_048_576, totalSize / 1_048_576);
          }
        }
        System.out.println();
      }

      System.out.println("[Updater] Download complete.");
    }
  }

  //  Extraction 

  private static void extractZip(final Path zipPath, final Path destDir) throws IOException {
    try(final ZipInputStream zis = new ZipInputStream(Files.newInputStream(zipPath))) {
      ZipEntry entry;
      while((entry = zis.getNextEntry()) != null) {
        final Path resolved = destDir.resolve(entry.getName()).normalize();

        if(!resolved.startsWith(destDir)) {
          throw new IOException("Bad zip entry: " + entry.getName());
        }

        if(entry.isDirectory()) {
          Files.createDirectories(resolved);
        } else {
          Files.createDirectories(resolved.getParent());
          Files.copy(zis, resolved, StandardCopyOption.REPLACE_EXISTING);
        }

        zis.closeEntry();
      }
    }
  }

  private static void extractTarGz(final Path archivePath, final Path destDir) throws IOException {
    try(final InputStream fis = Files.newInputStream(archivePath);
        final GZIPInputStream gis = new GZIPInputStream(fis)) {

      final byte[] header = new byte[512];
      while(true) {
        int totalRead = 0;
        while(totalRead < 512) {
          final int bytesRead = gis.read(header, totalRead, 512 - totalRead);
          if(bytesRead == -1) {
            return;
          }
          totalRead += bytesRead;
        }

        // check for end of file 
        boolean allZeros = true;
        for(final byte b : header) {
          if(b != 0) {
            allZeros = false;
            break;
          }
        }
        if(allZeros) {
          return;
        }

        // parse tar header
        final String name = parseTarString(header, 0, 100);
        final byte typeFlag = header[156];
        final long size = parseTarOctal(header, 124, 12);

        // check for gnu long name extension
        String fullName = name;
        if(typeFlag == 'L') {
          final byte[] nameData = new byte[(int)size];
          int nameRead = 0;
          while(nameRead < size) {
            final int bytesRead = gis.read(nameData, nameRead, (int)(size - nameRead));
            if(bytesRead == -1) {
              break;
            }
            nameRead += bytesRead;
          }
          fullName = new String(nameData, 0, nameRead).trim();

          final long remainder = size % 512;
          if(remainder != 0) {
            gis.skipNBytes(512 - remainder);
          }

          totalRead = 0;
          while(totalRead < 512) {
            final int bytesRead = gis.read(header, totalRead, 512 - totalRead);
            if(bytesRead == -1) {
              return;
            }
            totalRead += bytesRead;
          }
        }

        final byte realTypeFlag = typeFlag == 'L' ? header[156] : typeFlag;
        final long realSize = typeFlag == 'L' ? parseTarOctal(header, 124, 12) : size;

        final Path resolved = destDir.resolve(fullName).normalize();

        if(!resolved.startsWith(destDir)) {
          if(realSize > 0) {
            skipTarData(gis, realSize);
          }
          continue;
        }

        if(realTypeFlag == '5' || fullName.endsWith("/")) {
          Files.createDirectories(resolved);
        } else if(realTypeFlag == '0' || realTypeFlag == 0) {
          Files.createDirectories(resolved.getParent());
          try(final var out = Files.newOutputStream(resolved)) {
            long remaining = realSize;
            final byte[] buffer = new byte[65536];
            while(remaining > 0) {
              final int toRead = (int)Math.min(buffer.length, remaining);
              final int bytesRead = gis.read(buffer, 0, toRead);
              if(bytesRead == -1) {
                break;
              }
              out.write(buffer, 0, bytesRead);
              remaining -= bytesRead;
            }
          }

          final long remainder = realSize % 512;
          if(remainder != 0) {
            gis.skipNBytes(512 - remainder);
          }

          // restore execute permission for shell scripts
          if(fullName.endsWith(".sh") || fullName.equals("launch") || fullName.equals("download-java")) {
            resolved.toFile().setExecutable(true);
          }
        } else {
          if(realSize > 0) {
            skipTarData(gis, realSize);
          }
        }
      }
    }
  }

  private static void skipTarData(final InputStream in, final long size) throws IOException {
    final long blocks = (size + 511) / 512;
    in.skipNBytes(blocks * 512);
  }

  private static String parseTarString(final byte[] buf, final int offset, final int len) {
    int end = offset;
    while(end < offset + len && buf[end] != 0) {
      end++;
    }
    return new String(buf, offset, end - offset);
  }

  private static long parseTarOctal(final byte[] buf, final int offset, final int len) {
    long result = 0;
    for(int i = offset; i < offset + len; i++) {
      final byte b = buf[i];
      if(b == 0 || b == ' ') {
        continue;
      }
      result = result * 8 + (b - '0');
    }
    return result;
  }

  // Apply

  /**
   * copies files from the staging directory to the game directory
   * skipping preserved user directories/files.
   */
  private static void applyFiles(final Path staging, final Path gameDir, final List<String> addedFiles, final List<String> modifiedFiles) throws IOException {
    final Path sourceDir = findActualRoot(staging);

    Files.walkFileTree(sourceDir, new SimpleFileVisitor<>() {
      @Override
      public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
        final Path relative = sourceDir.relativize(dir);
        final String topLevel = relative.getNameCount() > 0 ? relative.getName(0).toString().toLowerCase(Locale.US) : "";

        if(PRESERVED.contains(topLevel)) {
          return FileVisitResult.SKIP_SUBTREE;
        }

        final Path target = gameDir.resolve(relative);
        Files.createDirectories(target);
        return FileVisitResult.CONTINUE;
      }

      @Override
      public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
        final Path relative = sourceDir.relativize(file);
        final String topLevel = relative.getNameCount() > 0 ? relative.getName(0).toString().toLowerCase(Locale.US) : relative.toString().toLowerCase(Locale.US);

        if(PRESERVED.contains(topLevel)) {
          return FileVisitResult.CONTINUE;
        }

        final Path target = gameDir.resolve(relative);
        boolean existed = false;
        try {
          existed = Files.exists(target);
        } catch(final Exception ignored) { }

        Files.createDirectories(target.getParent());
        Files.copy(file, target, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);

        // tracking for update log never interrupts the copy
        try {
          final String relStr = relative.toString();
          if(existed) {
            modifiedFiles.add(relStr);
            System.out.println("[Updater] Modified: " + relStr);
          } else {
            addedFiles.add(relStr);
            System.out.println("[Updater] Added: " + relStr);
          }
        } catch(final Exception ignored) { }

        return FileVisitResult.CONTINUE;
      }
    });
  }

  private static Path findActualRoot(final Path staging) throws IOException {
    Path singleChild = null;
    int count = 0;

    try(final DirectoryStream<Path> stream = Files.newDirectoryStream(staging)) {
      for(final Path entry : stream) {
        count++;
        singleChild = entry;
        if(count > 1) {
          break;
        }
      }
    }

    if(count == 1 && singleChild != null && Files.isDirectory(singleChild)) {
      return singleChild;
    }

    return staging;
  }

  /**
   * collects all filenames in a directory
   * returns an empty set if the directory does not exist
   */
  private static Set<String> collectFilenames(final Path dir) throws IOException {
    final Set<String> names = new HashSet<>();
    if(!Files.isDirectory(dir)) {
      return names;
    }
    try(final DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
      for(final Path entry : stream) {
        if(Files.isRegularFile(entry)) {
          names.add(entry.getFileName().toString());
        }
      }
    }
    return names;
  }

  /**
   * collects root level jar filenames matching the game jar pattern (lod-game-*.jar)
   * and script recompiler jars from the new release.
   */
  private static Set<String> collectRootJars(final Path dir) throws IOException {
    final Set<String> jars = new HashSet<>();
    if(!Files.isDirectory(dir)) {
      return jars;
    }
    try(final DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.jar")) {
      for(final Path entry : stream) {
        if(Files.isRegularFile(entry)) {
          jars.add(entry.getFileName().toString());
        }
      }
    }
    return jars;
  }

  /**
   * removes files from a game directory folder that are NOT in the new release
   * this cleans up orphaned JARs (e.g. old LWJGL versions) after an update
   */
  private static void cleanupOrphanedFiles(final Path dir, final Set<String> newFiles, final List<String> cleanedUpFiles) {
    if(!Files.isDirectory(dir) || newFiles.isEmpty()) {
      return;
    }
    try(final DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
      for(final Path entry : stream) {
        try {
          if(Files.isRegularFile(entry) && !newFiles.contains(entry.getFileName().toString())) {
            final String relPath = "libs/" + entry.getFileName();
            System.out.println("[Updater] Removing orphaned: " + relPath);
            Files.delete(entry);
            cleanedUpFiles.add(relPath);
          }
        } catch(final Exception e) {
          System.err.println("[Updater] Warning: could not remove orphaned file " + entry.getFileName() + ": " + e.getMessage());
        }
      }
    } catch(final IOException e) {
      System.err.println("[Updater] Warning: could not scan libs/ for cleanup: " + e.getMessage());
    }
  }

  /**
   * removes root-level JAR files from the game directory that are NOT in the new release
   * this handles old game JARs (lod-game-*.jar) and old script recompiler JARs
   */
  private static void cleanupOrphanedRootJars(final Path gameDir, final Set<String> newJars, final List<String> cleanedUpFiles) {
    if(newJars.isEmpty()) {
      return;
    }
    try(final DirectoryStream<Path> stream = Files.newDirectoryStream(gameDir, "*.jar")) {
      for(final Path entry : stream) {
        try {
          final String name = entry.getFileName().toString();
          // Only clean up game-related JARs, not the updater itself
          if(Files.isRegularFile(entry) && !newJars.contains(name) && !"updater.jar".equals(name)) {
            System.out.println("[Updater] Removing orphaned: " + name);
            Files.delete(entry);
            cleanedUpFiles.add(name);
          }
        } catch(final Exception e) {
          System.err.println("[Updater] Warning: could not remove orphaned JAR " + entry.getFileName() + ": " + e.getMessage());
        }
      }
    } catch(final IOException e) {
      System.err.println("[Updater] Warning: could not scan for orphaned JARs: " + e.getMessage());
    }
  }

  // Utility 

  private static void deleteRecursive(final Path path) throws IOException {
    if(!Files.exists(path)) {
      return;
    }

    Files.walkFileTree(path, new SimpleFileVisitor<>() {
      @Override
      public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
        Files.delete(file);
        return FileVisitResult.CONTINUE;
      }

      @Override
      public FileVisitResult postVisitDirectory(final Path dir, final IOException exc) throws IOException {
        Files.delete(dir);
        return FileVisitResult.CONTINUE;
      }
    });
  }
}
