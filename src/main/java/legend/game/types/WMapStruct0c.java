package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedShortRef;

public class WMapStruct0c implements MemoryRef {
  private final Value ref;

  public final Pointer<ArrayRef<UnsignedShortRef>> _00;
  public final UnsignedByteRef _04;
  public final UnsignedByteRef _05;
  public final ArrayRef<ByteRef> _06;

  public WMapStruct0c(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(4, 0x00L).cast(Pointer.deferred(4, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new)));
    this._04 = ref.offset(1, 0x04L).cast(UnsignedByteRef::new);
    this._05 = ref.offset(1, 0x05L).cast(UnsignedByteRef::new);
    this._06 = ref.offset(1, 0x06L).cast(ArrayRef.of(ByteRef.class, 4, 1, ByteRef::new));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
