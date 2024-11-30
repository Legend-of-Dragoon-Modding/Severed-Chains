package legend.game.modding.coremod.config;

import legend.game.combat.AdditionButtonMode;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigStorageLocation;
import legend.game.saves.EnumConfigEntry;

public class AdditionButtonModeConfigEntry extends EnumConfigEntry<AdditionButtonMode> {
  public AdditionButtonModeConfigEntry() {
    super(AdditionButtonMode.class, AdditionButtonMode.RETAIL, ConfigStorageLocation.CAMPAIGN, ConfigCategory.ADDITIONS);
  }
}
