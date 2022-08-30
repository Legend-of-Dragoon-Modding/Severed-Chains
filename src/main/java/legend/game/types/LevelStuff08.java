package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedShortRef;

public class LevelStuff08 implements MemoryRef {
  private final Value ref;

  public final UnsignedShortRef hp_00;
  public final ByteRef addition_02;

  public LevelStuff08(final Value ref) {
    this.ref = ref;

    this.hp_00 = ref.offset(2, 0x00L).cast(UnsignedShortRef::new);
    this.addition_02 = ref.offset(1, 0x02L).cast(ByteRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
