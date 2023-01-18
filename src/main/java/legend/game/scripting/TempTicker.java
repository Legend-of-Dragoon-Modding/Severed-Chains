package legend.game.scripting;

public interface TempTicker<T> {
  boolean run(final ScriptState<T> state, final T obj);
}
