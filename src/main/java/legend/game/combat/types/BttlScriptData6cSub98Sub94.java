package legend.game.combat.types;

import legend.core.gte.SVECTOR;
import legend.core.gte.VECTOR;
import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;

public class BttlScriptData6cSub98Sub94 implements MemoryRef {
  private final Value ref;

  public final UnsignedByteRef _00;
  public final UnsignedByteRef _01;
  public final UnsignedByteRef _02;

  public final ShortRef _04;
  public final ShortRef _06;
  public final ShortRef _08;
  public final ShortRef _0a;
  public final ShortRef _0c;
  public final ShortRef _0e;
  public final ShortRef _10;
  public final ShortRef _12;
  public final ShortRef _14;
  public final ShortRef _16;
  public final ShortRef _18;
  public final SVECTOR _1a;
  public final ShortRef _20;
  public final ShortRef _22;
  public final ShortRef _24;

  public final VECTOR _2c;

  public final SVECTOR _3c;
  public final Pointer<UnboundedArrayRef<SVECTOR>> _44;
  public final SVECTOR _48;
  public final SVECTOR _50;
  public final SVECTOR _58;
  public final SVECTOR _60;
  public final SVECTOR _68;
  public final SVECTOR _70;
  public final SVECTOR _78;
  /** TODO ptr */
  public final UnsignedIntRef _80;
  public final UnsignedShortRef _84;
  public final UnsignedShortRef _86;
  public final UnsignedShortRef _88;
  public final UnsignedShortRef _8a;
  public final UnsignedShortRef _8c;
  public final UnsignedShortRef _8e;
  public final UnsignedIntRef _90;

  public BttlScriptData6cSub98Sub94(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(1, 0x00L).cast(UnsignedByteRef::new);
    this._01 = ref.offset(1, 0x01L).cast(UnsignedByteRef::new);
    this._02 = ref.offset(1, 0x02L).cast(UnsignedByteRef::new);

    this._04 = ref.offset(2, 0x04L).cast(ShortRef::new);
    this._06 = ref.offset(2, 0x06L).cast(ShortRef::new);
    this._08 = ref.offset(2, 0x08L).cast(ShortRef::new);
    this._0a = ref.offset(2, 0x0aL).cast(ShortRef::new);
    this._0c = ref.offset(2, 0x0cL).cast(ShortRef::new);
    this._0e = ref.offset(2, 0x0eL).cast(ShortRef::new);
    this._10 = ref.offset(2, 0x10L).cast(ShortRef::new);
    this._12 = ref.offset(2, 0x12L).cast(ShortRef::new);
    this._14 = ref.offset(2, 0x14L).cast(ShortRef::new);
    this._16 = ref.offset(2, 0x16L).cast(ShortRef::new);
    this._18 = ref.offset(2, 0x18L).cast(ShortRef::new);
    this._1a = ref.offset(2, 0x1aL).cast(SVECTOR::new);
    this._20 = ref.offset(2, 0x20L).cast(ShortRef::new);
    this._22 = ref.offset(2, 0x22L).cast(ShortRef::new);
    this._24 = ref.offset(2, 0x24L).cast(ShortRef::new);

    this._2c = ref.offset(4, 0x2cL).cast(VECTOR::new);

    this._3c = ref.offset(2, 0x3cL).cast(SVECTOR::new);
    this._44 = ref.offset(4, 0x44L).cast(Pointer.deferred(4, UnboundedArrayRef.of(0x8, SVECTOR::new)));
    this._48 = ref.offset(2, 0x48L).cast(SVECTOR::new);
    this._50 = ref.offset(2, 0x50L).cast(SVECTOR::new);
    this._58 = ref.offset(2, 0x58L).cast(SVECTOR::new);
    this._60 = ref.offset(2, 0x60L).cast(SVECTOR::new);
    this._68 = ref.offset(2, 0x68L).cast(SVECTOR::new);
    this._70 = ref.offset(2, 0x70L).cast(SVECTOR::new);
    this._78 = ref.offset(2, 0x78L).cast(SVECTOR::new);
    this._80 = ref.offset(4, 0x80L).cast(UnsignedIntRef::new);
    this._84 = ref.offset(2, 0x84L).cast(UnsignedShortRef::new);
    this._86 = ref.offset(2, 0x86L).cast(UnsignedShortRef::new);
    this._88 = ref.offset(2, 0x88L).cast(UnsignedShortRef::new);
    this._8a = ref.offset(2, 0x8aL).cast(UnsignedShortRef::new);
    this._8c = ref.offset(2, 0x8cL).cast(UnsignedShortRef::new);
    this._8e = ref.offset(2, 0x8eL).cast(UnsignedShortRef::new);
    this._90 = ref.offset(4, 0x90L).cast(UnsignedIntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
