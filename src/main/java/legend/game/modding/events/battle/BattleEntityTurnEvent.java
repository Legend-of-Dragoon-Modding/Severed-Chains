package legend.game.modding.events.battle;

import legend.game.combat.Battle;
import legend.game.combat.bent.BattleEntity27c;
import legend.game.scripting.ScriptState;

public class BattleEntityTurnEvent<T extends BattleEntity27c> extends BattleStateEvent {
  public final ScriptState<T> state;
  public final T bent;

  public BattleEntityTurnEvent(final Battle battle, final ScriptState<T> state) {
    super(battle);
    this.state = state;
    this.bent = state.innerStruct_00;
  }
}
