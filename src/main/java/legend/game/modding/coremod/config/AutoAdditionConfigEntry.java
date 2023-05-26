package legend.game.modding.coremod.config;

import legend.game.combat.AutoAdditionMode;
import legend.game.saves.ConfigStorageLocation;
import legend.game.saves.EnumConfigEntry;

public class AutoAdditionConfigEntry extends EnumConfigEntry<AutoAdditionMode> {
  public AutoAdditionConfigEntry() {
    super(AutoAdditionMode.class, AutoAdditionMode.OFF, ConfigStorageLocation.CAMPAIGN);
  }
}
