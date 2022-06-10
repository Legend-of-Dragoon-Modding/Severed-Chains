package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.RelativePointer;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedIntRef;

public class ScriptFile implements MemoryRef {
  private final Value ref;

  public final ArrayRef<RelativePointer<IntRef>> offsetArr_00;
  public final UnboundedArrayRef<UnsignedIntRef> code_40;

  public ScriptFile(final Value ref) {
    this.ref = ref;

    this.offsetArr_00 = ref.offset(4, 0x00L).cast(ArrayRef.of(RelativePointer.classFor(IntRef.class), 0x10, 4, RelativePointer.deferred(4, ref.getAddress(), IntRef::new)));
    this.code_40 = ref.offset(4, 0x40L).cast(UnboundedArrayRef.of(4, UnsignedIntRef::new));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
