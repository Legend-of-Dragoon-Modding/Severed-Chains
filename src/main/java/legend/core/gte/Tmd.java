package legend.core.gte;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnboundedArrayRef;

public class Tmd implements MemoryRef {
  private final Value ref;

  public final TmdHeader header;
  public final UnboundedArrayRef<TmdObjTable> objTable;

  public Tmd(final Value ref) {
    this.ref = ref;

    this.header = ref.offset(4, 0x0L).cast(TmdHeader::new);
    this.objTable = ref.offset(4, 0x8L).cast(UnboundedArrayRef.of(0x1c, TmdObjTable::new, this::getLength));
  }

  private int getLength() {
    return (int)this.header.nobj.get();
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
