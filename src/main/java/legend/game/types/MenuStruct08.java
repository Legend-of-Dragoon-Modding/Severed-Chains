package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedShortRef;

public class MenuStruct08 implements MemoryRef {
  private final Value ref;

  public final Pointer<ArrayRef<UnsignedShortRef>> _00;
  public final UnsignedByteRef _04;

  public MenuStruct08(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(4, 0x0L).cast(Pointer.deferred(4, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new)));
    this._04 = ref.offset(1, 0x4L).cast(UnsignedByteRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
