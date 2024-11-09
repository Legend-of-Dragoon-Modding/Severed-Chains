package legend.game.modding.coremod.config;

import legend.game.combat.AdditionTimingMode;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigStorageLocation;
import legend.game.saves.EnumConfigEntry;

public class AdditionTimingModeConfigEntry extends EnumConfigEntry<AdditionTimingMode> {
  public AdditionTimingModeConfigEntry() {
    super(AdditionTimingMode.class, AdditionTimingMode.RETAIL, ConfigStorageLocation.CAMPAIGN, ConfigCategory.ADDITIONS);
  }
}
