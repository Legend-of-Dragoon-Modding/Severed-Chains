package legend.game.modding.coremod.config;

import legend.game.combat.ui.AdditionOverlayMode;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigStorageLocation;
import legend.game.saves.EnumConfigEntry;

public class AdditionOverlayConfigEntry extends EnumConfigEntry<AdditionOverlayMode> {
  public AdditionOverlayConfigEntry() {
    super(AdditionOverlayMode.class, AdditionOverlayMode.FULL, ConfigStorageLocation.CAMPAIGN, ConfigCategory.GAMEPLAY);
  }

  @Override
  public boolean hasHelp() {
    return true;
  }
}
