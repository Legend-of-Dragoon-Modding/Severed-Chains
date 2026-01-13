package legend.game.modding.events.engine;

import legend.game.EngineState;

public interface EngineStateEvent<T extends EngineState> {
  T getEngineState();
}
