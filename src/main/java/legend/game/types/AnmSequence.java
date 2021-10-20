package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedShortRef;

public class AnmSequence implements MemoryRef {
  private final Value ref;

  public final UnsignedShortRef spriteGroupNumber_00;
  public final UnsignedByteRef time_02;
  public final UnsignedByteRef attr_03;
  public final UnsignedShortRef x_04;
  public final UnsignedShortRef y_06;

  public AnmSequence(final Value ref) {
    this.ref = ref;

    this.spriteGroupNumber_00 = ref.offset(2, 0x0L).cast(UnsignedShortRef::new);
    this.time_02 = ref.offset(1, 0x2L).cast(UnsignedByteRef::new);
    this.attr_03 = ref.offset(1, 0x3L).cast(UnsignedByteRef::new);
    this.x_04 = ref.offset(2, 0x4L).cast(UnsignedShortRef::new);
    this.y_06 = ref.offset(2, 0x6L).cast(UnsignedShortRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
