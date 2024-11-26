package legend.game.combat.deff;

import legend.core.gte.MV;
import legend.core.memory.Method;
import legend.game.types.Model124;
import legend.game.unpacker.FileData;
import org.joml.Math;
import org.joml.Vector3f;

public class LmbType0 extends Lmb {
  public final PartInfo0c[] partAnimations_08;

  public LmbType0(final FileData data) {
    super(data);

    this.partAnimations_08 = new PartInfo0c[this.objectCount_04];

    for(int i = 0; i < this.objectCount_04; i++) {
      this.partAnimations_08[i] = new PartInfo0c(data.slice(0x8 + i * 0xc, 0xc), data);
    }
  }

  @Override
  public void loadIntoModel(final Model124 model) {
    model.anim_08 = model.new LmbAnim(this);
    model.partCount_98 = this.objectCount_04;
    model.totalFrames_9a = this.partAnimations_08[0].keyframeCount_04 * 2;
    model.animationState_9c = 1;
    model.remainingFrames_9e = this.partAnimations_08[0].keyframeCount_04 * 2;
    model.subFrameIndex = 0;
  }

  @Override
  @Method(0x800dd638L)
  public void apply(final Model124 model, final int animationTicks) {
    if(model.animationState_9c == 2) {
      return;
    }

    //LAB_800dd680
    final int count = Math.min(model.modelParts_00.length, model.partCount_98);

    //LAB_800dd69c
    final int a0_0;
    final int frame;
    final int remainingFrames;
    final int isInterpolationFrame;
    if(model.disableInterpolation_a2) {
      isInterpolationFrame = 0;
      a0_0 = (animationTicks << 1) % model.totalFrames_9a >>> 1;
      remainingFrames = (model.totalFrames_9a >> 1) - a0_0;
    } else {
      //LAB_800dd6dc
      frame = animationTicks % model.totalFrames_9a;
      isInterpolationFrame = (animationTicks & 0x1) << 11; // Dunno why this is shifted, makes no difference
      a0_0 = frame >>> 1;
      remainingFrames = model.totalFrames_9a - frame;
    }

    //LAB_800dd700
    model.remainingFrames_9e = remainingFrames - 1;
    model.subFrameIndex = 0;

    //LAB_800dd720
    for(int i = 0; i < count; i++) {
      final LmbTransforms14 transforms = this.partAnimations_08[i].keyframes_08[a0_0];
      final MV matrix = model.modelParts_00[i].coord2_04.coord;

      final Vector3f trans = new Vector3f();
      trans.set(transforms.trans_06);

      if(isInterpolationFrame != 0) { // Interpolation frame
        final LmbTransforms14 nextFrame;
        if(animationTicks == model.totalFrames_9a - 1) {
          nextFrame = this.partAnimations_08[i].keyframes_08[0]; // Wrap around to frame 0
        } else {
          //LAB_800dd7cc
          nextFrame = this.partAnimations_08[i].keyframes_08[a0_0 + 1];
        }

        //LAB_800dd7d0
        trans.set(
          (trans.x + nextFrame.trans_06.x) / 2,
          (trans.y + nextFrame.trans_06.y) / 2,
          (trans.z + nextFrame.trans_06.z) / 2
        );
      }

      //LAB_800dd818
      matrix.rotationZYX(transforms.rot_0c);
      matrix.transfer.set(trans);
      matrix.scaleLocal(transforms.scale_00);
    }

    //LAB_800dd84c
    if(model.remainingFrames_9e == 0) {
      model.animationState_9c = 0;
    } else {
      //LAB_800dd864
      model.animationState_9c = 1;
    }
  }

  public static class PartInfo0c {
    public final short partFlagIndex_00;

    public final short keyframeCount_04;

    public final LmbTransforms14[] keyframes_08;

    public PartInfo0c(final FileData data, final FileData baseData) {
      this.partFlagIndex_00 = data.readShort(0x0);

      this.keyframeCount_04 = data.readShort(0x4);

      final int transformsOffset = data.readInt(0x8);
      this.keyframes_08 = new LmbTransforms14[this.keyframeCount_04];
      for(int i = 0; i < this.keyframes_08.length; i++) {
        this.keyframes_08[i] = new LmbTransforms14(baseData.slice(transformsOffset + i * 0x14, 0x14));
      }
    }
  }
}
