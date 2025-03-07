package legend.game.modding.coremod.config;

import legend.game.saves.BoolConfigEntry;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigStorageLocation;

public class RunByDefaultConfig extends BoolConfigEntry {
  public RunByDefaultConfig() {
    super(true, ConfigStorageLocation.CAMPAIGN, ConfigCategory.CONTROLS);
  }

  @Override
  public boolean hasHelp() {
    return true;
  }
}
