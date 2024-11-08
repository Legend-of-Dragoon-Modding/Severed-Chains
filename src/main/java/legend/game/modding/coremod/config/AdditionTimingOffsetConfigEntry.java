package legend.game.modding.coremod.config;

import legend.game.combat.ui.AdditionTimingOffset;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigStorageLocation;
import legend.game.saves.EnumConfigEntry;

public class AdditionTimingOffsetConfigEntry extends EnumConfigEntry<AdditionTimingOffset> {
  public AdditionTimingOffsetConfigEntry() {
    super(AdditionTimingOffset.class, AdditionTimingOffset.NORMAL, ConfigStorageLocation.CAMPAIGN, ConfigCategory.GAMEPLAY);
  }
}
