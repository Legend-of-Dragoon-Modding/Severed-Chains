package legend.lodmod.config;

import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigStorageLocation;
import legend.game.saves.IntConfigEntry;

public class MaxLevelConfig extends IntConfigEntry {
  public MaxLevelConfig() {
    super(60, 1, 250, ConfigStorageLocation.CAMPAIGN, ConfigCategory.GAMEPLAY);
  }
}
