package legend.lodmod.config;

import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigStorageLocation;
import legend.game.saves.IntConfigEntry;

public class ItemStackSizeConfig extends IntConfigEntry {
  public ItemStackSizeConfig() {
    super(1, 1, 50, ConfigStorageLocation.CAMPAIGN, ConfigCategory.GAMEPLAY);
  }
}
