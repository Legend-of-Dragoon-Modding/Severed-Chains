package legend.game.modding.coremod.config;

import legend.game.saves.BoolConfigEntry;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigStorageLocation;

public class SwapConfirmCancelConfigEntry extends BoolConfigEntry {
  public SwapConfirmCancelConfigEntry() {
    super(false, ConfigStorageLocation.GLOBAL, ConfigCategory.CONTROLS);
  }

  @Override
  public boolean hasHelp() {
    return true;
  }
}
