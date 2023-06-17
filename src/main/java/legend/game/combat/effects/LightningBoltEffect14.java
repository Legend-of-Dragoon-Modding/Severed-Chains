package legend.game.combat.effects;

import legend.core.gte.SVECTOR;

public class LightningBoltEffect14 {
  public final int index;

  /** ubyte */
  public int unused_00;

  /** short */
  public int angle_02;
  public final SVECTOR rotation_04 = new SVECTOR();
  /** Average z-index */
  public int sz3_0c;
  public LightningBoltEffectSegment30[] boltSegments_10;

  public LightningBoltEffect14(final int index) {
    this.index = index;
  }
}
