package legend.game.saves;

import com.github.slugify.Slugify;
import legend.game.types.ActiveStatsa0;
import legend.game.types.GameState52c;
import legend.game.unpacker.FileData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static legend.core.GameEngine.CONFIG;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;

public final class SaveManager {
  private static final Logger LOGGER = LogManager.getFormatterLogger(SaveManager.class);

  private static final Slugify slug = Slugify.builder().underscoreSeparator(true).customReplacement("'", "").build();

  private final Path dir = Paths.get("saves");
  private final PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:*.dsav");

  private final Map<Function<FileData, FileData>, SaveDeserializer> deserializers = new HashMap<>();
  private final int serializerMagic;
  private final SaveSerializer serializer;

  public SaveManager(final int serializerMagic, final SaveSerializer serializer) {
    this.serializerMagic = serializerMagic;
    this.serializer = serializer;
  }

  public void registerDeserializer(final Function<FileData, FileData> matcher, final SaveDeserializer deserializer) {
    this.deserializers.put(matcher, deserializer);
  }

  public String generateCampaignName() {
    if(!Files.exists(this.dir.resolve("New Campaign"))) {
      return "New Campaign";
    }

    for(int i = 2; ; i++) {
      if(!Files.exists(this.dir.resolve("New Campaign " + i))) {
        return "New Campaign " + i;
      }
    }
  }

  public boolean campaignExists(final String campaign) {
    return Files.exists(this.dir.resolve(campaign));
  }

  public String slugName(final String name) {
    return slug.slugify(name);
  }

  public boolean saveExists(final String campaign, final String saveName) {
    return Files.exists(this.dir.resolve(campaign).resolve(this.slugName(saveName) + ".dsav"));
  }

  public String generateSaveName(final String campaign) {
    for(int i = 1; ; i++) {
      if(!this.saveExists(campaign, "Save " + i)) {
        return "Save " + i;
      }
    }
  }

  /** Look for saves from before campaigns were a thing */
  public void updateUncategorizedSaves() {
    try(final Stream<Path> stream = Files.list(this.dir)) {
      final List<Path> files = stream
        .filter(file -> !Files.isDirectory(file) && this.matcher.matches(file.getFileName()))
        .toList();

      if(!files.isEmpty()) {
        LOGGER.info("Upgrading legacy saves to campaign");

        final Path campaignDir = this.dir.resolve(this.generateCampaignName());
        Files.createDirectories(campaignDir);

        for(final Path file : files) {
          Files.move(file, campaignDir.resolve(file.getFileName()));
        }
      }
    } catch(final IOException e) {
      LOGGER.error("Failed to update legacy saves to campaigns");
    }
  }

  private List<Path> getCampaigns() {
    try {
      Files.createDirectories(this.dir);
    } catch(final IOException e) {
      throw new RuntimeException(e);
    }

    try(final Stream<Path> stream = Files.list(this.dir)) {
      return stream
        .filter(Files::isDirectory)
        .filter(path -> {
          try(final Stream<Path> children = Files.list(path)) {
            return children.anyMatch(child -> child.toString().endsWith(".dsav"));
          } catch(final IOException e) {
            throw new RuntimeException(e);
          }
        })
        .sorted(Comparator.comparingLong((final Path path) -> {
          try(final Stream<Path> children = this.childrenSortedByDate(path)) {
            return children
              .findFirst()
              .map(this::getModifiedTime)
              .orElse(0L);
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

  private List<Path> getSaves(final String campaign) {
    final Path campaignPath = this.dir.resolve(campaign);

    try {
      Files.createDirectories(campaignPath);
    } catch(final IOException e) {
      throw new RuntimeException(e);
    }

    try(final Stream<Path> children = this.childrenSortedByDate(campaignPath)) {
      return children
        .map(Path::getFileName)
        .collect(Collectors.toList());
    } catch(final IOException e) {
      throw new RuntimeException(e);
    }
  }

  private Stream<Path> childrenSortedByDate(final Path path) throws IOException {
    return Files.list(path)
      .filter(file -> !Files.isDirectory(file) && this.matcher.matches(file.getFileName()))
      .sorted(Comparator.comparingLong(this::getModifiedTime).reversed());
  }

  private long getModifiedTime(final Path path) {
    try {
      return Files.getLastModifiedTime(path).to(TimeUnit.MILLISECONDS);
    } catch(final IOException e) {
      throw new RuntimeException(e);
    }
  }

  public boolean hasCampaigns() {
    return !this.getCampaigns().isEmpty();
  }

  public void overwriteSave(final String fileName, final String saveName, final GameState52c state, final ActiveStatsa0[] activeStats) throws SaveFailedException {
    try {
      ConfigStorage.saveConfig(CONFIG, ConfigStorageLocation.CAMPAIGN, Path.of("saves", gameState_800babc8.campaignName, "campaign_config.dcnf"));

      final FileData data = new FileData(new byte[0x4000]); // Lots of extra space
      data.writeInt(0x0, this.serializerMagic);
      final int length = this.serializer.serializer(saveName, data.slice(0x4), state, activeStats);

      final Path dir = this.dir.resolve(state.campaignName);

      Files.createDirectories(dir);
      Files.write(dir.resolve(fileName + ".dsav"), data.slice(0x0, length + 0x4).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
    } catch(final IOException e) {
      throw new SaveFailedException("Failed to save game", e);
    }
  }

  public void newSave(final String saveName, final GameState52c state, final ActiveStatsa0[] activeStats) throws SaveFailedException {
    this.overwriteSave(slug.slugify(saveName), saveName, state, activeStats);
  }

  public SavedGame loadGame(final String campaign, final String filename) throws InvalidSaveException {
    final Path file = this.dir.resolve(campaign).resolve(filename + ".dsav");

    if(!Files.exists(file)) {
      throw new InvalidSaveException("No saved game " + filename);
    }

    final FileData data;
    try {
      data = new FileData(Files.readAllBytes(file));
    } catch(final IOException e) {
      throw new InvalidSaveException("Failed to load saved game " + filename, e);
    }

    //LAB_80109e38
    for(final var entry : this.deserializers.entrySet()) {
      final FileData slice = entry.getKey().apply(data);

      if(slice != null) {
        final SavedGame savedGame = entry.getValue().deserialize(filename, slice);
        savedGame.state().campaignName = campaign;
        return savedGame;
      }
    }

    throw new InvalidSaveException("Invalid save data " + filename);
  }

  public List<Campaign> loadAllCampaigns() {
    final List<Campaign> campaigns = new ArrayList<>();

    campaignLoop:
    for(final Path child : this.getCampaigns()) {
      final String filename = child.getFileName().toString();

      final List<SavedGame> saves = this.loadAllSaves(filename);

      if(!saves.isEmpty()) {
        // Display the first valid save, and use the first save if no valid ones are found
        for(final SavedGame save : saves) {
          if(save.isValid()) {
            campaigns.add(new Campaign(filename, save));
            continue campaignLoop;
          }
        }

        campaigns.add(new Campaign(filename, saves.get(0)));
      }
    }

    return campaigns;
  }

  public List<SavedGame> loadAllSaves(final String campaign) {
    final List<SavedGame> saves = new ArrayList<>();

    for(final Path child : this.getSaves(campaign)) {
      final String filename = child.getFileName().toString();
      final String name = filename.substring(0, filename.lastIndexOf('.'));

      try {
        saves.add(this.loadGame(campaign, name));
      } catch(final InvalidSaveException e) {
        saves.add(SavedGame.invalid(name));
      }
    }

    return saves;
  }
}
