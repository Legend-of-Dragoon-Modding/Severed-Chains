package legend.game.modding.coremod.config;

import legend.game.combat.effects.AdditionDifficulty;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigStorageLocation;
import legend.game.saves.EnumConfigEntry;

public class AdditionDifficultyConfigEntry extends EnumConfigEntry<AdditionDifficulty> {
  public AdditionDifficultyConfigEntry() {
    super(AdditionDifficulty.class, AdditionDifficulty.NORMAL, ConfigStorageLocation.CAMPAIGN, ConfigCategory.GAMEPLAY);
  }
}
