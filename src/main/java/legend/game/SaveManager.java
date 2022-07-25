package legend.game;

import legend.core.MathHelper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public final class SaveManager {
  private SaveManager() { }

  public static final int MAX_SAVES = 15;

  private static final Path dir = Paths.get("saves");

  public static boolean hasSavedGame(final int slot) {
    return Files.exists(dir.resolve(String.valueOf(slot)));
  }

  public static boolean hasSavedGames() {
    for(int i = 0; i < MAX_SAVES; i++) {
      if(hasSavedGame(i)) {
        return true;
      }
    }

    return false;
  }

  public static void saveGame(final int slot, final byte[] data) {
    try {
      Files.createDirectories(dir);
      Files.write(dir.resolve(String.valueOf(slot)), data, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
    } catch(final IOException e) {
      throw new RuntimeException("Failed to save game", e);
    }
  }

  public static byte[] loadGame(final int slot) {
    if(!hasSavedGame(slot)) {
      throw new RuntimeException("No saved game in slot " + slot);
    }

    final Path file = dir.resolve(String.valueOf(slot));

    final byte[] data;
    try {
      data = Files.readAllBytes(file);
    } catch(final IOException e) {
      throw new RuntimeException("Failed to load game", e);
    }

    //LAB_80109e38
    if(MathHelper.get(data, 0x180, 4) != 0x5a02_0006L) {
      throw new RuntimeException("Invalid save data");
    }

    return data;
  }
}
