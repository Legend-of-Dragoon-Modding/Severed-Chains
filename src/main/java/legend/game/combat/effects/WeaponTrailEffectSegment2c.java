package legend.game.combat.effects;

import org.joml.Vector3f;

import java.util.Arrays;

public class WeaponTrailEffectSegment2c {
  public final int index;

  public boolean _03;
  public final Vector3f[] endpointCoords_04 = new Vector3f[2];
  public WeaponTrailEffectSegment2c previousSegmentRef_24;
  public WeaponTrailEffectSegment2c nextSegmentRef_28;

  public WeaponTrailEffectSegment2c(final int index) {
    this.index = index;
    Arrays.setAll(this.endpointCoords_04, value -> new Vector3f());
  }
}
