package legend.game.saves;

import legend.game.modding.events.config.ConfigLoadedEvent;
import legend.game.unpacker.FileData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.legendofdragoon.modloader.registries.RegistryDelegate;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static legend.core.GameEngine.EVENTS;
import static legend.core.GameEngine.REGISTRIES;

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
    final int size = saveConfig(configs, location, data);

    try {
      Files.write(file, Arrays.copyOf(data.getBytes(), size));
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
  }

  public static int saveConfig(final ConfigCollection configs, final ConfigStorageLocation storageLocation, final FileData data) {
    final Map<RegistryId, byte[]> config = new HashMap<>();
    int offset = 0;

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
    offset += 4;

    for(final var entry : config.entrySet()) {
      data.writeRegistryId(offset, entry.getKey());
      offset += entry.getKey().toString().length() + 3;

      data.writeInt(offset, entry.getValue().length);
      offset += 4;

      data.copyTo(0, entry.getValue(), offset, entry.getValue().length);
      offset += entry.getValue().length;
    }

    return offset;
  }
}
