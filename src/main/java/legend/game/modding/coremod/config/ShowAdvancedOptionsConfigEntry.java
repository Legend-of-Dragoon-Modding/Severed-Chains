package legend.game.modding.coremod.config;

import legend.game.saves.BoolConfigEntry;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigStorageLocation;

public class ShowAdvancedOptionsConfigEntry extends BoolConfigEntry {
  public ShowAdvancedOptionsConfigEntry() {
    super(false, ConfigStorageLocation.GLOBAL, ConfigCategory.OTHER, false);
  }
}
