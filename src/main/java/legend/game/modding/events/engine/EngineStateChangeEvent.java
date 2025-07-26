package legend.game.modding.events.engine;

import legend.game.EngineState;
import legend.game.EngineStateEnum;

import javax.annotation.Nullable;

public class EngineStateChangeEvent extends EngineEvent {
  @Nullable
  public final EngineStateEnum oldEngineState;
  public final EngineStateEnum newEngineState;
  public final EngineState engineState;

  public EngineStateChangeEvent(@Nullable final EngineStateEnum oldEngineState, final EngineStateEnum newEngineState, final EngineState engineState) {
    this.oldEngineState = oldEngineState;
    this.newEngineState = newEngineState;
    this.engineState = engineState;
  }
}
