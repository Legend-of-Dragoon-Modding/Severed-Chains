package legend.game;

import legend.core.MathHelper;
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

    //LAB_80109e38
    if(MathHelper.get(data, 0x52, 4) != 0x3058_434dL) { // MCX0
      throw new RuntimeException("Invalid diiig data");
    }

    return new FileData(data, 0x580, 0x80);
  }
}
