package legend.game.types;

import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.ModelPart10;
import legend.core.gte.VECTOR;
import legend.game.combat.deff.Cmb;
import legend.game.combat.deff.Lmb;
import org.joml.Vector3f;

public class Model124 {
  public final String name;

  public ModelPart10[] modelParts_00;
//  public GsCOORDINATE2[] coord2ArrPtr_04; // Use coord2 on modelParts_00
//  public Transforms[] coord2ParamArrPtr_08; // Use modelParts_00.coord.transforms
  /** Union with coord2ParamArrPtr_08 */
  public final LmbAnim lmbAnim_08 = new LmbAnim();
  /** Union with coord2ParamArrPtr_08 */
  public final CmbAnim cmbAnim_08 = new CmbAnim();
//  public final GsOBJTABLE2 ObjTable_0c = new GsOBJTABLE2();
  // Supercoordinate system for all model part coordinate systems, all model parts are attached to this
  public final GsCOORDINATE2 coord2_14 = new GsCOORDINATE2();
//  public final Transforms transforms_64 = new Transforms(); // Use coord2_14.transforms
//  public Tmd tmd_8c;
  /** [keyframe][part] */
  public ModelPartTransforms0c[][] partTransforms_90;
  /** [keyframe][part] One entry for each TMD object (tmdNobj_ca) */
  public ModelPartTransforms0c[][] partTransforms_94;
  /**
   * Union with {@link #partTransforms_90}
   * <ul>
   *   <li>-1 - SAF</li>
   *   <li>0 - CMB</li>
   *   <li>1 - LMB</li>
   *   <li>2 - CMB</li>
   * </ul>
   */
  public int animType_90;
  /** Union with {@link #partTransforms_94} */
  public int lmbUnknown_94;

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
  public int colourMap_9d;
  /** short */
  public int remainingFrames_9e;
  /** short */
  public int zOffset_a0;
  /** Always 0 except sometimes on submaps (ubyte) */
  public int ub_a2;
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
  /** 0/1/2/3 - if 0, the model won't render (byte) */
  public int movementType_cc;
  /** byte */
  public int modelPartIndex_cd;

  public final short[][] ptrs_d0 = new short[7][];
  /** ubyte */
  public final boolean[] animateTextures_ec = new boolean[7];

  /** One bit per object in TMD object table */
  public long partInvisible_f4;
//  public final Vector3f scaleVector_fc = new Vector3f(); // Using transforms_64.scale instead
  /** Pretty sure this doesn't include VRAM X/Y */
  public int tpage_108;
  /** Maybe initial scale? */
  public final Vector3f vector_10c = new Vector3f();
  /** Maybe initial translation? */
  public final VECTOR vector_118 = new VECTOR();

  public Model124(final String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return this.name + " (" + super.toString() + ')';
  }

  public static class CmbAnim {
    public int animationTicks_00;
    public Cmb cmb_04;
    public ModelPartTransforms0c[] transforms_08;

    public void set(final CmbAnim other) {
      this.animationTicks_00 = other.animationTicks_00;
      this.cmb_04 = other.cmb_04;
      this.transforms_08 = other.transforms_08;
    }
  }

  public static class LmbAnim {
    public Lmb lmb_00;

    public void set(final LmbAnim other) {
      this.lmb_00 = other.lmb_00;
    }
  }

  public void set(final Model124 other) {
    this.modelParts_00 = other.modelParts_00;
    this.lmbAnim_08.set(other.lmbAnim_08);
    this.cmbAnim_08.set(other.cmbAnim_08);
    this.coord2_14.set(other.coord2_14);
    this.partTransforms_90 = other.partTransforms_90;
    this.partTransforms_94 = other.partTransforms_94;
    this.animType_90 = other.animType_90;
    this.lmbUnknown_94 = other.lmbUnknown_94;
    this.partCount_98 = other.partCount_98;
    this.totalFrames_9a = other.totalFrames_9a;
    this.animationState_9c = other.animationState_9c;
    this.colourMap_9d = other.colourMap_9d;
    this.remainingFrames_9e = other.remainingFrames_9e;
    this.zOffset_a0 = other.zOffset_a0;
    this.ub_a2 = other.ub_a2;
    this.ub_a3 = other.ub_a3;
    this.smallerStructPtr_a4 = other.smallerStructPtr_a4;
    this.ptr_a8 = other.ptr_a8;
    System.arraycopy(other.usArr_ac, 0, this.usArr_ac, 0, 7);
    System.arraycopy(other.usArr_ba, 0, this.usArr_ba, 0, 7);
    this.movementType_cc = other.movementType_cc;
    this.modelPartIndex_cd = other.modelPartIndex_cd;
    System.arraycopy(other.ptrs_d0, 0, this.ptrs_d0, 0, 7);
    System.arraycopy(other.animateTextures_ec, 0, this.animateTextures_ec, 0, 7);
    this.partInvisible_f4 = other.partInvisible_f4;
    this.tpage_108 = other.tpage_108;
    this.vector_10c.set(other.vector_10c);
    this.vector_118.set(other.vector_118);
  }
}
