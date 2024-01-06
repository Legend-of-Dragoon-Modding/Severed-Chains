package legend.game.types;

import legend.game.unpacker.FileData;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Keyframe0c {
  public final Vector3f rotate_00 = new Vector3f();
  public final Vector3f translate_06 = new Vector3f();

  public final Quaternionf quat = new Quaternionf();

  public Keyframe0c() { }

  public Keyframe0c(final FileData data) {
    data.readSvec3Rotation(0x0, this.rotate_00);
    data.readSvec3_0(0x6, this.translate_06);

    this.quat.rotationZYX(this.rotate_00.z, this.rotate_00.y, this.rotate_00.x);
  }
}
