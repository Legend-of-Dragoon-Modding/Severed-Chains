package legend.game.combat.effects;

import legend.core.gte.VECTOR;

import java.util.Arrays;

public class WeaponTrailEffectSegment2c {
  public final int index;

  /** ubytes */
  public int unused_00;
  public int unused_01;
  public int unused_02;
  public boolean _03;
  public final VECTOR[] endpointCoords_04 = new VECTOR[2];
  public WeaponTrailEffectSegment2c previousSegmentRef_24;
  public WeaponTrailEffectSegment2c nextSegmentRef_28;

  public WeaponTrailEffectSegment2c(final int index) {
    this.index = index;
    Arrays.setAll(this.endpointCoords_04, value -> new VECTOR());
  }
}
