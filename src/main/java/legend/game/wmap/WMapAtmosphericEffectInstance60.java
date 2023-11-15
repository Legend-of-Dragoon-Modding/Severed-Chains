package legend.game.wmap;

import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.MV;
import legend.core.opengl.MeshObj;

public class WMapAtmosphericEffectInstance60 {
  public static final int[] snowUs = {32, 40, 32, 40, 32, 40};
  public static final int[] snowVs = {16, 16, 24, 24, 24, 16};

  public MeshObj obj;
  public final MV transforms = new MV();
  public float queueZ;

  public final GsCOORDINATE2 coord2_00 = new GsCOORDINATE2();
  /** Originally vectro rotation_50 */
  public int snowUvIndex_50;
  /** short */
  public int x_58;
  /** short */
  public int y_5a;
  /** short */
  public float brightness_5c;
  /** byte */
  public int z_5e;

  public void set(final WMapAtmosphericEffectInstance60 other) {
    this.coord2_00.set(other.coord2_00);
    this.snowUvIndex_50 = other.snowUvIndex_50;
    this.x_58 = other.x_58;
    this.y_5a = other.y_5a;
    this.brightness_5c = other.brightness_5c;
    this.z_5e = other.z_5e;
  }
}
