package legend.game.modding.coremod.config;

import legend.game.combat.effects.TransformationMode;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigStorageLocation;
import legend.game.saves.EnumConfigEntry;

public class TransformationModeConfigEntry extends EnumConfigEntry<TransformationMode> {
  public TransformationModeConfigEntry() {
    super(TransformationMode.class, TransformationMode.NORMAL, ConfigStorageLocation.CAMPAIGN, ConfigCategory.GAMEPLAY);
  }
}
