package legend.game.combat.types;

import legend.core.gte.SVECTOR;
import legend.core.gte.VECTOR;

public class EffectManagerData6cInner {
  public int flags_00;
  public final VECTOR trans_04 = new VECTOR();
  public final SVECTOR rot_10 = new SVECTOR();
  public final SVECTOR scale_16 = new SVECTOR();
  public final SVECTOR colour_1c = new SVECTOR();
  public int z_22;
  /** TODO I dunno what's going on here, seems sometimes the end of this struct is different */
  public int _24;
  public int _28;
  public int _2c;
  public int _30;

  public void set(final EffectManagerData6cInner other) {
    this.flags_00 = other.flags_00;
    this.trans_04.set(other.trans_04);
    this.rot_10.set(other.rot_10);
    this.scale_16.set(other.scale_16);
    this.colour_1c.set(other.colour_1c);
    this.z_22 = other.z_22;
    this._24 = other._24;
    this._28 = other._28;
    this._2c = other._2c;
    this._30 = other._30;
  }
}
