package legend.game.types;

import legend.game.combat.deff.Anim;
import legend.game.unpacker.FileData;

public class TmdAnimationFile extends Anim {
  /** ushort */
  public final int modelPartCount_0c;
  /** Half the number of keyframes, twice the number of elements per model part in the transforms table. Don't know why. */
  public final short halfKeyframes_0e;
  public final ModelPartTransforms0c[] partTransforms_10;

  public TmdAnimationFile(final FileData data) {
    super(data);

    this.modelPartCount_0c = data.readUShort(0xc);
    this.halfKeyframes_0e = data.readShort(0xe);
    this.partTransforms_10 = new ModelPartTransforms0c[this.modelPartCount_0c * (this.halfKeyframes_0e / 2)];

    for(int i = 0; i < this.partTransforms_10.length; i++) {
      this.partTransforms_10[i] = new ModelPartTransforms0c(data.slice(0x10 + i * 0xc));
    }
  }
}
