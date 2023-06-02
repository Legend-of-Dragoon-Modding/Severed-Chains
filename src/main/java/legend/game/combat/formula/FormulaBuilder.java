package legend.game.combat.formula;

public class FormulaBuilder<InputType> {
  Step root;
  private Step current;

  public <ResultType> FormulaBuilder(final Operation<InputType, ResultType> initialOperation) {
    final Step step = new Step(initialOperation);
    this.root = step;
    this.current = step;
  }

  public <ResultType> FormulaBuilder<ResultType> then(final Operation<InputType, ResultType> operation) {
    this.current.next = new Step(operation);
    this.current = this.current.next;
    return (FormulaBuilder<ResultType>)this;
  }
}
