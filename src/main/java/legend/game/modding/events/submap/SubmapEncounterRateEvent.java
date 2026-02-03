package legend.game.modding.events.submap;

import legend.game.modding.events.engine.InGameEvent;
import legend.game.submap.SMap;
import legend.game.submap.Submap;
import legend.game.types.GameState52c;

public class SubmapEncounterRateEvent extends InGameEvent<SMap> implements LoadedSubmapEvent {
  private final Submap submap;
  public int encounterRate;
  public final int submapId;

  public SubmapEncounterRateEvent(final SMap engineState, final GameState52c gameState, final Submap submap, final int encounterRate, final int submapId) {
    super(engineState, gameState);
    this.submap = submap;
    this.encounterRate = encounterRate;
    this.submapId = submapId;
  }

  @Override
  public Submap getSubmap() {
    return this.submap;
  }
}
