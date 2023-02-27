package legend.game.combat.deff;

import legend.core.gte.BVEC4;
import legend.game.types.TmdAnimationFile;
import legend.game.unpacker.FileData;

import java.util.Arrays;

public class Cmb extends TmdAnimationFile {
  public static final int MAGIC = 0x2042_4d43;

  public final int size_08;

  public final SubTransforms08[] subTransforms;

  public Cmb(final FileData data) {
    super(data);

    if(data.readInt(0) != MAGIC) {
      throw new RuntimeException("Not a CMB! Magic: %x".formatted(data.readInt(0)));
    }

    this.size_08 = data.readUShort(0x8);

    final int baseAddress = 0x10 + this.modelPartCount_0c * 0xc;
    final int count = this.modelPartCount_0c * (this.totalFrames_0e - 1);
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
