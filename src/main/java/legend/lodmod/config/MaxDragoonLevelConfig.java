package legend.lodmod.config;

import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigStorageLocation;
import legend.game.saves.IntConfigEntry;

public class MaxDragoonLevelConfig extends IntConfigEntry {
  public MaxDragoonLevelConfig() {
    super(5, 1, 50, ConfigStorageLocation.CAMPAIGN, ConfigCategory.GAMEPLAY);
  }
}
