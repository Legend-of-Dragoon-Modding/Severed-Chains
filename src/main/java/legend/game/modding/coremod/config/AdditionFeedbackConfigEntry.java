package legend.game.modding.coremod.config;

import legend.game.combat.ui.AdditionFeedback;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigStorageLocation;
import legend.game.saves.EnumConfigEntry;

public class AdditionFeedbackConfigEntry extends EnumConfigEntry<AdditionFeedback> {
  public AdditionFeedbackConfigEntry() {
    super(AdditionFeedback.class, AdditionFeedback.NONE, ConfigStorageLocation.CAMPAIGN, ConfigCategory.GAMEPLAY);
  }
}
