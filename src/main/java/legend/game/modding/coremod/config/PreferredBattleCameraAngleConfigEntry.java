package legend.game.modding.coremod.config;

import legend.game.combat.PreferredBattleCameraAngle;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigStorageLocation;
import legend.game.saves.EnumConfigEntry;

public class PreferredBattleCameraAngleConfigEntry extends EnumConfigEntry<PreferredBattleCameraAngle> {
  public PreferredBattleCameraAngleConfigEntry() {
    super(PreferredBattleCameraAngle.class, PreferredBattleCameraAngle.NORMAL, ConfigStorageLocation.CAMPAIGN, ConfigCategory.GAMEPLAY);
  }
}
