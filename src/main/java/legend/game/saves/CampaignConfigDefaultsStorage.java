package legend.game.saves;

import legend.core.memory.types.IntRef;
import legend.game.unpacker.ExpandableFileData;
import legend.game.unpacker.FileData;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Set;

public final class CampaignConfigDefaultsStorage {
  private static final Path FILE = Path.of("campaign_defaults.dcnf");
  private static final Set<String> BUNDLED_MOD_IDS = Set.of("lod_core", "lod", "turn_order");

  private CampaignConfigDefaultsStorage() { }

  public static void load(final ConfigCollection configs) {
    ConfigStorage.loadConfig(configs, ConfigStorageLocation.CAMPAIGN, FILE, CampaignConfigDefaultsStorage::manages, CampaignConfigDefaultsStorage::deserialize);
  }

  public static void save(final ConfigCollection configs) {
    ConfigStorage.saveConfig(configs, ConfigStorageLocation.CAMPAIGN, FILE, CampaignConfigDefaultsStorage::manages, CampaignConfigDefaultsStorage::serialize);
  }

  public static boolean manages(final ConfigEntry<?> config) {
    final RegistryId id = config.getRegistryId();
    return config.storageLocation == ConfigStorageLocation.CAMPAIGN && config.hasEditControl() && id != null && BUNDLED_MOD_IDS.contains(id.modId());
  }

  private static byte[] serialize(final ConfigEntry<?> config, final Object value) {
    final byte[] configValue = serializeConfigValue(config, value);
    final FileData data = new ExpandableFileData(1);
    final IntRef offset = new IntRef();
    data.writeAscii(offset, typeSignature(config));
    data.write(0, configValue, offset.get(), configValue.length);
    offset.add(configValue.length);
    return Arrays.copyOf(data.getBytes(), offset.get());
  }

  private static Object deserialize(final ConfigEntry<?> config, final byte[] value) {
    final FileData data = new FileData(value);
    final IntRef offset = new IntRef();
    final String storedTypeSignature = data.readAscii(offset);
    final String currentTypeSignature = typeSignature(config);

    if(!storedTypeSignature.equals(currentTypeSignature)) {
      throw new IllegalArgumentException("Incompatible config type " + storedTypeSignature + " for " + config.getRegistryId());
    }

    return deserializeConfigValue(config, data.slice(offset.get()).getBytes());
  }

  private static String typeSignature(final ConfigEntry<?> config) {
    final Object defaultValue = config.getDefaultValue();
    final String valueType = defaultValue == null ? "null" : defaultValue.getClass().getName();
    return config.getClass().getName() + ':' + valueType;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private static byte[] serializeConfigValue(final ConfigEntry config, final Object value) {
    return (byte[])config.serializer.apply(value);
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private static Object deserializeConfigValue(final ConfigEntry config, final byte[] value) {
    return config.deserializer.apply(value);
  }
}
