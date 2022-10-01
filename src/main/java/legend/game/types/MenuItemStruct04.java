package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedShortRef;

public class MenuItemStruct04 implements MemoryRef {
  private final Value ref;

  public final UnsignedByteRef itemId_00;
  public final UnsignedByteRef itemSlot_01;
  public final UnsignedShortRef price_02;

  public MenuItemStruct04(final Value ref) {
    this.ref = ref;

    this.itemId_00 = ref.offset(1, 0x00L).cast(UnsignedByteRef::new);
    this.itemSlot_01 = ref.offset(1, 0x01L).cast(UnsignedByteRef::new);
    this.price_02 = ref.offset(2, 0x02L).cast(UnsignedShortRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
