package legend.game;

import legend.core.MathHelper;
import legend.core.Tuple;
import legend.game.types.SavedGameDisplayData;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class SaveManager {
  private SaveManager() { }

  public static final int MAX_SAVES = 1000;

  private static final Path dir = Paths.get("saves");
  private static final PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:*.dsav");

  private static String getFilename(final int slot) {
    return slot + ".dsav";
  }

  private static boolean hasSavedGame(final int slot) {
    return Files.exists(dir.resolve(getFilename(slot)));
  }

  private static List<Path> getSaves() {
    try {
      Files.createDirectories(dir);
    } catch(final IOException e) {
      throw new RuntimeException(e);
    }

    try(final Stream<Path> stream = Files.list(dir)) {
      return stream
        .filter(file -> !Files.isDirectory(file) && matcher.matches(file.getFileName()))
        .sorted(Comparator.comparingLong((final Path path) -> {
          try {
            return Files.getLastModifiedTime(path).to(TimeUnit.MILLISECONDS);
          } catch(final IOException e) {
            throw new RuntimeException(e);
          }
        }).reversed())
        .map(Path::getFileName)
        .collect(Collectors.toList());
    } catch(final IOException e) {
      throw new RuntimeException("Failed to get save files", e);
    }
  }

  public static boolean hasSavedGames() {
    return !getSaves().isEmpty();
  }

  public static int findFirstAvailableSlot() {
    for(int i = 0; i < MAX_SAVES; i++) {
      if(!hasSavedGame(i)) {
        return i;
      }
    }

    return -1;
  }

  public static void overwriteSave(final String filename, final byte[] data) {
    try {
      Files.createDirectories(dir);
      Files.write(dir.resolve(filename), data, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
    } catch(final IOException e) {
      throw new RuntimeException("Failed to save game", e);
    }
  }

  public static void newSave(final byte[] data) {
    final int index = findFirstAvailableSlot();
    overwriteSave(getFilename(index), data);
  }

  public static byte[] loadGame(final String filename) {
    final Path file = dir.resolve(filename);

    if(!Files.exists(file)) {
      throw new RuntimeException("No saved game " + filename);
    }

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

  public static SavedGameDisplayData loadDisplayData(final String filename) {
    return new SavedGameDisplayData(filename, loadGame(filename));
  }

  public static List<Tuple<String, SavedGameDisplayData>> loadAllDisplayData() {
    final List<Tuple<String, SavedGameDisplayData>> saves = new ArrayList<>();

    for(final Path child : getSaves()) {
      final String filename = child.getFileName().toString();
      saves.add(new Tuple<>(filename, loadDisplayData(filename)));
    }

    return saves;
  }
}
