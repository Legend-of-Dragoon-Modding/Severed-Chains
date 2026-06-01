package legend.game.modding.events.submap;

import legend.game.modding.events.engine.InGameEvent;
import legend.game.submap.SMap;
import legend.game.submap.Submap;
import legend.game.types.GameState52c;

public class SubmapEncounterAccumulatorEvent extends InGameEvent<SMap> implements LoadedSubmapEvent {
  private final Submap submap;
  public final float encounterAccumulator;
  public float encounterAccumulatedStep;
  public final float encounterMultiplier;
  public final int vsyncMode;
  public final int encounterAccumulatorLimit;
  public final float encounterAccumulatorStepModifier;

  public SubmapEncounterAccumulatorEvent(final SMap engineState, final GameState52c gameState, final Submap submap, final float encounterAccumulator, final float encounterAccumulatedStep, final float encounterMultiplier, final int vsyncMode, final int encounterAccumulatorLimit, final float encounterAccumulatorStepModifier) {
    super(engineState, gameState);
    this.submap = submap;
    this.encounterAccumulator = encounterAccumulator;
    this.encounterAccumulatedStep = encounterAccumulatedStep;
    this.encounterMultiplier = encounterMultiplier;
    this.vsyncMode = vsyncMode;
    this.encounterAccumulatorLimit = encounterAccumulatorLimit;
    this.encounterAccumulatorStepModifier = encounterAccumulatorStepModifier;
  }

  @Override
  public Submap getSubmap() {
    return this.submap;
  }
}
