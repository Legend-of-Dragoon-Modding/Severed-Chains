package legend.game.saves;

import legend.core.Tuple;
import legend.game.types.GameState52c;
import legend.game.unpacker.FileData;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.ToIntBiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class SaveManager {
  public static final int MAX_SAVES = 1000;

  private final Path dir = Paths.get("saves");
  private final PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:*.dsav");

  private final Map<Function<FileData, FileData>, BiFunction<String, FileData, SavedGame>> deserializers = new HashMap<>();
  private final int serializerMagic;
  private final ToIntBiFunction<FileData, GameState52c> serializer;

  public SaveManager(final int serializerMagic, final ToIntBiFunction<FileData, GameState52c> serializer) {
    this.serializerMagic = serializerMagic;
    this.serializer = serializer;
  }

  public void registerDeserializer(final Function<FileData, FileData> matcher, final BiFunction<String, FileData, SavedGame> deserializer) {
    this.deserializers.put(matcher, deserializer);
  }

  private String getFilename(final int slot) {
    return slot + ".dsav";
  }

  private boolean hasSavedGame(final int slot) {
    return Files.exists(this.dir.resolve(this.getFilename(slot)));
  }

  private List<Path> getSaves() {
    try {
      Files.createDirectories(this.dir);
    } catch(final IOException e) {
      throw new RuntimeException(e);
    }

    try(final Stream<Path> stream = Files.list(this.dir)) {
      return stream
        .filter(file -> !Files.isDirectory(file) && this.matcher.matches(file.getFileName()))
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

  public boolean hasSavedGames() {
    return !this.getSaves().isEmpty();
  }

  public int findFirstAvailableSlot() {
    for(int i = 0; i < MAX_SAVES; i++) {
      if(!this.hasSavedGame(i)) {
        return i;
      }
    }

    return -1;
  }

  public void overwriteSave(final String filename, final GameState52c state) {
    try {
      final FileData data = new FileData(new byte[0x4000]); // Lots of extra space
      data.writeInt(0x0, this.serializerMagic);
      final int length = this.serializer.applyAsInt(data.slice(0x4), state);

      Files.createDirectories(this.dir);
      Files.write(this.dir.resolve(filename), data.slice(0x0, length + 0x4).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
    } catch(final IOException e) {
      throw new RuntimeException("Failed to save game", e);
    }
  }

  public void newSave(final GameState52c state) {
    final int index = this.findFirstAvailableSlot();
    this.overwriteSave(this.getFilename(index), state);
  }

  public SavedGame loadGame(final String filename) {
    final Path file = this.dir.resolve(filename);

    if(!Files.exists(file)) {
      throw new RuntimeException("No saved game " + filename);
    }

    final FileData data;
    try {
      data = new FileData(Files.readAllBytes(file));
    } catch(final IOException e) {
      throw new RuntimeException("Failed to load saved game", e);
    }

    //LAB_80109e38
    for(final var entry : this.deserializers.entrySet()) {
      final FileData slice = entry.getKey().apply(data);

      if(slice != null) {
        return entry.getValue().apply(filename, slice);
      }
    }

    throw new RuntimeException("Invalid save data for file " + filename);
  }

  public List<Tuple<String, SavedGame>> loadAllSaves() {
    final List<Tuple<String, SavedGame>> saves = new ArrayList<>();

    for(final Path child : this.getSaves()) {
      final String filename = child.getFileName().toString();
      saves.add(new Tuple<>(filename, this.loadGame(filename)));
    }

    return saves;
  }
}
