package legend.game.combat.deff;

import legend.core.IoHelper;
import legend.core.gte.BVEC4;
import legend.game.types.TmdAnimationFile;

import java.util.Arrays;

public class Cmb extends TmdAnimationFile {
  public static final int MAGIC = 0x2042_4d43;

  public final SubTransforms08[] subTransforms;

  public Cmb(final byte[] data, final int offset) {
    super(data, offset);

    final int baseAddress = offset + 0x10 + this.modelPartCount_0c * 0xc;
    final int count = (data.length - baseAddress) / 0x8;
    this.subTransforms = new SubTransforms08[count];
    Arrays.setAll(this.subTransforms, i -> new SubTransforms08(data, baseAddress + i * 0x8));
  }

  public static class SubTransforms08 {
    /** ubyte */
    public final int rotScale_00;
    public final BVEC4 rot_01 = new BVEC4();
    /** ubyte */
    public final int transScale_04;
    public final BVEC4 trans_05 = new BVEC4();

    public SubTransforms08(final byte[] data, final int offset) {
      this.rotScale_00 = IoHelper.readUByte(data, offset);
      IoHelper.readBvec3(data, offset + 1, this.rot_01);
      this.transScale_04 = IoHelper.readUByte(data, offset + 4);
      IoHelper.readBvec3(data, offset + 5, this.trans_05);
    }
  }
}
