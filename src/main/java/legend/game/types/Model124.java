package legend.game.types;

import legend.core.gte.GsCOORD2PARAM;
import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.GsDOBJ2;
import legend.core.gte.GsOBJTABLE2;
import legend.core.gte.Tmd;
import legend.core.gte.VECTOR;
import legend.core.memory.types.UnboundedArrayRef;
import legend.game.combat.deff.Cmb;
import legend.game.combat.deff.Lmb;

public class Model124 {
  public GsDOBJ2[] dobj2ArrPtr_00;
  public GsCOORDINATE2[] coord2ArrPtr_04;
  public GsCOORD2PARAM[] coord2ParamArrPtr_08;
  /** Union with {@link #coord2ParamArrPtr_08} */
  public final LmbAnim lmbAnim_08 = new LmbAnim();
  /** Union with {@link #coord2ParamArrPtr_08} */
  public final CmbAnim cmbAnim_08 = new CmbAnim();
  public final GsOBJTABLE2 ObjTable_0c = new GsOBJTABLE2();
  // 0x50 bytes
  public final GsCOORDINATE2 coord2_14 = new GsCOORDINATE2();
  public final GsCOORD2PARAM coord2Param_64 = new GsCOORD2PARAM();
  public Tmd tmd_8c;
  public UnboundedArrayRef<ModelPartTransforms> partTransforms_90;
  /** One entry for each TMD object (tmdNobj_ca) */
  public UnboundedArrayRef<ModelPartTransforms> partTransforms_94;
  /**
   * Union with {@link #partTransforms_90}
   * <ul>
   *   <li>1 - LMB</li>
   *   <li>2 - CMB</li>
   * </ul>
   */
  public int animType_90;
  /** Union with {@link #partTransforms_94} */
  public int lmbUnknown_94;

  /** short */
  public int animCount_98;
  /** short */
  public int s_9a;
  /** ubyte */
  public int ub_9c;
  /** ubyte */
  public int colourMap_9d;
  /** short */
  public int s_9e;
  /** short */
  public int zOffset_a0;
  /** ubyte */
  public int ub_a2;
  /** ubyte */
  public int ub_a3;
  /** Pointer to an address on the linked list, 0x30 bytes long, contains data copied from {@link ExtendedTmd#ext_04} */
  public SmallerStruct smallerStructPtr_a4;
  /** TODO Pointer to whatever is pointed to by {@link ExtendedTmd#ptr_08} */
  public long ptr_a8;
  /** ushort */
  public final int[] usArr_ac = new int[7];
  /** ushort */
  public final int[] usArr_ba = new int[7];
  /** short */
  public int count_c8;
  /** ushort */
  public int tmdNobj_ca;
  /** byte */
  public int b_cc;
  /** byte */
  public int b_cd;

  public final long[] ptrs_d0 = new long[7];
  /** ubyte */
  public final int[] aub_ec = new int[7];

  /** One bit per object in TMD object table */
  public long ui_f4;
  public final VECTOR scaleVector_fc = new VECTOR();
  /** Pretty sure this doesn't include VRAM X/Y */
  public int tpage_108;
  public final VECTOR vector_10c = new VECTOR();
  public final VECTOR vector_118 = new VECTOR();

  public static class CmbAnim {
    public int _00;
    public Cmb cmb_04;
    public Cmb.Transforms0c[] transforms_08;

    public void set(final CmbAnim other) {
      this._00 = other._00;
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
    this.dobj2ArrPtr_00 = other.dobj2ArrPtr_00;
    this.coord2ArrPtr_04 = other.coord2ArrPtr_04;
    this.coord2ParamArrPtr_08 = other.coord2ParamArrPtr_08;
    this.lmbAnim_08.set(other.lmbAnim_08);
    this.cmbAnim_08.set(other.cmbAnim_08);
    this.ObjTable_0c.set(other.ObjTable_0c);
    this.coord2_14.set(other.coord2_14);
    this.coord2Param_64.set(other.coord2Param_64);
    this.tmd_8c = other.tmd_8c;
    this.partTransforms_90 = other.partTransforms_90;
    this.partTransforms_94 = other.partTransforms_94;
    this.animType_90 = other.animType_90;
    this.lmbUnknown_94 = other.lmbUnknown_94;
    this.animCount_98 = other.animCount_98;
    this.s_9a = other.s_9a;
    this.ub_9c = other.ub_9c;
    this.colourMap_9d = other.colourMap_9d;
    this.s_9e = other.s_9e;
    this.zOffset_a0 = other.zOffset_a0;
    this.ub_a2 = other.ub_a2;
    this.ub_a3 = other.ub_a3;
    this.smallerStructPtr_a4 = other.smallerStructPtr_a4;
    this.ptr_a8 = other.ptr_a8;
    System.arraycopy(other.usArr_ac, 0, this.usArr_ac, 0, 7);
    System.arraycopy(other.usArr_ba, 0, this.usArr_ba, 0, 7);
    this.count_c8 = other.count_c8;
    this.tmdNobj_ca = other.tmdNobj_ca;
    this.b_cc = other.b_cc;
    this.b_cd = other.b_cd;
    System.arraycopy(other.ptrs_d0, 0, this.ptrs_d0, 0, 7);
    System.arraycopy(other.aub_ec, 0, this.aub_ec, 0, 7);
    this.ui_f4 = other.ui_f4;
    this.scaleVector_fc.set(other.scaleVector_fc);
    this.tpage_108 = other.tpage_108;
    this.vector_10c.set(other.vector_10c);
    this.vector_118.set(other.vector_118);
  }
}
