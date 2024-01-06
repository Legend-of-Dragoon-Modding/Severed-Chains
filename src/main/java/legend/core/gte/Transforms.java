package legend.core.gte;

import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Transforms {
  public final Vector3f scale = new Vector3f();
  public final Vector3f rotate = new Vector3f();
  public final Vector3f trans = new Vector3f();
  public final Quaternionf quat = new Quaternionf();

  public Transforms set(final Transforms other) {
    this.scale.set(other.scale);
    this.rotate.set(other.rotate);
    this.trans.set(other.trans);
    return this;
  }
}
