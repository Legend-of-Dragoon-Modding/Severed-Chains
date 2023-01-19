package legend.game.types;

import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.SVECTOR;

public class WMapStruct258Sub60 {
  public final GsCOORDINATE2 coord2_00 = new GsCOORDINATE2();
  public final SVECTOR rotation_50 = new SVECTOR();
  /** short */
  public int _58;
  /** short */
  public int _5a;
  /** short */
  public int _5c;
  /** byte */
  public int _5e;

  public void set(final WMapStruct258Sub60 other) {
    this.coord2_00.set(other.coord2_00);
    this.rotation_50.set(other.rotation_50);
    this._58 = other._58;
    this._5a = other._5a;
    this._5c = other._5c;
    this._5e = other._5e;
  }
}
