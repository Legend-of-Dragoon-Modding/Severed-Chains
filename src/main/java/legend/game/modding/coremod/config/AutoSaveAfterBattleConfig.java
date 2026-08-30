package legend.game.modding.coremod.config;

import legend.game.saves.BoolConfigEntry;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigStorageLocation;

public class AutoSaveAfterBattleConfig extends BoolConfigEntry {
  public AutoSaveAfterBattleConfig() {
    super(false, ConfigStorageLocation.CAMPAIGN, ConfigCategory.GAMEPLAY);
  }

  @Override
  public boolean hasHelp() {
    return true;
  }
}
