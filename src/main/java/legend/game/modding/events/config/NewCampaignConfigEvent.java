package legend.game.modding.events.config;

import legend.game.saves.ConfigCollection;

public class NewCampaignConfigEvent extends ConfigEvent {
  public final ConfigCollection configCollection;
  public final boolean rememberDefaults;

  public NewCampaignConfigEvent(final ConfigCollection configCollection, final boolean rememberDefaults) {
    this.configCollection = configCollection;
    this.rememberDefaults = rememberDefaults;
  }
}
