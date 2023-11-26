package legend.game.inventory;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedShortRef;

public class Addition04 implements MemoryRef {
  private final Value ref;

  public final UnsignedShortRef _00;
  public final UnsignedByteRef spMultiplier_02;
  public final UnsignedByteRef damageMultiplier_03;

  public Addition04(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(2, 0x00L).cast(UnsignedShortRef::new);
    this.spMultiplier_02 = ref.offset(1, 0x02L).cast(UnsignedByteRef::new);
    this.damageMultiplier_03 = ref.offset(1, 0x03L).cast(UnsignedByteRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
