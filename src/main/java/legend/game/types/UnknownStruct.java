package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedIntRef;

public class UnknownStruct implements MemoryRef {
  private final Value ref;

  /** Seems to point to something 0x10 bytes long */
  public final UnsignedIntRef addr_ui00;
  /** Number of 0x10-byte structures pointed to? */
  public final UnsignedIntRef size_ui04;

  public UnknownStruct(final Value ref) {
    this.ref = ref;

    this.addr_ui00 = ref.offset(4, 0x0L).cast(UnsignedIntRef::new);
    this.size_ui04 = ref.offset(4, 0x4L).cast(UnsignedIntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
