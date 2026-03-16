package legend.game.combat.ui;

import legend.game.combat.Battle;
import legend.game.combat.bent.PlayerBattleEntity;
import org.legendofdragoon.modloader.registries.RegistryEntry;

public abstract class BattleAction extends RegistryEntry {
  /** Called when the player clicks on this action */
  public BattleActionUseFlowControl use(final Battle battle, final PlayerBattleEntity player) {
    return BattleActionUseFlowControl.CONTINUE_SCRIPT;
  }

  /** Once the action is used, called once per frame until */
  public BattleActionTickFlowControl tick(final Battle battle, final PlayerBattleEntity player) {
    return BattleActionTickFlowControl.CONTINUE_SCRIPT;
  }

  public abstract void draw(final Battle battle, final int index, final boolean selected);
}
