package legend.game.wmap;

import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.MV;
import legend.core.opengl.MeshObj;
import org.joml.Vector3f;

public class WMapAtmosphericEffectInstance60 {
  public MeshObj obj;
  public final MV transforms = new MV();
  public float queueZ;

  public final GsCOORDINATE2 coord2_00 = new GsCOORDINATE2();
  public final Vector3f rotation_50 = new Vector3f();
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
    this.rotation_50.set(other.rotation_50);
    this.x_58 = other.x_58;
    this.y_5a = other.y_5a;
    this.brightness_5c = other.brightness_5c;
    this.z_5e = other.z_5e;
  }
}
