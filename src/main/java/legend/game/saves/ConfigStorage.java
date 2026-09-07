package legend.game.saves;

import legend.core.lang.I18nText;
import legend.core.memory.types.IntRef;
import legend.core.tags.ListTag;
import legend.core.tags.MapTag;
import legend.core.tags.RawTag;
import legend.core.tags.RegistryIdTag;
import legend.game.modding.coremod.CoreMod;
import legend.game.modding.events.config.ConfigLoadedEvent;
import legend.game.ui.GameOverlay;
import legend.game.unpacker.FileData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.legendofdragoon.modloader.registries.RegistryDelegate;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.EVENTS;
import static legend.core.GameEngine.REGISTRIES;
import static legend.core.GameEngine.RENDERER;

public final class ConfigStorage {
  private ConfigStorage() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(ConfigStorage.class);

  public static void loadConfig(final ConfigCollection configs, final ConfigStorageLocation location, final Path file) {
    LOGGER.info("Loading config %s from %s", location, file);

    if(!Files.exists(file)) {
      configs.clearConfig(location);
      return;
    }

    final FileData data;

    try {
      data = new FileData(Files.readAllBytes(file));
      loadConfig(configs, location, data);
    } catch(final Throwable e) {
      LOGGER.warn("Failed to load config file %s", file);
      LOGGER.warn("Exception", e);
    }
  }

  public static void saveConfig(final ConfigCollection configs, final ConfigStorageLocation location, final Path file) {
    LOGGER.info("Saving config %s to %s", location, file);

    try {
      Files.createDirectories(file.toAbsolutePath().getParent());
    } catch(final IOException e) {
      LOGGER.warn("Failed to create parent directories for config file %s", file);
      LOGGER.warn("Exception", e);
      return;
    }

    final FileData data = new FileData(new byte[100 * 1024]);
    final IntRef size = new IntRef();
    saveConfig(configs, location, data, size);

    try {
      Files.write(file, data.slice(0, size.get()).getBytes());
    } catch(final IOException e) {
      LOGGER.warn("Failed to save config file %s", file);
      LOGGER.warn("Exception", e);
    }
  }

  public static void loadConfig(final ConfigCollection configs, final ConfigStorageLocation storageLocation, final FileData data) {
    int offset = 0;

    configs.clearConfig(storageLocation);

    final int configCount = data.readInt(offset);
    offset += 4;

    for(int configIndex = 0; configIndex < configCount; configIndex++) {
      final RegistryId configId = data.readRegistryId(offset);
      offset += configId.toString().length() + 3;

      final RegistryDelegate<ConfigEntry<?>> delegate = REGISTRIES.config.getEntry(configId);

      if(delegate.isValid()) {
        //noinspection rawtypes
        final ConfigEntry configEntry = delegate.get();

        final int configValueLength = data.readInt(offset);
        offset += 4;

        final byte[] configValueRaw = data.slice(offset, configValueLength).getBytes();
        offset += configValueLength;

        if(configEntry != null) {
          if(configEntry.storageLocation == storageLocation) {
            //noinspection unchecked
            configs.setConfigQuietly(configEntry, configEntry.deserializer.apply(configValueRaw));
          }
        } else {
          LOGGER.warn("Unknown config ID %s", configId);
        }
      } else {
        LOGGER.warn("Unknown mod ID %s", configId);
        final int configValueLength = data.readInt(offset);
        offset += 4;
        offset += configValueLength;
      }
    }

    EVENTS.postEvent(new ConfigLoadedEvent(configs, storageLocation));
    RENDERER.setFrameSkipOption(CONFIG.getConfig(CoreMod.FRAME_SKIP_CONFIG.get()));
  }

  public static void saveConfig(final ConfigCollection configs, final ConfigStorageLocation storageLocation, final FileData data, final IntRef offset) {
    final Map<RegistryId, byte[]> config = new HashMap<>();

    for(final RegistryId configId : REGISTRIES.config) {
      //noinspection rawtypes
      final ConfigEntry configEntry = REGISTRIES.config.getEntry(configId).get();

      if(configEntry.storageLocation == storageLocation) {
        //noinspection unchecked
        final Object value = configs.getConfig(configEntry);

        if(value != null) {
          //noinspection unchecked
          config.put(configId, (byte[])configEntry.serializer.apply(value));
        } else {
          LOGGER.warn("Unknown config ID %s", configId);
        }
      }
    }

    data.writeInt(offset, config.size());

    for(final var entry : config.entrySet()) {
      data.writeRegistryId(offset, entry.getKey());
      data.writeInt(offset, entry.getValue().length);
      data.write(0, entry.getValue(), offset, entry.getValue().length);
    }
  }

  public static void loadConfig(final ConfigCollection configs, final ConfigStorageLocation storageLocation, final MapTag tag) {
    configs.clearConfig(storageLocation);

    final ListTag locationTag = tag.get(storageLocation.name()).asList();
    int skipped = 0;

    for(int configIndex = 0; configIndex < locationTag.size(); configIndex++) {
      RegistryId configId = null;
      try {
        final MapTag configTag = locationTag.get(configIndex).asMap();
        configId = configTag.get("configId").asRegistryId().get();
        final RegistryDelegate<ConfigEntry<?>> delegate = REGISTRIES.config.getEntry(configId);

        if(!delegate.isValid()) {
          throw new IllegalArgumentException("Unknown config ID " + configId);
        }

        final ConfigEntry<?> configEntry = delegate.get();
        if(configEntry.storageLocation != storageLocation) {
          throw new IllegalArgumentException("Incorrect storage location for " + configId);
        }

        loadConfigValue(configs, configEntry, configTag);
      } catch(final RuntimeException e) {
        skipped++;
        LOGGER.warn("Skipping preset setting %s at %s[%d]", configId, storageLocation, configIndex, e);
      }
    }

    if(skipped != 0) {
      GameOverlay.addNotification(5, new I18nText("lod_core.ui.options_presets.skipped_settings", skipped));
    }

    EVENTS.postEvent(new ConfigLoadedEvent(configs, storageLocation));
    RENDERER.setFrameSkipOption(CONFIG.getConfig(CoreMod.FRAME_SKIP_CONFIG.get()));
  }

  private static <T> void loadConfigValue(final ConfigCollection configs, final ConfigEntry<T> configEntry, final MapTag configTag) {
    final byte[] data = configTag.get("data").asRaw().get();
    final T value = Objects.requireNonNull(configEntry.deserializer.apply(data), "Config deserializer returned null");
    configs.setConfigQuietly(configEntry, value);
  }

  public static void saveConfig(final ConfigCollection configs, final ConfigStorageLocation storageLocation, final MapTag tag) {
    final ListTag locationTag = new ListTag();
    tag.set(storageLocation.name(), locationTag);

    for(final RegistryId configId : REGISTRIES.config) {
      //noinspection rawtypes
      final ConfigEntry configEntry = REGISTRIES.config.getEntry(configId).get();

      if(configEntry.storageLocation == storageLocation) {
        //noinspection unchecked
        final Object value = configs.getConfig(configEntry);

        if(value != null) {
          final MapTag configTag = new MapTag();
          locationTag.add(configTag);

          configTag.set("configId", new RegistryIdTag(configId));
          //noinspection unchecked
          configTag.set("data", new RawTag((byte[])configEntry.serializer.apply(value)));
        } else {
          LOGGER.warn("Unknown config ID %s", configId);
        }
      }
    }
  }
}
