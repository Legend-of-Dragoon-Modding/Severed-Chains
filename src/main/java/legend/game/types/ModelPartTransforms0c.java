package legend.game.types;

import legend.core.gte.SVECTOR;
import legend.game.unpacker.FileData;

public class ModelPartTransforms0c {
  public final SVECTOR rotate_00 = new SVECTOR();
  public final SVECTOR translate_06 = new SVECTOR();

  public ModelPartTransforms0c() { }

  public ModelPartTransforms0c(final FileData data) {
    data.readSvec3(0x0, this.rotate_00);
    data.readSvec3(0x6, this.translate_06);
  }
}
