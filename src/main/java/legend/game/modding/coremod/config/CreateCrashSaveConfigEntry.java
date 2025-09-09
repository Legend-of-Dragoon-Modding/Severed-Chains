package legend.game.modding.coremod.config;

import legend.game.saves.BoolConfigEntry;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigStorageLocation;

public class CreateCrashSaveConfigEntry extends BoolConfigEntry {
  public CreateCrashSaveConfigEntry() {
    super(true, ConfigStorageLocation.GLOBAL, ConfigCategory.GAMEPLAY);
  }

  @Override
  public boolean hasHelp() {
    return true;
  }
}
