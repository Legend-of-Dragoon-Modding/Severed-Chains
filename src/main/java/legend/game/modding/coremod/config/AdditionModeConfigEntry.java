package legend.game.modding.coremod.config;

import legend.game.combat.AdditionMode;
import legend.game.saves.ConfigStorageLocation;
import legend.game.saves.EnumConfigEntry;

public class AdditionModeConfigEntry extends EnumConfigEntry<AdditionMode> {
  public AdditionModeConfigEntry() {
    super(AdditionMode.class, AdditionMode.NORMAL, ConfigStorageLocation.CAMPAIGN);
  }
}
