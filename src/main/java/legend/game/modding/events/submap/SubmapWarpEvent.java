package legend.game.modding.events.submap;

import legend.game.modding.events.engine.InGameEvent;
import legend.game.submap.SMap;
import legend.game.submap.Submap;
import legend.game.types.GameState52c;

public class SubmapWarpEvent extends InGameEvent<SMap> implements LoadedSubmapEvent {
  private final Submap submap;
  public final int submapCut;

  public SubmapWarpEvent(final SMap engineState, final GameState52c gameState, final Submap submap, final int submapCut) {
    super(engineState, gameState);
    this.submap = submap;
    this.submapCut = submapCut;
  }

  @Override
  public Submap getSubmap() {
    return this.submap;
  }
}
