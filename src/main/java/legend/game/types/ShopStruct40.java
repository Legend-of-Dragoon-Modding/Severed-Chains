package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedByteRef;

public class ShopStruct40 implements MemoryRef {
  private final Value ref;

  /** NOTE: overlaps with item array */
  public final UnsignedByteRef shopType_00;
  public final ArrayRef<ItemStruct04> item_00;

  public ShopStruct40(final Value ref) {
    this.ref = ref;
    this.shopType_00 = ref.offset(1, 0x00L).cast(UnsignedByteRef::new);
    this.item_00 = ref.offset(4, 0x00L).cast(ArrayRef.of(ItemStruct04.class, 0x10, 4, ItemStruct04::new));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }

  public static class ItemStruct04 implements MemoryRef {
    private final Value ref;

    // 0x00 shop type on first item
    public final UnsignedByteRef id_01;
    // 0x02 unknown/unused
    // 0x03 unknown/unused

    public ItemStruct04(final Value ref) {
      this.ref = ref;
      this.id_01 = ref.offset(1, 0x01L).cast(UnsignedByteRef::new);
    }

    @Override
    public long getAddress() {
      return this.ref.getAddress();
    }
  }
}
