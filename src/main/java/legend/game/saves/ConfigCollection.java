package legend.game.saves;

import java.util.HashMap;
import java.util.Map;

public class ConfigCollection {
  private final Map<ConfigEntry<?>, Object> configValues = new HashMap<>();

  public <T> T getConfig(final ConfigEntry<T> config) {
    //noinspection unchecked
    return (T)this.configValues.getOrDefault(config, config.defaultValue);
  }

  public <T> void setConfig(final ConfigEntry<T> config, final T value) {
    final T oldValue = this.getConfig(config);
    this.configValues.put(config, value);
    config.onChange(oldValue, value);
  }

  public void clearConfig() {
    this.configValues.clear();
  }

  public void clearConfig(final ConfigStorageLocation storageLocation) {
    this.configValues.keySet().removeIf(configEntry -> configEntry.storageLocation == storageLocation);
  }

  public void copyConfigFrom(final ConfigCollection other) {
    this.configValues.putAll(other.configValues);
  }
}
