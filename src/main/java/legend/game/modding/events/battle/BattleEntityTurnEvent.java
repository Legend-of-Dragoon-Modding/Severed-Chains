package legend.game.modding.events.battle;

import legend.game.combat.Battle;
import legend.game.combat.bent.BattleEntity27c;
import legend.game.combat.encounters.Encounter;
import legend.game.scripting.ScriptState;

public class BattleEntityTurnEvent<T extends BattleEntity27c> extends BattleStateEvent {
  public final ScriptState<T> state;
  public final T bent;

  public BattleEntityTurnEvent(final Battle battle, final Encounter encounter, final ScriptState<T> state) {
    super(battle, encounter);
    this.state = state;
    this.bent = state.innerStruct_00;
  }
}
