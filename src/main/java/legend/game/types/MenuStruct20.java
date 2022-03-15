package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;

public class MenuStruct20 implements MemoryRef {
  private final Value ref;

  public final Pointer<ArrayRef<UnsignedShortRef>> _00;
  public final Pointer<Drgn0_6666Struct58> drgn0_6666_04;
  public final Pointer<Drgn0_6666Struct58> drgn0_6666_08;

  public final UnsignedByteRef _0c;

  public final UnsignedIntRef _10;

  public final UnsignedByteRef _15;

  public final UnsignedIntRef _18;
  public final UnsignedShortRef _1c;
  public final UnsignedShortRef _1e;

  public MenuStruct20(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(4, 0x00L).cast(Pointer.deferred(4, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new)));
    this.drgn0_6666_04 = ref.offset(4, 0x04L).cast(Pointer.deferred(4, Drgn0_6666Struct58::new));
    this.drgn0_6666_08 = ref.offset(4, 0x08L).cast(Pointer.deferred(4, Drgn0_6666Struct58::new));

    this._0c = ref.offset(1, 0x0cL).cast(UnsignedByteRef::new);

    this._10 = ref.offset(4, 0x10L).cast(UnsignedIntRef::new);

    this._15 = ref.offset(1, 0x15L).cast(UnsignedByteRef::new);

    this._18 = ref.offset(4, 0x18L).cast(UnsignedIntRef::new);
    this._1c = ref.offset(2, 0x1cL).cast(UnsignedShortRef::new);
    this._1e = ref.offset(2, 0x1eL).cast(UnsignedShortRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
