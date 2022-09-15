package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedShortRef;

public class MagicStuff08 implements MemoryRef {
  private final Value ref;

  public final UnsignedShortRef mp_00;
  public final ByteRef spellIndex_02;
  public final UnsignedByteRef _03;
  public final UnsignedByteRef dragoonAttack_04;
  public final UnsignedByteRef dragoonMagicAttack_05;
  public final UnsignedByteRef dragoonDefence_06;
  public final UnsignedByteRef dragoonMagicDefence_07;

  public MagicStuff08(final Value ref) {
    this.ref = ref;

    this.mp_00 = ref.offset(2, 0x00L).cast(UnsignedShortRef::new);
    this.spellIndex_02 = ref.offset(1, 0x02L).cast(ByteRef::new);
    this._03 = ref.offset(1, 0x03L).cast(UnsignedByteRef::new);
    this.dragoonAttack_04 = ref.offset(1, 0x04L).cast(UnsignedByteRef::new);
    this.dragoonMagicAttack_05 = ref.offset(1, 0x05L).cast(UnsignedByteRef::new);
    this.dragoonDefence_06 = ref.offset(1, 0x06L).cast(UnsignedByteRef::new);
    this.dragoonMagicDefence_07 = ref.offset(1, 0x07L).cast(UnsignedByteRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
