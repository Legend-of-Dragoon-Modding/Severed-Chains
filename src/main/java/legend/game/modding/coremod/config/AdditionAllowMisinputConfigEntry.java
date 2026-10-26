package legend.game.modding.coremod.config;

import legend.game.saves.BoolConfigEntry;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigStorageLocation;

public class AdditionAllowMisinputConfigEntry extends BoolConfigEntry {
  public AdditionAllowMisinputConfigEntry() {
    super(false, ConfigStorageLocation.CAMPAIGN, ConfigCategory.GAMEPLAY);
  }
}
