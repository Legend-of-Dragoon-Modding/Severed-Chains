package legend.game.modding.events.engine;

import legend.game.EngineState;
import legend.game.types.GameState52c;
import org.legendofdragoon.modloader.events.Event;

public class InGameEvent<T extends EngineState> extends Event implements EngineStateEvent<T> {
  private final T engineState;
  private final GameState52c gameState;

  public InGameEvent(final T engineState, final GameState52c gameState) {
    this.engineState = engineState;
    this.gameState = gameState;
  }

  @Override
  public T getEngineState() {
    return this.engineState;
  }

  public GameState52c getGameState() {
    return this.gameState;
  }
}
