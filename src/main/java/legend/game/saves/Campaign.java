package legend.game.saves;

import legend.core.IoHelper;
import legend.game.modding.coremod.CoreMod;
import legend.game.unpacker.FileData;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Campaign {
  private static final Logger LOGGER = LogManager.getFormatterLogger(Campaign.class);

  private final SaveManager manager;
  public final ConfigCollection config = new ConfigCollection();
  public final Path path;
  public final String name;
  public final SavedGame latestSave;

  public static Campaign load(final SaveManager manager, final Path path) {
    return new Campaign(manager, path);
  }

  public static Campaign create(final SaveManager manager, final String name) {
    return new Campaign(manager, name);
  }

  private Campaign(final SaveManager manager, final Path path) {
    this.manager = manager;
    this.path = path;
    this.latestSave = this.loadFirstValidSave();
    ConfigStorage.loadConfig(this.config, ConfigStorageLocation.CAMPAIGN, this.path.resolve("campaign_config.dcnf"));
    final String campaignName = this.config.getConfig(CoreMod.CAMPAIGN_NAME.get());

    if(!campaignName.isBlank()) {
      this.name = campaignName;
    } else {
      this.name = path.getFileName().toString();
    }
  }

  private Campaign(final SaveManager manager, final String name) {
    this.manager = manager;
    this.path = manager.resolve(IoHelper.slugName(name));
    this.name = name;
    this.latestSave = null;
    this.config.setConfig(CoreMod.CAMPAIGN_NAME.get(), name);
  }

  public void loadConfigInto(final ConfigCollection config) {
    config.copyConfigFrom(this.config);
  }

  public boolean exists() {
    return Files.exists(this.path);
  }

  public boolean hasSave() {
    return this.latestSave != null;
  }

  public boolean saveExists(final String saveName) {
    return Files.exists(this.path.resolve(IoHelper.slugName(saveName) + ".dsav"));
  }

  public void deleteSave(final String saveName) throws IOException {
    Files.delete(this.path.resolve(saveName + ".dsav"));
  }

  public void delete() throws IOException {
    FileUtils.deleteDirectory(this.path.toFile());
  }

  private SavedGame loadFirstValidSave() {
    if(!this.exists()) {
      return null;
    }

    try(final Stream<Path> children = IoHelper.childrenSortedByDate(this.path, SaveManager.SAVE_MATCHER)) {
      return children
        .map(Path::getFileName)
        .map(Path::toString)
        .map(s -> s.substring(0, s.lastIndexOf(".dsav")))
        .map(this::loadGame)
        .findFirst().orElse(null);
    } catch(final Throwable e) {
      LOGGER.info("Failed to load save", e);
      return null;
    }
  }

  public List<SavedGame> loadAllSaves() {
    final List<SavedGame> saves = new ArrayList<>();

    for(final Path child : this.getSaves()) {
      final String filename = child.getFileName().toString();
      final String name = filename.substring(0, filename.lastIndexOf('.'));

      try {
        saves.add(this.loadGame(name));
      } catch(final InvalidSaveException e) {
        saves.add(SavedGame.invalid(name));
      }
    }

    return saves;
  }

  public SavedGame loadGame(final String filename) throws InvalidSaveException {
    final Path file = this.path.resolve(filename + ".dsav");

    if(!Files.exists(file)) {
      throw new InvalidSaveException("No saved game " + filename);
    }

    final FileData data;
    try {
      data = new FileData(Files.readAllBytes(file));
    } catch(final IOException e) {
      throw new InvalidSaveException("Failed to load saved game " + filename, e);
    }

    return this.manager.loadData(this, filename, data);
  }

  private List<Path> getSaves() {
    final Path campaignPath = this.path;

    try {
      Files.createDirectories(campaignPath);
    } catch(final IOException e) {
      throw new RuntimeException(e);
    }

    try(final Stream<Path> children = IoHelper.childrenSortedByDate(campaignPath, SaveManager.SAVE_MATCHER)) {
      return children
        .map(Path::getFileName)
        .collect(Collectors.toList());
    } catch(final IOException e) {
      throw new RuntimeException(e);
    }
  }
}
