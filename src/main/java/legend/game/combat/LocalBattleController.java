package legend.game.combat;

import legend.game.combat.bent.BattleEntity27c;
import legend.game.scripting.ScriptState;

public class LocalBattleController implements BattleController {
  @Override
  public void startTurn(final ScriptState<? extends BattleEntity27c> bent) {

  }

  @Override
  public boolean canTakeAction() {
    return true;
  }
}
