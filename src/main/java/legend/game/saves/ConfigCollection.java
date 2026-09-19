package legend.game.saves;

import legend.core.lang.I18nText;
import legend.core.lang.TextComponent;
import legend.game.modding.events.config.ConfigUpdatedEvent;
import legend.lodmod.LodMod;
import org.legendofdragoon.modloader.ModContainer;
import org.legendofdragoon.modloader.registries.RegistryDelegate;
import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static legend.core.GameEngine.EVENTS;
import static legend.core.GameEngine.MODS;
import static legend.core.GameEngine.REGISTRIES;

public class ConfigCollection {
  private final boolean notifyChanges;
  private final Map<RegistryId, Object> configValues = new HashMap<>();
  private final Map<RegistryId, Set<String>> locked = new HashMap<>();
  @Nullable
  private ConfigPresetState presetState;
  private long revision;

  public ConfigCollection() {
    this(true);
  }

  /** Use false for detached edits that must not change runtime state or post events. */
  public ConfigCollection(final boolean notifyChanges) {
    this.notifyChanges = notifyChanges;
  }

  public <T> T getConfig(final ConfigEntry<T> config) {
    //noinspection unchecked
    return (T)this.configValues.getOrDefault(config.getRegistryId(), config.getDefaultValue());
  }

  public <T> void setConfig(final ConfigEntry<T> config, final T value) {
    final T oldValue = this.getConfig(config);
    this.setConfigQuietly(config, value);
    if(this.notifyChanges) {
      config.onChange(this, oldValue, value);
      EVENTS.postEvent(new ConfigUpdatedEvent(config));
    }
  }

  /** Doesn't trigger onChange */
  <T> void setConfigQuietly(final ConfigEntry<T> config, final T value) {
    this.configValues.put(config.getRegistryId(), value);
    this.revision++;
  }

  /** Records the successfully applied preset without changing values or firing callbacks. */
  public void setPreset(final TextComponent name) {
    this.presetState = new ConfigPresetState(name, this, this.revision);
  }

  @Nullable
  public TextComponent getPresetName() {
    return this.presetState != null ? this.presetState.name : null;
  }

  public boolean isPresetModified() {
    return this.presetState != null && this.presetState.isModified(this, this.revision);
  }

  public TextComponent getPresetDisplayName() {
    if(this.presetState == null) return new I18nText("lod_core.config_presets.custom");
    if(this.isPresetModified()) return new I18nText("lod_core.config_presets.modified", this.presetState.name);
    return this.presetState.name;
  }

  /** Rechecks mutable values and registry defaults after returning from an editor or mod reload. */
  public void refreshPreset() {
    this.revision++;
  }

  public boolean hasConfig(final ConfigEntry<?> config) {
    return this.configValues.containsKey(config.getRegistryId());
  }

  public void clearConfig() {
    this.configValues.clear();
    this.presetState = null;
  }

  public void clearConfig(final ConfigStorageLocation storageLocation) {
    this.presetState = null;
    this.configValues.keySet().removeIf(id -> {
      final RegistryDelegate<ConfigEntry<?>> delegate = REGISTRIES.config.getEntry(id);
      return !delegate.isValid() || delegate.get().storageLocation == storageLocation;
    });
  }

  public void copyConfigFrom(final ConfigCollection other) {
    this.configValues.putAll(other.configValues);
    this.presetState = null;
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
