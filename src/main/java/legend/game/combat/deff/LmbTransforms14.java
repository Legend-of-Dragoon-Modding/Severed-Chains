package legend.game.combat.deff;

import legend.core.gte.SVECTOR;
import legend.game.unpacker.FileData;
import org.joml.Vector3f;

public class LmbTransforms14 {
  public final SVECTOR scale_00 = new SVECTOR();
  public final SVECTOR trans_06 = new SVECTOR();
  public final Vector3f rot_0c = new Vector3f();

  public LmbTransforms14() { }

  public LmbTransforms14(final FileData data) {
    data.readSvec3(0x0, this.scale_00);
    data.readSvec3(0x6, this.trans_06);
    data.readSvec3Rotation(0xc, this.rot_0c);
  }

  public LmbTransforms14 set(final LmbTransforms14 other) {
    this.scale_00.set(other.scale_00);
    this.trans_06.set(other.trans_06);
    this.rot_0c.set(other.rot_0c);
    return this;
  }
}
