package legend.game.scripting;

public interface TempTicker<T extends ScriptedObject> {
  boolean run(final ScriptState<T> state, final T obj);
}
