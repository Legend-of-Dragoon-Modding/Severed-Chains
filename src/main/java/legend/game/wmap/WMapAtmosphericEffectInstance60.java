package legend.game.wmap;

import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.MV;
import legend.core.opengl.MeshObj;
import org.joml.Vector3i;

public class WMapAtmosphericEffectInstance60 {
  public static final int[] snowUs = {32, 40, 32, 40, 32, 40};
  public static final int[] snowVs = {16, 16, 24, 24, 24, 16};

  public MeshObj obj;
  public final MV transforms = new MV();
  public float queueZ;

  public final GsCOORDINATE2 coord2_00 = new GsCOORDINATE2();
  /** Originally vectro rotation_50 */
  public int snowUvIndex_50;
  /** Was short x_58, short y_5a, byte z_5e */
  public final Vector3i translation_58 = new Vector3i();
  /** short */
  public float brightness_5c;

  public void set(final WMapAtmosphericEffectInstance60 other) {
    this.coord2_00.set(other.coord2_00);
    this.snowUvIndex_50 = other.snowUvIndex_50;
    this.translation_58.set(other.translation_58);
    this.brightness_5c = other.brightness_5c;
  }
}
