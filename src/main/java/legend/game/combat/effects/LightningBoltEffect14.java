package legend.game.combat.effects;

import org.joml.Vector3f;

import java.util.Arrays;

public class LightningBoltEffect14 {
  public final int index;

  /** ubyte */
  public int unused_00;

  /** short */
  public float angle_02;
  public final Vector3f rotation_04 = new Vector3f();
  /** Average z-index */
  public float sz3_0c;
  public LightningBoltEffectSegment30[] boltSegments_10;

  public LightningBoltEffect14(final int index, final int count) {
    this.index = index;
    this.boltSegments_10 = new LightningBoltEffectSegment30[count];
    Arrays.setAll(this.boltSegments_10, LightningBoltEffectSegment30::new);
  }
}
