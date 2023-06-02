package legend.game.combat.formula;

public interface Operation<InputType, ResultType> {
  ResultType apply(final State<InputType> state);
}
