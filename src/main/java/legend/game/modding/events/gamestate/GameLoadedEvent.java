package legend.game.modding.events.gamestate;

import legend.game.types.GameState52c;

/** Called when a save is loaded or a new game is started (after {@link NewGameEvent}) */
public class GameLoadedEvent extends GameStateEvent {
  public GameLoadedEvent(final GameState52c gameState) {
    super(gameState);
  }
}
