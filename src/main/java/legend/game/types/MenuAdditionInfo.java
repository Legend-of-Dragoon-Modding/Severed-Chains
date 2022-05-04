package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.MemoryRef;

public class MenuAdditionInfo implements MemoryRef {
  private final Value ref;

  public final ByteRef offset_00;
  public final ByteRef index_01;

  public MenuAdditionInfo(final Value ref) {
    this.ref = ref;

    this.offset_00 = ref.offset(1, 0x00L).cast(ByteRef::new);
    this.index_01 = ref.offset(1, 0x01L).cast(ByteRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
