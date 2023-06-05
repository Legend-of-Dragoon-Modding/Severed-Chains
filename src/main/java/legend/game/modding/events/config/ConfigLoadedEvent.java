package legend.game.modding.events.config;

import legend.game.saves.ConfigCollection;
import legend.game.saves.ConfigStorageLocation;

public class ConfigLoadedEvent extends ConfigEvent {
  public final ConfigCollection configCollection;
  public final ConfigStorageLocation storageLocation;

  public ConfigLoadedEvent(final ConfigCollection configCollection, final ConfigStorageLocation storageLocation) {
    this.configCollection = configCollection;
    this.storageLocation = storageLocation;
  }
}
