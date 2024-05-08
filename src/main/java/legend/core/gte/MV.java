package legend.core.gte;

import org.joml.Matrix3f;
import org.joml.Vector3f;

public class MV extends Matrix3f {
  public final Vector3f transfer = new Vector3f();

  public MV() {

  }

  public MV(final MV mv) {
    super(mv);
    this.transfer.set(mv.transfer);
  }

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

  public MV rotationYXZ(final Vector3f rotation) {
    this.rotationYXZ(rotation.y, rotation.x, rotation.z);
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

  /** Rotate with parallel translation */
  public MV compose(final MV other, final MV out) {
    final float vx = other.transfer.x;
    final float vy = other.transfer.y;
    final float vz = other.transfer.z;
    this.transfer.mul(other, out.transfer);
    out.transfer.add(vx, vy, vz);

    other.mul(this, out);
    return out;
  }

  /** Rotate with parallel translation */
  public MV compose(final MV rotation) {
    return this.compose(rotation, this);
  }
}
