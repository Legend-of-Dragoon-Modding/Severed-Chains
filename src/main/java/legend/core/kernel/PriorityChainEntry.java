package legend.core.kernel;

import legend.core.memory.Value;
import legend.core.memory.types.ConsumerRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.SupplierRef;

public class PriorityChainEntry implements MemoryRef {
  private final Value ref;

  public final Pointer<PriorityChainEntry> next;
  public final Pointer<ConsumerRef<Integer>> secondFunction;
  public final Pointer<SupplierRef<Integer>> firstFunction;
  public final IntRef unknown;

  public PriorityChainEntry(final Value ref) {
    this.ref = ref;
    this.next = this.ref.offset(4, 0x0L).cast(Pointer.of(0x10, PriorityChainEntry::new));
    this.secondFunction = this.ref.offset(4, 0x4L).cast(Pointer.of(4, ConsumerRef::new));
    this.firstFunction = this.ref.offset(4, 0x8L).cast(Pointer.of(4, SupplierRef::new));
    this.unknown = this.ref.offset(4, 0xcL).cast(IntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
