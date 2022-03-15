package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;

public class Drgn0_6666Struct58 implements MemoryRef {
  private final Value ref;

  public final UnsignedIntRef _00;
  public final UnsignedIntRef _04;
  public final IntRef _08;
  public final UnsignedIntRef _0c;
  public final UnsignedIntRef _10;
  public final UnsignedIntRef _14;
  public final UnsignedIntRef _18;
  public final UnsignedIntRef _1c;
  public final UnsignedIntRef _20;
  public final UnsignedIntRef _24;
  public final UnsignedByteRef _28;
  public final UnsignedIntRef _2c;
  public final UnsignedIntRef _30;
  public final UnsignedIntRef _34;
  public final UnsignedIntRef _38;
  public final UnsignedByteRef _3c;
  public final UnsignedIntRef _40;
  public final UnsignedIntRef _44;
  public final UnsignedIntRef _48;
  public final Pointer<Drgn0_6666Struct58> child_50;
  public final Pointer<Drgn0_6666Struct58> parent_54;

  public Drgn0_6666Struct58(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(4, 0x00L).cast(UnsignedIntRef::new);
    this._04 = ref.offset(4, 0x04L).cast(UnsignedIntRef::new);
    this._08 = ref.offset(4, 0x08L).cast(IntRef::new);
    this._0c = ref.offset(4, 0x0cL).cast(UnsignedIntRef::new);
    this._10 = ref.offset(4, 0x10L).cast(UnsignedIntRef::new);
    this._14 = ref.offset(4, 0x14L).cast(UnsignedIntRef::new);
    this._18 = ref.offset(4, 0x18L).cast(UnsignedIntRef::new);
    this._1c = ref.offset(4, 0x1cL).cast(UnsignedIntRef::new);
    this._20 = ref.offset(4, 0x20L).cast(UnsignedIntRef::new);
    this._24 = ref.offset(4, 0x24L).cast(UnsignedIntRef::new);
    this._28 = ref.offset(1, 0x28L).cast(UnsignedByteRef::new);
    this._2c = ref.offset(4, 0x2cL).cast(UnsignedIntRef::new);
    this._30 = ref.offset(4, 0x30L).cast(UnsignedIntRef::new);
    this._34 = ref.offset(4, 0x34L).cast(UnsignedIntRef::new);
    this._38 = ref.offset(4, 0x38L).cast(UnsignedIntRef::new);
    this._3c = ref.offset(1, 0x3cL).cast(UnsignedByteRef::new);
    this._40 = ref.offset(4, 0x40L).cast(UnsignedIntRef::new);
    this._44 = ref.offset(4, 0x44L).cast(UnsignedIntRef::new);
    this._48 = ref.offset(4, 0x48L).cast(UnsignedIntRef::new);
    this.child_50 = ref.offset(4, 0x50L).cast(Pointer.deferred(4, Drgn0_6666Struct58::new));
    this.parent_54 = ref.offset(4, 0x54L).cast(Pointer.deferred(4, Drgn0_6666Struct58::new));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
