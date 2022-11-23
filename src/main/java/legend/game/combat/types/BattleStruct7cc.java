package legend.game.combat.types;

import legend.core.gte.DVECTOR;
import legend.core.gte.SVECTOR;
import legend.core.gte.TmdObjTable;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;
import legend.game.types.MrgFile;

public class BattleStruct7cc implements MemoryRef {
  private final Value ref;

  //TODO these probably aren't actually an SVEC and an array of DVECs
  public final SVECTOR svec_00;
  public final ArrayRef<DVECTOR> dvecs_08;

  public final IntRef scriptIndex_1c;
  public final UnsignedIntRef _20;
  /** TODO ptr */
  public final UnsignedIntRef ptr_24;
  //TODO sub-structs from here down?
  public final Value _28;
  public final Pointer<MrgFile> mrg_2c;
  public final UnsignedIntRef _30;
  public final UnsignedIntRef _34;
  public final Pointer<DeffFile> deff_38;

  public final BattleStruct4c _4c;
  public final ArrayRef<BattleStruct4c> _98;
  public final UnboundedArrayRef<Pointer<TmdObjTable>> tmds_2f8;

  public final UnboundedArrayRef<Pointer<DeffPart>> _390;

  public final UnboundedArrayRef<SpriteMetrics08> spriteMetrics_39c;

  public final UnsignedShortRef u_3ac;
  public final UnsignedShortRef v_3ae;

  public final UnsignedShortRef clut_3b2;

  public final Pointer<MrgFile> deffPackage_5a8;
  public final Pointer<DeffFile> deff_5ac;

  public final BattleStruct24_2 _5b8;
  public final BattleLightStruct64 _5dc;
  public final Value _640;

  public BattleStruct7cc(final Value ref) {
    this.ref = ref;

    this.svec_00 = ref.offset(2, 0x00L).cast(SVECTOR::new);
    this.dvecs_08 = ref.offset(2, 0x08L).cast(ArrayRef.of(DVECTOR.class, 5, 0x4, DVECTOR::new));
    this.scriptIndex_1c = ref.offset(4, 0x1cL).cast(IntRef::new);
    this._20 = ref.offset(4, 0x20L).cast(UnsignedIntRef::new);
    this.ptr_24 = ref.offset(4, 0x24L).cast(UnsignedIntRef::new);
    this._28 = ref.offset(4, 0x28L);
    this.mrg_2c = ref.offset(4, 0x2cL).cast(Pointer.deferred(4, MrgFile::new));
    this._30 = ref.offset(4, 0x30L).cast(UnsignedIntRef::new);
    this._34 = ref.offset(4, 0x34L).cast(UnsignedIntRef::new);
    this.deff_38 = ref.offset(4, 0x38L).cast(Pointer.deferred(4, DeffFile::new));

    this._4c = ref.offset(4, 0x4cL).cast(BattleStruct4c::new);
    this._98 = ref.offset(4, 0x98L).cast(ArrayRef.of(BattleStruct4c.class, 8, 0x4c, BattleStruct4c::new));
    this.tmds_2f8 = ref.offset(4, 0x2f8L).cast(UnboundedArrayRef.of(0x4, Pointer.deferred(4, TmdObjTable::new)));

    this._390 = ref.offset(4, 0x390L).cast(UnboundedArrayRef.of(0x4, Pointer.deferred(4, DeffPart::new)));

    this.spriteMetrics_39c = ref.offset(4, 0x39cL).cast(UnboundedArrayRef.of(8, SpriteMetrics08::new));

    this.u_3ac = ref.offset(2, 0x3acL).cast(UnsignedShortRef::new);
    this.v_3ae = ref.offset(2, 0x3aeL).cast(UnsignedShortRef::new);

    this.clut_3b2 = ref.offset(2, 0x3b2L).cast(UnsignedShortRef::new);

    this.deffPackage_5a8 = ref.offset(4, 0x5a8L).cast(Pointer.deferred(4, MrgFile::new));
    this.deff_5ac = ref.offset(4, 0x5acL).cast(Pointer.deferred(4, DeffFile::new));

    this._5b8 = ref.offset(4, 0x5b8L).cast(BattleStruct24_2::new);
    this._5dc = ref.offset(4, 0x5dcL).cast(BattleLightStruct64::new);
    this._640 = ref.offset(4, 0x640L);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
