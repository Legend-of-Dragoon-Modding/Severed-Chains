package legend.game.combat.effects;

import legend.game.types.Model124;
import org.joml.Vector3f;

import java.util.Arrays;

public class WeaponTrailEffect3c implements Effect {
  public int currentSegmentIndex_00;
  public int parentIndex_04;
  public int dobjIndex_08;
  public int largestVertexIndex_0a;
  public int smallestVertexIndex_0c;
  /**
   * ubyte
   */
  public int segmentCount_0e;
  public final Vector3f largestVertex_10 = new Vector3f();
  public final Vector3f smallestVertex_20 = new Vector3f();
  public Model124 parentModel_30;
  public WeaponTrailEffectSegment2c[] segments_34 = new WeaponTrailEffectSegment2c[65];
  public WeaponTrailEffectSegment2c currentSegment_38;

  public WeaponTrailEffect3c() {
    Arrays.setAll(this.segments_34, WeaponTrailEffectSegment2c::new);
  }
}
