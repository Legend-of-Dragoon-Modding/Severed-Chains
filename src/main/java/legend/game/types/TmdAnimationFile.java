package legend.game.types;

import legend.game.combat.deff.Anim;
import legend.game.unpacker.FileData;

public class TmdAnimationFile extends Anim {
  /** ushort */
  public final int modelPartCount_0c;
  /** The total number of frames (seems there is one keyframe every two frames */
  public final short totalFrames_0e;
  /** Arrays are [keyframe][part] */
  public final ModelPartTransforms0c[][] partTransforms_10;

  public TmdAnimationFile(final FileData data) {
    super(data);

    this.modelPartCount_0c = data.readUShort(0xc);
    this.totalFrames_0e = data.readShort(0xe);

    final int keyframeCount = this.totalFrames_0e / 2;

    this.partTransforms_10 = new ModelPartTransforms0c[keyframeCount][];

    for(int frameIndex = 0; frameIndex < keyframeCount; frameIndex++) {
      this.partTransforms_10[frameIndex] = new ModelPartTransforms0c[this.modelPartCount_0c];

      for(int partIndex = 0; partIndex < this.modelPartCount_0c; partIndex++) {
        this.partTransforms_10[frameIndex][partIndex] = new ModelPartTransforms0c(data.slice(0x10 + (frameIndex * this.modelPartCount_0c + partIndex) * 0xc, 0xc));
      }
    }
  }
}
