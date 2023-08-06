package legend.game.wmap;

import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.SVECTOR;

public class WMapAtmosphericEffectInstance60 {
  public final GsCOORDINATE2 coord2_00 = new GsCOORDINATE2();
  public final SVECTOR rotation_50 = new SVECTOR();
  /** short */
  public int x_58;
  /** short */
  public int y_5a;
  /** short */
  public int brightness_5c;
  /** byte */
  public int z_5e;

  public void set(final WMapAtmosphericEffectInstance60 other) {
    this.coord2_00.set(other.coord2_00);
    this.rotation_50.set(other.rotation_50);
    this.x_58 = other.x_58;
    this.y_5a = other.y_5a;
    this.brightness_5c = other.brightness_5c;
    this.z_5e = other.z_5e;
  }
}
