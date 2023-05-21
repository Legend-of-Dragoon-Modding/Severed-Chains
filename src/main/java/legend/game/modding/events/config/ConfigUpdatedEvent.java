package legend.game.modding.events.config;

import legend.game.saves.ConfigEntry;

public class ConfigUpdatedEvent extends ConfigEvent {
  public final ConfigEntry<?> config;

  public ConfigUpdatedEvent(final ConfigEntry<?> config) {
    this.config = config;
  }
}
