package legend.game.modding.coremod.config;

import legend.game.combat.BattleTransitionMode;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigStorageLocation;
import legend.game.saves.EnumConfigEntry;

public class BattleTransitionModeConfigEntry extends EnumConfigEntry<BattleTransitionMode> {
  public BattleTransitionModeConfigEntry() {
    super(BattleTransitionMode.class, BattleTransitionMode.NORMAL, ConfigStorageLocation.CAMPAIGN, ConfigCategory.GAMEPLAY);
  }
}
