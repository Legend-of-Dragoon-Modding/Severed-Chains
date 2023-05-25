package legend.game.modding.events.battle;

import legend.game.combat.bobj.BattleObject27c;
import legend.game.scripting.ScriptState;

public class BattleObjectTurnEvent<T extends BattleObject27c> extends BattleStateEvent {
  public final ScriptState<T> state;
  public final T bobj;

  public BattleObjectTurnEvent(final ScriptState<T> state) {
    this.state = state;
    this.bobj = state.innerStruct_00;
  }
}
