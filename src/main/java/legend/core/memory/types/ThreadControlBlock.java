package legend.core.memory.types;

import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedIntRef;

/**
 * c0 bytes long
 */
public class ThreadControlBlock implements MemoryRef {
  private final Value ref;

  public final UnsignedIntRef status;
  public final UnsignedIntRef unused;
  public final ArrayRef<UnsignedIntRef> registers;
  public final UnsignedIntRef cop0r14Epc;
  public final UnsignedIntRef hi;
  public final UnsignedIntRef lo;
  public final UnsignedIntRef cop0r12Sr;
  public final UnsignedIntRef cop0r13Cause;
  // Additional 24h bytes, unused/uninitialized

  public ThreadControlBlock(final Value ref) {
    this.ref = ref;

    this.status = ref.offset(4, 0x0L).cast(UnsignedIntRef::new);
    this.unused = ref.offset(4, 0x4L).cast(UnsignedIntRef::new);
    this.registers = ref.offset(4, 0x8L).cast(ArrayRef.of(UnsignedIntRef.class, 0x20, 4, UnsignedIntRef::new));
    this.cop0r14Epc = ref.offset(4, 0x88L).cast(UnsignedIntRef::new);
    this.hi = ref.offset(4, 0x8cL).cast(UnsignedIntRef::new);
    this.lo = ref.offset(4, 0x90L).cast(UnsignedIntRef::new);
    this.cop0r12Sr = ref.offset(4, 0x94L).cast(UnsignedIntRef::new);
    this.cop0r13Cause = ref.offset(4, 0x98L).cast(UnsignedIntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
