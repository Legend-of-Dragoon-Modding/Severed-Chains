package legend.game.saves;

import legend.core.IoHelper;
import legend.core.lang.I18nText;
import legend.core.lang.RawText;
import legend.core.memory.types.IntRef;
import legend.core.tags.MapTag;
import legend.core.tags.StringTag;
import legend.game.ui.GameOverlay;
import legend.game.unpacker.ExpandableFileData;
import legend.game.unpacker.FileData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public final class ConfigPresetManager {
  private ConfigPresetManager() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(ConfigPresetManager.class);

  private static final Path configPath = Path.of("config");
  public static final PathMatcher CONFIG_MATCHER = FileSystems.getDefault().getPathMatcher("glob:*.dpre");

  public static boolean presetExists(final String name) {
    try {
      Files.createDirectories(configPath);
      return Files.exists(configPath.resolve(IoHelper.slugName(name) + ".dpre"));
    } catch(final IOException e) {
      GameOverlay.addNotification(5, new I18nText("lod_core.ui.options_presets.failed_to_load_presets"));
      LOGGER.warn("Failed to check if options preset exists", e);
      return false;
    }
  }

  public static void deletePreset(final ConfigPresetEntry presetEntry) {
    try {
      Files.deleteIfExists(presetEntry.path);
    } catch(final IOException e) {
      GameOverlay.addNotification(5, new I18nText("lod_core.ui.options_presets.delete_failed"));
      LOGGER.warn("Failed to delete preset", e);
    }
  }

  public static List<ConfigPresetEntry> loadPresetList() {
    LOGGER.info("Loading options presets");

    try {
      Files.createDirectories(configPath);

      try(final Stream<Path> stream = Files.list(configPath)) {
        return stream
          .filter(file -> !Files.isDirectory(file) && CONFIG_MATCHER.matches(file.getFileName()))
          .map(ConfigPresetManager::loadPresetEntry)
          .toList();
      }
    } catch(final IOException e) {
      GameOverlay.addNotification(5, new I18nText("lod_core.ui.options_presets.failed_to_load_presets"));
      LOGGER.warn("Failed to load options presets", e);
      return List.of();
    }
  }

  private static ConfigPresetEntry loadPresetEntry(final Path path) {
    final String name = path.getFileName().toString();
    return new ConfigPresetEntry(path, new RawText(name.substring(0, name.length() - ".dpre".length())), CompletableFuture.supplyAsync(() -> loadPreset(path)), true);
  }

  private static ConfigPreset loadPreset(final Path path) {
    try {
      final FileData data = new FileData(Files.readAllBytes(path));
      final IntRef offset = new IntRef();
      final MapTag tag = new MapTag();
      tag.deserialize(data, offset);

      final String name = tag.get("name").asString().get();

      final ConfigCollection config = new ConfigCollection();
      for(final ConfigStorageLocation location : ConfigStorageLocation.values()) {
        ConfigStorage.loadConfig(config, location, tag);
      }

      return new ConfigPreset(new RawText(name), config);
    } catch(final IOException e) {
      GameOverlay.addNotification(5, new I18nText("lod_core.ui.options_presets.failed_to_load_preset"));
      LOGGER.warn("Failed to load options preset", e);
      return null;
    }
  }

  public static Path savePreset(final String name, final ConfigCollection config) {
    final MapTag tag = new MapTag();

    tag.set("name", new StringTag(name));

    for(final ConfigStorageLocation location : ConfigStorageLocation.values()) {
      ConfigStorage.saveConfig(config, location, tag);
    }

    final FileData data = new ExpandableFileData(256);
    final IntRef offset = new IntRef();
    tag.serialize(data, offset);

    final byte[] out = new byte[offset.get()];
    data.read(0, out, 0, offset.get());

    final Path path = configPath.resolve(IoHelper.slugName(name) + ".dpre");

    try {
      Files.write(path, out);
    } catch(final IOException e) {
      GameOverlay.addNotification(5, new I18nText("lod_core.ui.options_presets.failed_to_save_preset"));
      LOGGER.warn("Failed to save options preset", e);
    }

    return path;
  }

  public static List<ConfigPresetEntry> loadDefaultPresets() {
    final ConfigPreset severedChains = getSeveredChainsDefaults();
    final ConfigPreset vanilla = getVanillaDefaults();
    final ConfigPreset speedrunner = getSpeedrunnerDefaults();

    return List.of(
      new ConfigPresetEntry(null, severedChains.name, CompletableFuture.completedFuture(severedChains), false),
      new ConfigPresetEntry(null, vanilla.name, CompletableFuture.completedFuture(vanilla), false),
      new ConfigPresetEntry(null, speedrunner.name, CompletableFuture.completedFuture(speedrunner), false)
    );
  }

  private static ConfigPreset getSeveredChainsDefaults() {
    return new ConfigPreset(new I18nText("lod_core.config_presets.severed_chains"), new ConfigCollection());
  }

  private static ConfigPreset getVanillaDefaults() {
    return new ConfigPreset(new I18nText("lod_core.config_presets.vanilla"), new ConfigCollection());
  }

  private static ConfigPreset getSpeedrunnerDefaults() {
    return new ConfigPreset(new I18nText("lod_core.config_presets.speedrunner"), new ConfigCollection());
  }
}
