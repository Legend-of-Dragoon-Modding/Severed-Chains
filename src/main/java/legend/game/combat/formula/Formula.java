package legend.game.combat.formula;

import legend.game.combat.bent.BattleEntity27c;

import java.util.List;
import java.util.function.Consumer;

public class Formula<InitialType, TerminalType> {
  public static <InitialType, ResultType> Formula<InitialType, ResultType> make(final Operation<InitialType, ResultType> initialOperation, final Consumer<FormulaBuilder<InitialType>> builder) {
    final FormulaBuilder<InitialType> b = new FormulaBuilder<InitialType>(initialOperation);
    builder.accept(b);
    return new Formula<>(b.root);
  }

  private final Step root;

  private Formula(final Step root) {
    this.root = root;
  }

  public TerminalType[] calculate(final BattleEntity27c attacker, final List<BattleEntity27c> defenders) {
    final State[] states = new State[defenders.size()];

    for(int i = 0; i < states.length; i++) {
      final State state = new State(attacker, defenders.get(i));
      states[i] = state;
      states[i].value = 0;
    }

    Step current = this.root;
    while(current != null) {
      for(final State state : states) {
        state.value = current.apply(state);
      }

      current = current.next();
    }

    final TerminalType[] results = (TerminalType[])new Object[states.length];
    for(int i = 0; i < states.length; i++) {
      results[i] = (TerminalType)states[i].value;
    }

    return results;
  }

  public TerminalType calculate(final BattleEntity27c attacker, final BattleEntity27c defender) {
    return this.calculate(attacker, List.of(defender))[0];
  }
}
