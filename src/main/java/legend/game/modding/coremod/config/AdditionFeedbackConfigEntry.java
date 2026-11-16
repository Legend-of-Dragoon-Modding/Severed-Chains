package legend.game.modding.coremod.config;

import legend.game.combat.SEffe;
import legend.game.combat.ui.AdditionFeedback;
import legend.game.combat.ui.AdditionFeedbackText;
import legend.game.modding.coremod.CoreMod;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigStorageLocation;
import legend.game.saves.EnumConfigEntry;

import static legend.core.GameEngine.CONFIG;
import static legend.game.Scus94491BpeSegment_8006.battleState_8006e398;

public class AdditionFeedbackConfigEntry extends EnumConfigEntry<AdditionFeedback> {
  public AdditionFeedbackConfigEntry() {
    super(AdditionFeedback.class, AdditionFeedback.NONE, ConfigStorageLocation.CAMPAIGN, ConfigCategory.GAMEPLAY, AdditionFeedbackConfigEntry::additionFeedbackConfigCallback);
  }

  public static void additionFeedbackConfigCallback() {
    if(battleState_8006e398 != null) {
      SEffe.additionFeedbackText = CONFIG.getConfig(CoreMod.ADDITION_FEEDBACK_CONFIG.get()) == AdditionFeedback.FEEDBACK ? new AdditionFeedbackText() : null;
    } else {
      SEffe.additionFeedbackText = null;
    }
  }
}
