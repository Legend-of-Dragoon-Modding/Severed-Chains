package legend.game.combat.ui;

import legend.game.combat.bent.BattleEntity27c;
import legend.game.scripting.ScriptState;

public class TurnOrder {
  public final ScriptState<? extends BattleEntity27c> state;
  public int turnValue;

  public TurnOrder(final ScriptState<? extends BattleEntity27c> state) {
    this.state = state;
    this.turnValue = state.innerStruct_00.turnValue_4c;
  }
}
