package legend.lodmod.items;

import legend.game.combat.bent.BattleEntity27c;
import legend.game.scripting.FlowControl;
import legend.game.scripting.ScriptState;

public class ShieldItem extends BuffItem {
  private int deffLoadingStage;

  public ShieldItem(final int useItemEntrypoint, final boolean physicalImmunity, final boolean magicalImmunity) {
    super(useItemEntrypoint, 42, 200, TargetType.ALLIES, 0, 0, 0, 0, 0, 0, 0, 0, physicalImmunity, magicalImmunity, 0, 0, 0, 0, 0, 0);
  }

  @Override
  public FlowControl useInBattle(final ScriptState<BattleEntity27c> user, final int targetBentIndex) {
    return switch(this.deffLoadingStage) {
      // Initial load
      case 0 -> {
        this.deffLoadingStage = 1;

        this.injectScript(user, this.getUseItemScriptPath(), this.getUseItemScriptEntrypoint(), () -> {
          this.useItemScriptLoaded(user, targetBentIndex);
          this.deffLoadingStage = 2;
        });

        yield FlowControl.PAUSE_AND_REWIND;
      }

      // Wait for load
      case 1 -> FlowControl.PAUSE_AND_REWIND;

      // Loaded, carry on
      default -> {
        this.deffLoadingStage = 0;
        yield FlowControl.CONTINUE;
      }
    };
  }
}
