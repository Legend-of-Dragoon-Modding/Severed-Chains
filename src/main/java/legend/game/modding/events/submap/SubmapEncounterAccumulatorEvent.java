package legend.game.modding.events.submap;

public class SubmapEncounterAccumulatorEvent extends SubmapEvent {
  public final float encounterAccumulator;
  public float encounterAccumulatedStep;
  public final float encounterMultiplier;
  public final int vsyncMode;
  public final int encounterAccumulatorLimit;
  public final float encounterAccumulatorStepModifier;

  public SubmapEncounterAccumulatorEvent(final float encounterAccumulator, final float encounterAccumulatedStep, final float encounterMultiplier, final int vsyncMode, final int encounterAccumulatorLimit, final float encounterAccumulatorStepModifier) {
    this.encounterAccumulator = encounterAccumulator;
    this.encounterAccumulatedStep = encounterAccumulatedStep;
    this.encounterMultiplier = encounterMultiplier;
    this.vsyncMode = vsyncMode;
    this.encounterAccumulatorLimit = encounterAccumulatorLimit;
    this.encounterAccumulatorStepModifier = encounterAccumulatorStepModifier;
  }
}
