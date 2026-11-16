package legend.game.modding.coremod.config;

import legend.game.combat.AdditionCounterAmount;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigStorageLocation;
import legend.game.saves.EnumConfigEntry;

public class AdditionCounterAmountConfigEntry extends EnumConfigEntry<AdditionCounterAmount> {
  public AdditionCounterAmountConfigEntry() {
    super(AdditionCounterAmount.class, AdditionCounterAmount.NORMAL, ConfigStorageLocation.CAMPAIGN, ConfigCategory.GAMEPLAY);
  }
}
