package legend.game.types;

import legend.core.memory.Method;
import legend.game.combat.deff.Anim;
import legend.game.unpacker.FileData;

import static legend.game.Scus94491BpeSegment_8002.applyInterpolationFrame;
import static legend.game.Scus94491BpeSegment_8002.applyModelPartTransforms;
import static legend.game.Scus94491BpeSegment_8002.loadModelStandardAnimation;

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

  @Override
  public void loadIntoModel(final Model124 model) {
    loadModelStandardAnimation(model, this);
  }

  @Override
  @Method(0x800dd4ccL)
  public void apply(final Model124 model, final int animationTicks) {
    if(model.animationState_9c == 2) {
      return;
    }

    //LAB_800dd4fc
    final int totalFrames;
    final int frame;
    if(model.disableInterpolation_a2) {
      totalFrames = model.totalFrames_9a / 2;
      frame = animationTicks % totalFrames;
      model.currentKeyframe_94 = frame;
      applyModelPartTransforms(model);
    } else {
      //LAB_800dd568
      totalFrames = model.totalFrames_9a;
      frame = animationTicks % totalFrames;
      model.currentKeyframe_94 = frame / 2;

      if((frame & 0x1) != 0 && frame != model.totalFrames_9a - 1 && model.ub_a3 == 0) { // Interpolation frame
        applyInterpolationFrame(model, 1);
      } else {
        applyModelPartTransforms(model);
      }
    }

    //LAB_800dd5f0
    model.currentKeyframe_94++;
    model.remainingFrames_9e = totalFrames - frame - 1;
    model.subFrameIndex = 0;

    if(model.remainingFrames_9e == 0) {
      model.animationState_9c = 0;
    } else {
      //LAB_800dd618
      model.animationState_9c = 1;
    }
  }
}
