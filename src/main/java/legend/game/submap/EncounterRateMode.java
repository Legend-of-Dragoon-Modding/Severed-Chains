package legend.game.submap;

import it.unimi.dsi.fastutil.floats.Float2FloatFunction;

public enum EncounterRateMode {
  /** Retail doesn't abs the values so running up/right gave 1/4 the intended encounters */
  RETAIL(i -> i, 1.0f, 4.0f, 1.0f, 2.0f),
  /** Averages up/right and down/left values when running to give the overall average number of encounters */
  AVERAGE(Math::abs, 1.0f, 2.5f, 1.0f, 2.0f),
  /** The encounter rate the LOD devs intended (a lot) */
  INTENDED(Math::abs, 1.0f, 4.0f, 1.0f, 2.0f),
  /** No encounters at all */
  NONE(i -> i, 0.0f, 0.0f, 0.0f, 0.0f),
  ;

  private final Float2FloatFunction distanceModifier;
  public final float walkModifier;
  public final float runModifier;
  public final float worldMapWalkModifier;
  public final float worldMapRunModifier;

  EncounterRateMode(final Float2FloatFunction distanceModifier, final float walkModifier, final float runModifier, final float worldMapWalkModifier, final float worldMapRunModifier) {
    this.distanceModifier = distanceModifier;
    this.walkModifier = walkModifier;
    this.runModifier = runModifier;
    this.worldMapWalkModifier = worldMapWalkModifier;
    this.worldMapRunModifier = worldMapRunModifier;
  }

  public float modifyDistance(final float distance) {
    return this.distanceModifier.get(distance);
  }
}
