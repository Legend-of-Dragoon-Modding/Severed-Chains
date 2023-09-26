package legend.core.gte;

import org.joml.Matrix3f;
import org.joml.Vector3f;

public class MV extends Matrix3f {
  public final Vector3f transfer = new Vector3f();

  public MV set(final MV m) {
    this.set((Matrix3f)m);
    this.transfer.set(m.transfer);
    return this;
  }

  public MV rotationXYZ(final Vector3f rotation) {
    this.rotationXYZ(rotation.x, rotation.y, rotation.z);
    return this;
  }

  public MV rotationZYX(final Vector3f rotation) {
    this.rotationZYX(rotation.z, rotation.y, rotation.x);
    return this;
  }

  public MV scale(final Vector3f scale) {
    this.scale(scale.x, scale.y, scale.z);
    return this;
  }

  public MV scaleLocal(final Vector3f scale) {
    this.scaleLocal(scale.x, scale.y, scale.z);
    return this;
  }
}
