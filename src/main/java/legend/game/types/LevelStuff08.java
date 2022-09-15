package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedShortRef;

public class LevelStuff08 implements MemoryRef {
  private final Value ref;

  public final UnsignedShortRef hp_00;
  public final ByteRef addition_02;
  public final UnsignedByteRef bodySpeed_03;
  public final UnsignedByteRef bodyAttack_04;
  public final UnsignedByteRef bodyMagicAttack_05;
  public final UnsignedByteRef bodyDefence_06;
  public final UnsignedByteRef bodyMagicDefence_07;

  public LevelStuff08(final Value ref) {
    this.ref = ref;

    this.hp_00 = ref.offset(2, 0x00L).cast(UnsignedShortRef::new);
    this.addition_02 = ref.offset(1, 0x02L).cast(ByteRef::new);
    this.bodySpeed_03 = ref.offset(1, 0x03L).cast(UnsignedByteRef::new);
    this.bodyAttack_04 = ref.offset(1, 0x04L).cast(UnsignedByteRef::new);
    this.bodyMagicAttack_05 = ref.offset(1, 0x05L).cast(UnsignedByteRef::new);
    this.bodyDefence_06 = ref.offset(1, 0x06L).cast(UnsignedByteRef::new);
    this.bodyMagicDefence_07 = ref.offset(1, 0x07L).cast(UnsignedByteRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
