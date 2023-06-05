package legend.game.combat.formula;

public class Step {
  private final Operation op;
  Step next;

  public Step(final Operation op) {
    this.op = op;
  }

  public Object apply(final State state) {
    return this.op.apply(state);
  }

  public Step next() {
    return this.next;
  }
}
