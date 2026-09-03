package legend.game.saves;

import legend.core.memory.types.IntRef;
import legend.game.modding.coremod.CoreMod;
import legend.game.modding.events.config.ConfigLoadedEvent;
import legend.game.unpacker.ExpandableFileData;
import legend.game.unpacker.FileData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.legendofdragoon.modloader.registries.RegistryDelegate;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Predicate;

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
    saveConfig(configs, location, file, config -> true, ConfigStorage::serialize);
  }

  public static void loadConfig(final ConfigCollection configs, final ConfigStorageLocation storageLocation, final FileData data) {
    loadConfig(configs, storageLocation, data, config -> true, ConfigStorage::deserialize, true);
    EVENTS.postEvent(new ConfigLoadedEvent(configs, storageLocation));
    RENDERER.setFrameSkipOption(CONFIG.getConfig(CoreMod.FRAME_SKIP_CONFIG.get()));
  }

  public static void saveConfig(final ConfigCollection configs, final ConfigStorageLocation storageLocation, final FileData data, final IntRef offset) {
    saveConfig(configs, storageLocation, data, offset, config -> true, ConfigStorage::serialize);
  }

  static void loadConfig(final ConfigCollection configs, final ConfigStorageLocation storageLocation, final Path file, final Predicate<ConfigEntry<?>> filter, final BiFunction<ConfigEntry<?>, byte[], Object> deserializer) {
    LOGGER.info("Loading filtered config %s from %s", storageLocation, file);

    if(!Files.exists(file)) return;

    try {
      loadConfig(configs, storageLocation, new FileData(Files.readAllBytes(file)), filter, deserializer, false);
    } catch(final Throwable e) {
      LOGGER.warn("Failed to load filtered config file %s", file);
      LOGGER.warn("Exception", e);
    }
  }

  static void saveConfig(final ConfigCollection configs, final ConfigStorageLocation storageLocation, final Path file, final Predicate<ConfigEntry<?>> filter, final BiFunction<ConfigEntry<?>, Object, byte[]> serializer) {
    LOGGER.info("Saving config %s to %s", storageLocation, file);

    final FileData data = new ExpandableFileData(1);
    final IntRef offset = new IntRef();
    saveConfig(configs, storageLocation, data, offset, filter, serializer);

    try {
      writeAtomically(file, Arrays.copyOf(data.getBytes(), offset.get()));
    } catch(final IOException e) {
      LOGGER.warn("Failed to save config file %s", file);
      LOGGER.warn("Exception", e);
    }
  }

  private static void loadConfig(final ConfigCollection configs, final ConfigStorageLocation storageLocation, final FileData data, final Predicate<ConfigEntry<?>> filter, final BiFunction<ConfigEntry<?>, byte[], Object> deserializer, final boolean clearConfig) {
    final IntRef offset = new IntRef();

    if(clearConfig) configs.clearConfig(storageLocation);

    final int configCount = data.readInt(offset);

    for(int configIndex = 0; configIndex < configCount; configIndex++) {
      final RegistryId configId = data.readRegistryId(offset);
      final int configValueLength = data.readInt(offset);
      final byte[] configValue = data.slice(offset.get(), configValueLength).getBytes();
      offset.add(configValueLength);

      if(configId == null) {
        LOGGER.warn("Unknown config ID %s", configId);
        continue;
      }

      final RegistryDelegate<ConfigEntry<?>> delegate = REGISTRIES.config.getEntry(configId);

      if(!delegate.isValid()) {
        LOGGER.warn("Unknown config ID %s", configId);
        continue;
      }

      final ConfigEntry<?> configEntry = delegate.get();

      if(configEntry.storageLocation != storageLocation || !filter.test(configEntry)) continue;

      try {
        setConfigQuietly(configs, configEntry, deserializer.apply(configEntry, configValue));
      } catch(final Throwable e) {
        LOGGER.warn("Ignoring invalid config ID %s", configId);
        LOGGER.warn("Exception", e);
      }
    }
  }

  private static void saveConfig(final ConfigCollection configs, final ConfigStorageLocation storageLocation, final FileData data, final IntRef offset, final Predicate<ConfigEntry<?>> filter, final BiFunction<ConfigEntry<?>, Object, byte[]> serializer) {
    final Map<RegistryId, byte[]> config = new HashMap<>();

    for(final RegistryId configId : REGISTRIES.config) {
      final ConfigEntry<?> configEntry = REGISTRIES.config.getEntry(configId).get();

      if(configEntry.storageLocation != storageLocation || !filter.test(configEntry)) continue;

      final Object value = configs.getConfig(configEntry);

      if(value == null) {
        LOGGER.warn("Unknown config ID %s", configId);
        continue;
      }

      try {
        config.put(configId, serializer.apply(configEntry, value));
      } catch(final Throwable e) {
        LOGGER.warn("Ignoring invalid config ID %s while saving", configId);
        LOGGER.warn("Exception", e);
      }
    }

    data.writeInt(offset, config.size());

    for(final var entry : config.entrySet()) {
      data.writeRegistryId(offset, entry.getKey());
      data.writeInt(offset, entry.getValue().length);
      data.write(0, entry.getValue(), offset.get(), entry.getValue().length);
      offset.add(entry.getValue().length);
    }
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private static byte[] serialize(final ConfigEntry config, final Object value) {
    return (byte[])config.serializer.apply(value);
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private static Object deserialize(final ConfigEntry config, final byte[] value) {
    return config.deserializer.apply(value);
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private static void setConfigQuietly(final ConfigCollection configs, final ConfigEntry config, final Object value) {
    configs.setConfigQuietly(config, value);
  }

  private static void writeAtomically(final Path file, final byte[] data) throws IOException {
    final Path absoluteFile = file.toAbsolutePath();
    final Path parent = absoluteFile.getParent();
    Path temporaryFile = null;

    try {
      Files.createDirectories(parent);
      temporaryFile = Files.createTempFile(parent, absoluteFile.getFileName().toString(), ".tmp");
      Files.write(temporaryFile, data);

      try {
        Files.move(temporaryFile, absoluteFile, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
      } catch(final AtomicMoveNotSupportedException e) {
        Files.move(temporaryFile, absoluteFile, StandardCopyOption.REPLACE_EXISTING);
      }

      temporaryFile = null;
    } finally {
      if(temporaryFile != null) Files.deleteIfExists(temporaryFile);
    }
  }
}
