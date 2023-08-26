package legend.game.combat.formula;

import legend.game.combat.bent.BattleEntity27c;

import java.util.EnumMap;
import java.util.Map;

public class State<InputType> {
  public final Map<Side, BattleEntity27c> bents = new EnumMap<>(Side.class);

  InputType value;

  public State(final BattleEntity27c attacker, final BattleEntity27c defender) {
    this.bents.put(Side.ATTACKER, attacker);
    this.bents.put(Side.DEFENDER, defender);
  }

  public InputType value() {
    return this.value;
  }
}
