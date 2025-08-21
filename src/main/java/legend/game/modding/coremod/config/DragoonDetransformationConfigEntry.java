package legend.game.modding.coremod.config;

import legend.game.combat.ui.DragoonDetransformationMode;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigStorageLocation;
import legend.game.saves.EnumConfigEntry;

public class DragoonDetransformationConfigEntry extends EnumConfigEntry<DragoonDetransformationMode> {
  public DragoonDetransformationConfigEntry() {
    super(DragoonDetransformationMode.class, DragoonDetransformationMode.OFF, ConfigStorageLocation.CAMPAIGN, ConfigCategory.GAMEPLAY);
  }

  @Override
  public boolean hasHelp() {
    return true;
  }
}
