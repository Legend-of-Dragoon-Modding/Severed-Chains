package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;

public class BttlStruct08 implements MemoryRef {
  private final Value ref;

  /** Pretty sure this can point to different file types */
  public final UnsignedIntRef ptr_00;
  public final UnsignedByteRef _04;
  public final UnsignedByteRef _05;
  public final UnsignedByteRef _06;

  public BttlStruct08(final Value ref) {
    this.ref = ref;

    this.ptr_00 = ref.offset(4, 0x00L).cast(UnsignedIntRef::new);
    this._04 = ref.offset(1, 0x04L).cast(UnsignedByteRef::new);
    this._05 = ref.offset(1, 0x05L).cast(UnsignedByteRef::new);
    this._06 = ref.offset(1, 0x06L).cast(UnsignedByteRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
