package legend.game.modding.events.gamestate;

import legend.game.modding.events.Event;
import legend.game.types.GameState52c;

public class GameStateEvent extends Event {
  public final GameState52c gameState;

  public GameStateEvent(final GameState52c gameState) {
    this.gameState = gameState;
  }
}
