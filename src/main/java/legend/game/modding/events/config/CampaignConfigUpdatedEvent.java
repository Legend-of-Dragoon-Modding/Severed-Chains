package legend.game.modding.events.config;

import legend.game.saves.ConfigCollection;
import legend.game.saves.ConfigEntry;

public class CampaignConfigUpdatedEvent extends ConfigEvent {
  public final ConfigCollection configCollection;
  public final ConfigEntry<?> config;

  public CampaignConfigUpdatedEvent(final ConfigCollection configCollection, final ConfigEntry<?> config) {
    this.configCollection = configCollection;
    this.config = config;
  }
}
