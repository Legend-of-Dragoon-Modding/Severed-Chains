package legend.game.modding.coremod.config;

import legend.game.combat.DragoonAdditionDifficulty;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigStorageLocation;
import legend.game.saves.EnumConfigEntry;

public class DragoonAdditionDifficultyConfigEntry extends EnumConfigEntry<DragoonAdditionDifficulty> {
  public DragoonAdditionDifficultyConfigEntry() {
    super(DragoonAdditionDifficulty.class, DragoonAdditionDifficulty.NORMAL, ConfigStorageLocation.CAMPAIGN, ConfigCategory.ADDITIONS);
  }
}
