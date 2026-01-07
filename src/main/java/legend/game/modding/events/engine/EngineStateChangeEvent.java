package legend.game.modding.events.engine;

import legend.game.EngineState;
import legend.game.EngineStateType;

import javax.annotation.Nullable;

public class EngineStateChangeEvent extends EngineEvent {
  @Nullable
  public final EngineStateType<?> oldEngineState;
  public final EngineState<?> engineState;

  public EngineStateChangeEvent(@Nullable final EngineStateType<?> oldEngineState, final EngineState<?> engineState) {
    this.oldEngineState = oldEngineState;
    this.engineState = engineState;
  }
}
