package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;

public class TimFile implements MemoryRef {
  private final Value ref;

  public final IntRef id_00;
  public final Value data_04;

  public TimFile(final Value ref) {
    this.ref = ref;

    this.id_00 = ref.offset(4, 0x00L).cast(IntRef::new);
    this.data_04 = ref.offset(4, 0x04L);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
