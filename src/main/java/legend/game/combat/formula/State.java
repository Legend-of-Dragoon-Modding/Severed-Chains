package legend.game.combat.formula;

import legend.game.combat.bobj.BattleObject27c;

import java.util.EnumMap;
import java.util.Map;

public class State<InputType> {
  public final Map<Side, BattleObject27c> bobjs = new EnumMap<>(Side.class);

  InputType value;

  public State(final BattleObject27c attacker, final BattleObject27c defender) {
    this.bobjs.put(Side.ATTACKER, attacker);
    this.bobjs.put(Side.DEFENDER, defender);
  }

  public InputType value() {
    return this.value;
  }
}
