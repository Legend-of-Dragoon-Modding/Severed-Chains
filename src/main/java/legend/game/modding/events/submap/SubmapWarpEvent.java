package legend.game.modding.events.submap;

import legend.game.types.GameState52c;

public class SubmapWarpEvent extends SubmapEvent {
  public final int submapCut;
  public final GameState52c gameState;

  public SubmapWarpEvent(final int submapCut, final GameState52c gamestate) {
    this.submapCut = submapCut;
    this.gameState = gamestate;
  }
}