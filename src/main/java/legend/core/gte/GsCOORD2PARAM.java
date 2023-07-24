package legend.core.gte;

import org.joml.Vector3f;

public class GsCOORD2PARAM {
  public final Vector3f scale = new Vector3f();
  public final Vector3f rotate = new Vector3f();
  public final VECTOR trans = new VECTOR();

  public GsCOORD2PARAM set(final GsCOORD2PARAM other) {
    this.scale.set(other.scale);
    this.rotate.set(other.rotate);
    this.trans.set(other.trans);
    return this;
  }
}
