package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.ConsumerRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.UnsignedIntRef;

public class MediumStruct implements MemoryRef {
  private final Value ref;

  public final ArrayRef<UnsignedIntRef> arr_00;
  public final UnsignedIntRef count_40;
  public final UnsignedIntRef _44;
  public final Pointer<ConsumerRef<MediumStruct>> callback_48;

  public MediumStruct(final Value ref) {
    this.ref = ref;

    this.arr_00 = ref.offset(4, 0x00L).cast(ArrayRef.of(UnsignedIntRef.class, 0x10, 4, UnsignedIntRef::new));
    this.count_40 = ref.offset(4, 0x40L).cast(UnsignedIntRef::new);
    this._44 = ref.offset(4, 0x44L).cast(UnsignedIntRef::new);
    this.callback_48 = ref.offset(4, 0x48L).cast(Pointer.deferred(4, ConsumerRef::new));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
