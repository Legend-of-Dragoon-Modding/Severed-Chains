package legend.game.scripting;

import legend.game.types.ScriptState;

public interface TempTicker<T> {
  boolean run(final int scriptIndex, final ScriptState<T> state, final T obj);
}
