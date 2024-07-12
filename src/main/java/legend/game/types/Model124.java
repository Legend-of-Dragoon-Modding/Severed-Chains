package legend.game.types;

import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.ModelPart10;
import legend.game.combat.deff.Cmb;
import legend.game.combat.deff.Lmb;
import legend.game.tmd.UvAdjustmentMetrics14;
import org.joml.Vector3f;

import java.util.Arrays;

public class Model124 {
  public final String name;

  public ModelPart10[] modelParts_00;
//  public GsCOORDINATE2[] coord2ArrPtr_04; // Use coord2 on modelParts_00
//  public Transforms[] coord2ParamArrPtr_08; // Use modelParts_00.coord.transforms
  /** Union with coord2ParamArrPtr_08 */
  public AnimType anim_08;
//  public final GsOBJTABLE2 ObjTable_0c = new GsOBJTABLE2();
  // Supercoordinate system for all model part coordinate systems, all model parts are attached to this
  public final GsCOORDINATE2 coord2_14 = new GsCOORDINATE2();
//  public final Transforms transforms_64 = new Transforms(); // Use coord2_14.transforms
//  public Tmd tmd_8c;
  /** [keyframe][part] */
  public Keyframe0c[][] keyframes_90;
  public int currentKeyframe_94;

  /** short */
  public int partCount_98;
  /** short */
  public int totalFrames_9a;
  /**
   * <ol start="0">
   *   <li>Init/reset - start animation from beginning and transition to state 1</li>
   *   <li>Playing</li>
   *   <li>Paused</li>
   * </ol>
   *
   * ubyte */
  public int animationState_9c;
  /** ubyte */
  public UvAdjustmentMetrics14 uvAdjustments_9d = UvAdjustmentMetrics14.NONE;
  /** short */
  public int remainingFrames_9e;
  public int subFrameIndex;
  /** short */
  public int zOffset_a0;
  /** Always 0 except sometimes on submaps (ubyte) */
  public boolean disableInterpolation_a2;
  /** ubyte */
  public int ub_a3;
  /** Pointer to an address on the linked list, 0x30 bytes long, contains data copied from {@link CContainer#ext_04} */
  public SmallerStruct smallerStructPtr_a4;
  /** Pointer to the subfile pointed to by {@link CContainer#ptr_08} */
  public CContainerSubfile2 ptr_a8;
  /** ushort */
  public final int[] usArr_ac = new int[7];
  /** ushort */
  public final int[] usArr_ba = new int[7];
//  /** short */
//  public int count_c8; // Use modelParts_00.length
//  /** ushort */
//  public int tmdNobj_ca; // Use modelParts_00.length
  /** 0/1/2/3 - if 0, some model attached to this model won't render (maybe shadow/dust?) (byte) */
  public int shadowType_cc;
  /** byte */
  public int modelPartWithShadowIndex_cd;

  public final short[][] animationMetrics_d0 = new short[7][];
  /** ubyte */
  public final boolean[] animateTextures_ec = new boolean[7];

  /** One bit per object in TMD object table */
  public long partInvisible_f4;
//  public final Vector3f scaleVector_fc = new Vector3f(); // Using transforms_64.scale instead
  /** Pretty sure this doesn't include VRAM X/Y */
  public int tpage_108;
  public final Vector3f shadowSize_10c = new Vector3f();
  public final Vector3f shadowOffset_118 = new Vector3f();

  public Model124(final String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return this.name + " (" + super.toString() + ')';
  }

  public abstract class AnimType {
    public abstract void apply(final int animationTicks);
  }

  public class StandardAnim extends AnimType {
    public final TmdAnimationFile anim;

    public StandardAnim(final TmdAnimationFile anim) {
      this.anim = anim;
    }

    @Override
    public void apply(final int animationTicks) {
      this.anim.apply(Model124.this, animationTicks);
    }
  }

  public class CmbAnim extends AnimType {
    public int animationTicks_00;
    public final Cmb cmb_04;
    public final Keyframe0c[] transforms_08;

    public CmbAnim(final Cmb cmb, final int count) {
      this.cmb_04 = cmb;
      this.transforms_08 = new Keyframe0c[count];
      Arrays.setAll(this.transforms_08, i -> new Keyframe0c());
    }

    @Override
    public void apply(final int animationTicks) {
      this.cmb_04.apply(Model124.this, animationTicks);
    }
  }

  public class LmbAnim extends AnimType {
    public final Lmb lmb_00;

    public LmbAnim(final Lmb lmb) {
      this.lmb_00 = lmb;
    }

    @Override
    public void apply(final int animationTicks) {
      this.lmb_00.apply(Model124.this, animationTicks);
    }
  }

  public void set(final Model124 other) {
    this.modelParts_00 = other.modelParts_00;
    this.anim_08 = other.anim_08;
    this.coord2_14.set(other.coord2_14);
    this.keyframes_90 = other.keyframes_90;
    this.currentKeyframe_94 = other.currentKeyframe_94;
    this.partCount_98 = other.partCount_98;
    this.totalFrames_9a = other.totalFrames_9a;
    this.animationState_9c = other.animationState_9c;
    this.uvAdjustments_9d = other.uvAdjustments_9d;
    this.remainingFrames_9e = other.remainingFrames_9e;
    this.zOffset_a0 = other.zOffset_a0;
    this.disableInterpolation_a2 = other.disableInterpolation_a2;
    this.ub_a3 = other.ub_a3;
    this.smallerStructPtr_a4 = other.smallerStructPtr_a4;
    this.ptr_a8 = other.ptr_a8;
    System.arraycopy(other.usArr_ac, 0, this.usArr_ac, 0, 7);
    System.arraycopy(other.usArr_ba, 0, this.usArr_ba, 0, 7);
    this.shadowType_cc = other.shadowType_cc;
    this.modelPartWithShadowIndex_cd = other.modelPartWithShadowIndex_cd;
    System.arraycopy(other.animationMetrics_d0, 0, this.animationMetrics_d0, 0, 7);
    System.arraycopy(other.animateTextures_ec, 0, this.animateTextures_ec, 0, 7);
    this.partInvisible_f4 = other.partInvisible_f4;
    this.tpage_108 = other.tpage_108;
    this.shadowSize_10c.set(other.shadowSize_10c);
    this.shadowOffset_118.set(other.shadowOffset_118);
  }

  public void deleteModelParts() {
    if(this.modelParts_00 != null) {
      for(final ModelPart10 part : this.modelParts_00) {
        part.delete();
      }
    }
  }
}
