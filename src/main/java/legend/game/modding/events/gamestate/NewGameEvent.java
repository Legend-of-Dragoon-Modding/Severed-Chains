package legend.game.modding.events.gamestate;

import legend.game.types.GameState52c;

/** Called when a new game is started (before {@link GameLoadedEvent}) */
public class NewGameEvent extends GameStateEvent {
  public NewGameEvent(final GameState52c gameState) {
    super(gameState);
  }
}
