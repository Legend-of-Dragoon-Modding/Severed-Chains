package legend.game.sound;

import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedByteRef;

public class Pan implements MemoryRef {
  private final Value ref;

  public final UnsignedByteRef left_00;
  public final UnsignedByteRef right_01;
  /** Union */
  public final ArrayRef<UnsignedByteRef> val_00;

  public Pan(final Value ref) {
    this.ref = ref;

    this.left_00 = ref.offset(1, 0x00L).cast(UnsignedByteRef::new);
    this.right_01 = ref.offset(1, 0x01L).cast(UnsignedByteRef::new);
    this.val_00 = ref.offset(1, 0x00L).cast(ArrayRef.of(UnsignedByteRef.class, 2, 1, UnsignedByteRef::new));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
