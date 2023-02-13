package legend.game.types;

import legend.core.IoHelper;
import legend.core.gte.SVECTOR;

public class ModelPartTransforms0c {
  public final SVECTOR rotate_00 = new SVECTOR();
  public final SVECTOR translate_06 = new SVECTOR();

  public ModelPartTransforms0c() { }

  public ModelPartTransforms0c(final byte[] data, final int offset) {
    IoHelper.readSvec3(data, offset, this.rotate_00);
    IoHelper.readSvec3(data, offset + 0x6, this.translate_06);
  }
}
