package legend.game.types;

import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.Transforms;
import legend.core.memory.Method;
import legend.game.combat.deff.Anim;
import legend.game.unpacker.FileData;

import static legend.game.Scus94491BpeSegment_8002.applyKeyframe;
import static legend.game.Scus94491BpeSegment_8002.loadModelStandardAnimation;

public class TmdAnimationFile extends Anim {
  /** ushort */
  public final int modelPartCount_0c;
  /** The total number of frames (seems there is one keyframe every two frames */
  public final short totalFrames_0e;
  /** Arrays are [keyframe][part] */
  public final Keyframe0c[][] partTransforms_10;

  public TmdAnimationFile(final FileData data) {
    super(data);

    this.modelPartCount_0c = data.readUShort(0xc);
    this.totalFrames_0e = data.readShort(0xe);

    final int keyframeCount = this.totalFrames_0e / 2;

    this.partTransforms_10 = new Keyframe0c[keyframeCount][];

    for(int frameIndex = 0; frameIndex < keyframeCount; frameIndex++) {
      this.partTransforms_10[frameIndex] = new Keyframe0c[this.modelPartCount_0c];

      for(int partIndex = 0; partIndex < this.modelPartCount_0c; partIndex++) {
        this.partTransforms_10[frameIndex][partIndex] = new Keyframe0c(data.slice(0x10 + (frameIndex * this.modelPartCount_0c + partIndex) * 0xc, 0xc));
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

    final int framesPerKeyframe = 2;

    //LAB_800dd4fc
    final int totalFrames;
    final int frame;
    if(model.disableInterpolation_a2) {
      totalFrames = model.totalFrames_9a / 2;
      frame = animationTicks % totalFrames;
      model.currentKeyframe_94 = frame;
      applyKeyframe(model);
    } else {
      //LAB_800dd568
      totalFrames = model.totalFrames_9a;
      frame = animationTicks % totalFrames;
      model.currentKeyframe_94 = frame / 2;
      model.subFrameIndex = animationTicks % framesPerKeyframe;

      // Last condition checks if this is a repeat frame (this was causing Haschel's twitching during transform, Gehrich teabagging, etc.)
      // GH#1664, GH#1687
      if(model.subFrameIndex == 0 || model.ub_a3 != 0) {
        applyKeyframe(model);
      } else {
        this.applyInterpolationFrame(model, framesPerKeyframe);
      }
    }

    //LAB_800dd5f0
    model.remainingFrames_9e = totalFrames - frame - 1;

    if(model.remainingFrames_9e == 0) {
      model.animationState_9c = 0;
      model.subFrameIndex = 0;
    } else {
      //LAB_800dd618
      model.animationState_9c = 1;
    }
  }

  @Method(0x800213c4L)
  private void applyInterpolationFrame(final Model124 model, final int framesPerKeyframe) {
    //LAB_80021404
    for(int i = 0; i < model.modelParts_00.length; i++) {
      final GsCOORDINATE2 coord2 = model.modelParts_00[i].coord2_04;
      final Transforms params = coord2.transforms;

      // Differs from standard applyInterpolationFrame by considering frame 0 the keyframe
      final float interpolationScale = (float)model.subFrameIndex / framesPerKeyframe;
      params.trans.lerp(model.keyframes_90[Math.min(model.currentKeyframe_94 + 1, model.keyframes_90.length - 1)][i].translate_06, interpolationScale, coord2.coord.transfer);
      params.quat.nlerp(model.keyframes_90[Math.min(model.currentKeyframe_94 + 1, model.keyframes_90.length - 1)][i].quat, interpolationScale, params.quat);
      coord2.coord.rotation(params.quat);
    }
  }
}
