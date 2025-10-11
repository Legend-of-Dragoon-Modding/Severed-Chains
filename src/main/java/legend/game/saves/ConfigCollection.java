package legend.game.saves;

import legend.game.modding.events.config.ConfigUpdatedEvent;
import legend.lodmod.LodMod;
import org.legendofdragoon.modloader.ModContainer;
import org.legendofdragoon.modloader.registries.RegistryDelegate;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static legend.core.GameEngine.EVENTS;
import static legend.core.GameEngine.MODS;
import static legend.core.GameEngine.REGISTRIES;

public class ConfigCollection {
  private final Map<RegistryId, Object> configValues = new HashMap<>();
  private final Map<RegistryId, Set<String>> locked = new HashMap<>();

  public <T> T getConfig(final ConfigEntry<T> config) {
    //noinspection unchecked
    return (T)this.configValues.getOrDefault(config.getRegistryId(), config.defaultValue);
  }

  public <T> void setConfig(final ConfigEntry<T> config, final T value) {
    final T oldValue = this.getConfig(config);
    this.setConfigQuietly(config, value);
    config.onChange(this, oldValue, value);
    EVENTS.postEvent(new ConfigUpdatedEvent(config));
  }

  /** Doesn't trigger onChange */
  <T> void setConfigQuietly(final ConfigEntry<T> config, final T value) {
    this.configValues.put(config.getRegistryId(), value);
  }

  public boolean hasConfig(final ConfigEntry<?> config) {
    return this.configValues.containsKey(config.getRegistryId());
  }

  public void clearConfig() {
    this.configValues.clear();
  }

  public void clearConfig(final ConfigStorageLocation storageLocation) {
    this.configValues.keySet().removeIf(id -> {
      final RegistryDelegate<ConfigEntry<?>> delegate = REGISTRIES.config.getEntry(id);
      return !delegate.isValid() || delegate.get().storageLocation == storageLocation;
    });
  }

  public void copyConfigFrom(final ConfigCollection other) {
    this.configValues.putAll(other.configValues);
  }

  public void lockConfig(final ConfigEntry<?> config) {
    final Class<?> caller = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).getCallerClass();
    MODS.setActiveModByClassloader(caller.getClassLoader());
    final ModContainer mod = ModContainer.getActiveMod();
    this.locked.computeIfAbsent(config.getRegistryId(), k -> new HashSet<>()).add(mod != null ? mod.modId : LodMod.MOD_ID);
  }

  public Set<String> getLocked(final ConfigEntry<?> config) {
    return this.locked.computeIfAbsent(config.getRegistryId(), k -> new HashSet<>());
  }
}
