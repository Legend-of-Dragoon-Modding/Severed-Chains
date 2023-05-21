package legend.game.modding.events.config;

import legend.game.saves.ConfigStorageLocation;

public class ConfigLoadedEvent extends ConfigEvent {
  public final ConfigStorageLocation storageLocation;

  public ConfigLoadedEvent(final ConfigStorageLocation storageLocation) {
    this.storageLocation = storageLocation;
  }
}
