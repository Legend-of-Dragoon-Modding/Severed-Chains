package legend.game.modding.coremod.config;

import legend.game.combat.AdditionMode;
import legend.game.combat.DragoonAdditionMode;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigStorageLocation;
import legend.game.saves.EnumConfigEntry;

public class DragoonAdditionModeConfigEntry extends EnumConfigEntry<DragoonAdditionMode> {
  public DragoonAdditionModeConfigEntry() {
    super(DragoonAdditionMode.class, DragoonAdditionMode.NORMAL, ConfigStorageLocation.CAMPAIGN, ConfigCategory.ADDITIONS);
  }
}
