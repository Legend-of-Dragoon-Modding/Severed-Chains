package legend.game.combat.deff;

import legend.core.gte.BVEC4;
import legend.game.types.TmdAnimationFile;
import legend.game.unpacker.FileData;

import java.util.Arrays;

public class Cmb extends TmdAnimationFile {
  public static final int MAGIC = 0x2042_4d43;

  public final SubTransforms08[] subTransforms;

  public Cmb(final FileData data) {
    super(data);

    final int baseAddress = 0x10 + this.modelPartCount_0c * 0xc;
    final int count = (data.size() - baseAddress) / 0x8;
    this.subTransforms = new SubTransforms08[count];
    Arrays.setAll(this.subTransforms, i -> new SubTransforms08(data.slice(baseAddress + i * 0x8, 0x8)));
  }

  public static class SubTransforms08 {
    /** ubyte */
    public final int rotScale_00;
    public final BVEC4 rot_01 = new BVEC4();
    /** ubyte */
    public final int transScale_04;
    public final BVEC4 trans_05 = new BVEC4();

    public SubTransforms08(final FileData data) {
      this.rotScale_00 = data.readUByte(0);
      data.readBvec3(1, this.rot_01);
      this.transScale_04 = data.readUByte(4);
      data.readBvec3(5, this.trans_05);
    }
  }
}
