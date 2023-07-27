package legend.game.types;

import legend.game.unpacker.FileData;
import org.joml.Vector3f;

public class ModelPartTransforms0c {
  public final Vector3f rotate_00 = new Vector3f();
  public final Vector3f translate_06 = new Vector3f();

  public ModelPartTransforms0c() { }

  public ModelPartTransforms0c(final FileData data) {
    data.readSvec3Rotation(0x0, this.rotate_00);
    data.readSvec3_0(0x6, this.translate_06);
  }
}
