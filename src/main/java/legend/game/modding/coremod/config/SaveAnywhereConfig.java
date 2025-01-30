package legend.game.modding.coremod.config;

import legend.game.saves.BoolConfigEntry;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigStorageLocation;

public class SaveAnywhereConfig extends BoolConfigEntry {
  public SaveAnywhereConfig() {
    super(false, ConfigStorageLocation.CAMPAIGN, ConfigCategory.GAMEPLAY);
  }

  @Override
  public boolean hasHelp() {
    return true;
  }
}
