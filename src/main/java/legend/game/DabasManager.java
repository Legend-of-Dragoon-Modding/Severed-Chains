package legend.game;

import legend.game.unpacker.FileData;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class DabasManager {
  private DabasManager() { }

  private static final Path dir = Paths.get("saves");

  private static String getFilename() {
    return "dabas.ddab";
  }

  public static boolean hasSave() {
    return Files.exists(dir.resolve(getFilename()));
  }

  public static FileData loadSave() {
    final Path file = dir.resolve(getFilename());

    if(!Files.exists(file)) {
      throw new RuntimeException("No saved diiig");
    }

    final byte[] data;
    try {
      data = Files.readAllBytes(file);
    } catch(final IOException e) {
      throw new RuntimeException("Failed to load diiig", e);
    }

    return new FileData(data, 0, 0x80);
  }
}
