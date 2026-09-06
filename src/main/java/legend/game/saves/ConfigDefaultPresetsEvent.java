package legend.game.saves;

import legend.game.modding.events.config.ConfigEvent;

import java.util.List;

public class ConfigDefaultPresetsEvent extends ConfigEvent {
  public final List<ConfigPresetEntry> presetEntries;

  public ConfigDefaultPresetsEvent(final List<ConfigPresetEntry> presetEntries) {
    this.presetEntries = presetEntries;
  }
}
