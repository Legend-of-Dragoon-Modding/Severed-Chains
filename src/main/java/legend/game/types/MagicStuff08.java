package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedByteRef;

public class MagicStuff08 implements MemoryRef {
  private final Value ref;

  public final UnsignedByteRef mp_00;

  public final ByteRef spellIndex_02;

  public MagicStuff08(final Value ref) {
    this.ref = ref;

    this.mp_00 = ref.offset(1, 0x00L).cast(UnsignedByteRef::new);

    this.spellIndex_02 = ref.offset(1, 0x02L).cast(ByteRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
