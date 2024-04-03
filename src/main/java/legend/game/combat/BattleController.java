package legend.game.combat;

import legend.game.combat.bent.BattleEntity27c;
import legend.game.net.BattleAction;
import legend.game.scripting.ScriptState;

public interface BattleController {
  void startTurn(final ScriptState<? extends BattleEntity27c> bent);
  void action(final BattleAction action);
  boolean canTakeAction();
  BattleAction getAction();
}
