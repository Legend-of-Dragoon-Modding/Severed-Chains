package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.RunnableRef;
import legend.core.memory.types.UnsignedIntRef;

public class CallbackStruct implements MemoryRef {
  private final Value ref;

  public final Pointer<RunnableRef> callback_00;
  public final UnsignedIntRef ptr_04;
  public final UnsignedIntRef ptr_08;
  public final UnsignedIntRef uint_0c;

  public CallbackStruct(final Value ref) {
    this.ref = ref;

    this.callback_00 = ref.offset(4, 0x0L).cast(Pointer.deferred(4, RunnableRef::new));
    this.ptr_04 = ref.offset(4, 0x4L).cast(UnsignedIntRef::new);
    this.ptr_08 = ref.offset(4, 0x8L).cast(UnsignedIntRef::new);
    this.uint_0c = ref.offset(4, 0xcL).cast(UnsignedIntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
