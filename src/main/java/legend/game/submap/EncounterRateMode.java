package legend.game.submap;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;

public enum EncounterRateMode {
  RETAIL("Retail", i -> i, 1.0f, 4.0f, 1.0f, 2.0f),
  AVERAGE("Average", Math::abs, 1.0f, 4.0f, 1.0f, 2.0f),
  INTENDED("Intended", Math::abs, 1.0f, 4.0f, 1.0f, 2.0f),
  NONE("None", i -> i, 0.0f, 0.0f, 1.0f, 2.0f),
  ;

  public final String name;
  private final Int2IntFunction distanceModifier;
  public final float walkModifier;
  public final float runModifier;
  public final float worldMapWalkModifier;
  public final float worldMapRunModifier;

  EncounterRateMode(final String name, final Int2IntFunction distanceModifier, final float walkModifier, final float runModifier, final float worldMapWalkModifier, final float worldMapRunModifier) {
    this.name = name;
    this.distanceModifier = distanceModifier;
    this.walkModifier = walkModifier;
    this.runModifier = runModifier;
    this.worldMapWalkModifier = worldMapWalkModifier;
    this.worldMapRunModifier = worldMapRunModifier;
  }

  public int modifyDistance(final int distance) {
    return this.distanceModifier.applyAsInt(distance);
  }
}
