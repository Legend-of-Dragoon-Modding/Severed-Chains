package legend.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.function.Consumer;

/**
 * Launches the standalone {@code updater.jar} as a separate process,
 * then shuts down the game so the updater can overwrite files safely.
 *
 * <p>The actual download/extract/apply logic lives in {@link UpdaterMain},
 * which is packaged as a separate {@code updater.jar}. This class is the
 * bridge from the running game to that external process.
 *
 * <p>Flow:
 * <ol>
 *   <li>Game detects an update and user clicks "Auto-Update"</li>
 *   <li>This class launches {@code updater.jar} with the download URL and current PID</li>
 *   <li>Game exits so the updater can freely overwrite JARs and files</li>
 *   <li>The updater downloads, extracts, applies, then relaunches the game</li>
 * </ol>
 */
public final class SelfUpdater {
  private SelfUpdater() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(SelfUpdater.class);

  public enum UpdateState {
    LAUNCHING_UPDATER,
    DONE,
    FAILED
  }

  /**
   * Progress info passed to the UI callback.
   */
  public record UpdateProgress(UpdateState state, String message) { }

  /**
   * Launches the external updater JAR and shuts down the game.
   *
   * @param downloadUrl      the direct download URL for the platform archive
   * @param progressCallback receives status updates (called from the current thread)
   */
  public static void launchUpdaterAndExit(final String downloadUrl, final Consumer<UpdateProgress> progressCallback) {
    try {
      final Path gameDir = Path.of(".").toAbsolutePath().normalize();
      final Path updaterJar = gameDir.resolve("updater.jar");

      if(!Files.exists(updaterJar)) {
        LOGGER.error("updater.jar not found at %s", updaterJar);
        progressCallback.accept(new UpdateProgress(UpdateState.FAILED, "updater.jar not found. Please re-download Severed Chains."));
        return;
      }

      // find the Java executable prefer the bundled JDK fall back to system
      final Path javaExe = findJavaExecutable(gameDir);
      if(javaExe == null) {
        LOGGER.error("Could not find a Java executable to launch the updater");
        progressCallback.accept(new UpdateProgress(UpdateState.FAILED, "Java not found. Cannot launch updater."));
        return;
      }

      // pass the current PID so the updater can wait to exit
      final long pid = ProcessHandle.current().pid();

      progressCallback.accept(new UpdateProgress(UpdateState.LAUNCHING_UPDATER, "Launching updater..."));

      LOGGER.info("Launching updater: %s -jar %s %s %s %d %s", javaExe, updaterJar, downloadUrl, gameDir, pid, Version.FULL_VERSION);

      final ProcessBuilder pb = new ProcessBuilder(
        javaExe.toString(),
        "-jar", updaterJar.toString(),
        downloadUrl,
        gameDir.toString(),
        String.valueOf(pid),
        Version.FULL_VERSION
      );
      pb.directory(gameDir.toFile());
      pb.inheritIO();
      pb.start();

      progressCallback.accept(new UpdateProgress(UpdateState.DONE, "Updater launched. Game will close now."));

      // give the updater a moment to start
      try { Thread.sleep(1000); } catch(final InterruptedException ignored) { }

      // exit the game so the updater can overwrite files
      LOGGER.info("Exiting game for update...");
      System.exit(0);

    } catch(final IOException e) {
      LOGGER.error("Failed to launch updater", e);
      progressCallback.accept(new UpdateProgress(UpdateState.FAILED, "Failed to launch updater: " + e.getMessage()));
    }
  }

  /**
   * finds the Java executable preferring the bundled JDK.
   */
  private static Path findJavaExecutable(final Path gameDir) {
    final String os = System.getProperty("os.name", "").toLowerCase(Locale.US);
    final String exeName = os.contains("win") ? "java.exe" : "java";

    // check bundled JDK first (jdk25/bin/java)
    final Path bundled = gameDir.resolve("jdk25").resolve("bin").resolve(exeName);
    if(Files.exists(bundled)) {
      return bundled;
    }

    // also check the versioned directory name pattern 
    try(final var stream = Files.newDirectoryStream(gameDir, "jdk25*")) {
      for(final Path dir : stream) {
        final Path candidate = dir.resolve("bin").resolve(exeName);
        if(Files.exists(candidate)) {
          return candidate;
        }
      }
    } catch(final IOException ignored) { }

    // fall back to whatever java is on PATH 
    final Path javaHome = Path.of(System.getProperty("java.home"));
    final Path systemJava = javaHome.resolve("bin").resolve(exeName);
    if(Files.exists(systemJava)) {
      return systemJava;
    }

    return null;
  }
}
