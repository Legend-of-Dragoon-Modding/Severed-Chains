package legend.game.types;

import legend.core.IoHelper;
import legend.game.combat.deff.Anim;

public class TmdAnimationFile extends Anim {
  /** ushort */
  public final int modelPartCount_0c;
  public final short _0e;
  public final ModelPartTransforms0c[] partTransforms_10;

  public TmdAnimationFile(final byte[] data, final int offset) {
    super(data, offset);

    this.modelPartCount_0c = IoHelper.readUShort(data, offset + 0xc);
    this._0e = IoHelper.readShort(data, offset + 0xe);
    this.partTransforms_10 = new ModelPartTransforms0c[this.modelPartCount_0c];

    for(int i = 0; i < this.modelPartCount_0c; i++) {
      this.partTransforms_10[i] = new ModelPartTransforms0c(data, offset + 0x10 + i * 0xc);
    }
  }
}
