package legend.game.combat.deff;

import legend.core.MathHelper;
import legend.core.gte.MV;
import legend.core.memory.Method;
import legend.game.types.Model124;
import legend.game.types.Keyframe0c;
import legend.game.types.TmdAnimationFile;
import legend.game.unpacker.FileData;
import org.joml.Math;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector3i;

public class Cmb extends TmdAnimationFile {
  public static final int MAGIC = 0x2042_4d43;

  public final int size_08;

  /** [frame][part] */
  public final SubTransforms08[][] subTransforms;

  public Cmb(final FileData data) {
    super(data);

    if(data.readInt(0) != MAGIC) {
      throw new RuntimeException("Not a CMB! Magic: %x".formatted(data.readInt(0)));
    }

    this.size_08 = data.readUShort(0x8);

    final int baseAddress = 0x10 + this.modelPartCount_0c * 0xc;

    final int keyframeCount = this.totalFrames_0e - 1;
    this.subTransforms = new SubTransforms08[keyframeCount][];

    for(int frameIndex = 0; frameIndex < keyframeCount; frameIndex++) {
      this.subTransforms[frameIndex] = new SubTransforms08[this.modelPartCount_0c];

      for(int partIndex = 0; partIndex < this.modelPartCount_0c; partIndex++) {
        this.subTransforms[frameIndex][partIndex] = new SubTransforms08(data.slice(baseAddress + (frameIndex * this.modelPartCount_0c + partIndex) * 0x8, 0x8));
      }
    }
  }

  @Method(0x800de210L)
  @Override
  public void loadIntoModel(final Model124 model) {
    final Model124.CmbAnim anim = model.new CmbAnim(this, this.modelPartCount_0c);
    model.anim_08 = anim;

    model.partCount_98 = this.modelPartCount_0c;
    model.totalFrames_9a = this.totalFrames_0e * 2;
    model.animationState_9c = 1;
    model.remainingFrames_9e = this.totalFrames_0e * 2;
    model.subFrameIndex = 0;

    //LAB_800de270
    for(int i = 0; i < this.modelPartCount_0c; i++) {
      final Keyframe0c v1 = this.partTransforms_10[0][i];
      final Keyframe0c a1_0 = anim.transforms_08[i];
      a1_0.rotate_00.set(v1.rotate_00);
      a1_0.translate_06.set(v1.translate_06);
    }

    //LAB_800de2c8
    anim.animationTicks_00 = 1;
    this.apply(model, 0);
  }

  @Override
  @Method(0x800ddd3cL)
  public void apply(final Model124 model, final int animationTicks) {
    if(model.animationState_9c == 2) {
      return;
    }

    //LAB_800ddd9c
    final Model124.CmbAnim cmbAnim = (Model124.CmbAnim)model.anim_08;

    if(animationTicks == cmbAnim.animationTicks_00) {
      return;
    }

    // Note: these two variables _should_ be the same
    final int modelPartCount = this.modelPartCount_0c;
    final int count = Math.min(model.modelParts_00.length, model.partCount_98);

    //LAB_800dddc4
    int frameIndex;
    final int a1_0;
    final int isInterpolationFrame;
    if(model.disableInterpolation_a2) {
      isInterpolationFrame = 0;
      a1_0 = (animationTicks * 2) % model.totalFrames_9a / 2;
      frameIndex = (cmbAnim.animationTicks_00 * 2) % model.totalFrames_9a / 2;
      model.remainingFrames_9e = model.totalFrames_9a / 2 - a1_0 - 1;
    } else {
      //LAB_800dde1c
      // This modulo has to be unsigned due to a bug causing the number of ticks
      // to go negative. This matches the retail behaviour (it uses divu).
      final int frame = (int)((animationTicks & 0xffff_ffffL) % model.totalFrames_9a);
      isInterpolationFrame = animationTicks & 0x1;
      a1_0 = frame >>> 1;
      frameIndex = cmbAnim.animationTicks_00 % model.totalFrames_9a >> 1;
      model.remainingFrames_9e = model.totalFrames_9a - frame - 1;

      // This is another retail bug - it's possible for the frame index to go negative
      if(frameIndex < 0) {
        frameIndex = 0;
      }
    }

    model.subFrameIndex = 0;

    //LAB_800dde60
    if(frameIndex > a1_0) {
      //LAB_800dde88
      for(int partIndex = 0; partIndex < modelPartCount; partIndex++) {
        final Keyframe0c fileTransforms = this.partTransforms_10[0][partIndex];
        final Keyframe0c modelTransforms = cmbAnim.transforms_08[partIndex];

        modelTransforms.rotate_00.set(fileTransforms.rotate_00);
        modelTransforms.translate_06.set(fileTransforms.translate_06);
      }

      //LAB_800ddee0
      cmbAnim.animationTicks_00 = 0;
      frameIndex = 0;
    }

    //LAB_800ddeec
    //LAB_800ddf1c
    for(; frameIndex < a1_0; frameIndex++) {
      //LAB_800ddf2c
      for(int partIndex = 0; partIndex < modelPartCount; partIndex++) {
        final Cmb.SubTransforms08 subTransforms = this.subTransforms[frameIndex][partIndex];
        final Keyframe0c modelTransforms = cmbAnim.transforms_08[partIndex];

        modelTransforms.rotate_00.add(subTransforms.rot_01);
        modelTransforms.translate_06.add(subTransforms.trans_05);
      }

      //LAB_800ddfd4
    }

    //LAB_800ddfe4
    //LAB_800de158
    if(isInterpolationFrame != 0 && model.ub_a3 == 0 && a1_0 != (model.totalFrames_9a >> 1) - 1) { // Interpolation frame
      final Quaternionf q0 = new Quaternionf();
      final Quaternionf q1 = new Quaternionf();
      final Vector3f translation = new Vector3f();

      //LAB_800de050
      for(int i = 0; i < count; i++) {
        final Cmb.SubTransforms08 subTransforms = this.subTransforms[a1_0][i];
        final Keyframe0c modelTransforms = cmbAnim.transforms_08[i];

        final MV modelPartMatrix = model.modelParts_00[i].coord2_04.coord;
        modelPartMatrix.transfer.set(modelTransforms.translate_06);

        modelTransforms.translate_06.add(subTransforms.trans_05, translation);

        q0.rotationZYX(modelTransforms.rotate_00.z, modelTransforms.rotate_00.y, modelTransforms.rotate_00.x);
        q1.rotationZYX(modelTransforms.rotate_00.z + subTransforms.rot_01.z, modelTransforms.rotate_00.y + subTransforms.rot_01.y, modelTransforms.rotate_00.x + subTransforms.rot_01.x);

        this.lerp(q0, q1, modelPartMatrix.transfer, translation, 0.5f);
        modelPartMatrix.rotation(q0);
      }
    } else {
      //LAB_800de164
      for(int i = 0; i < count; i++) {
        final Keyframe0c modelTransforms = cmbAnim.transforms_08[i];
        final MV modelPartMatrix = model.modelParts_00[i].coord2_04.coord;
        modelPartMatrix.rotationZYX(modelTransforms.rotate_00);
        modelPartMatrix.transfer.set(modelTransforms.translate_06);
      }
    }

    //LAB_800de1b4
    if(model.remainingFrames_9e == 0) {
      model.animationState_9c = 0;
    } else {
      //LAB_800de1cc
      model.animationState_9c = 1;
    }

    //LAB_800de1d0
    cmbAnim.animationTicks_00 = animationTicks;
  }

  @Method(0x800dd15cL)
  private Quaternionf lerp(final Quaternionf q0, final Quaternionf q1, final Vector3f v0, final Vector3f v1, final float ratio) {
    if(ratio > 0) {
      final float recipRatio = 1.0f - ratio;
      q0.nlerp(q1, ratio);
      v0.x = v0.x * recipRatio + v1.x * ratio;
      v0.y = v0.y * recipRatio + v1.y * ratio;
      v0.z = v0.z * recipRatio + v1.z * ratio;
    }

    //LAB_800dd4b8
    //LAB_800dd4bc
    return q0;
  }

  public static class SubTransforms08 {
//    /** ubyte */
//    public final int rotScale_00;
//    public final BVEC4 rot_01 = new BVEC4();
//    /** ubyte */
//    public final int transScale_04;
//    public final BVEC4 trans_05 = new BVEC4();

    public final Vector3f rot_01 = new Vector3f();
    public final Vector3f trans_05 = new Vector3f();

    public SubTransforms08(final FileData data) {
      final int rotScale_00 = 1 << data.readUByte(0);
      final Vector3i rot = data.readBvec3(1, new Vector3i());

      final int transScale_04 = 1 << data.readUByte(4);
      final Vector3i trans = data.readBvec3(5, new Vector3i());

      this.rot_01.set(
        MathHelper.psxDegToRad(rot.x * rotScale_00),
        MathHelper.psxDegToRad(rot.y * rotScale_00),
        MathHelper.psxDegToRad(rot.z * rotScale_00)
      );

      this.trans_05.set(
        trans.x * transScale_04,
        trans.y * transScale_04,
        trans.z * transScale_04
      );
    }
  }
}
