package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.BiFunctionRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.UnsignedIntRef;

public class UnknownStruct implements MemoryRef {
  private final Value ref;

  public final Pointer<UnknownStruct> parent_00;
  public final Pointer<BiFunctionRef<UnknownStruct2, Long, Long>> callback_04;
  public final Pointer<UnknownStruct2> inner_08;
  public final UnsignedIntRef _0c;

  public UnknownStruct(final Value ref) {
    this.ref = ref;

    this.parent_00 = ref.offset(4, 0x00L).cast(Pointer.deferred(4, UnknownStruct::new));
    this.callback_04 = ref.offset(4, 0x04L).cast(Pointer.deferred(4, BiFunctionRef::new));
    this.inner_08 = ref.offset(4, 0x08L).cast(Pointer.deferred(4, UnknownStruct2::new));
    this._0c = ref.offset(4, 0x0cL).cast(UnsignedIntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
