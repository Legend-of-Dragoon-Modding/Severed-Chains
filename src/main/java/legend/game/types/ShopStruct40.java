package legend.game.types;

import legend.core.memory.*;
import legend.core.memory.types.*;

import static legend.core.Hardware.MEMORY;

public class ShopStruct40 implements MemoryRef {
    private final Value ref;
    /** NOTE: overlaps with item array */
    public final UnsignedByteRef shopType_00;
    public final ArrayRef<ItemStruct04> item_00;

    public ShopStruct40(final Value ref) {
        this.ref = ref;
        this.shopType_00 = ref.offset(1, 0x00L).cast(UnsignedByteRef::new);
        this.item_00 = MEMORY.ref(4, this.getAddress(), ArrayRef.of(ItemStruct04.class, 0x40, 4, ItemStruct04::new));
    }

    @Override
    public long getAddress() { return this.ref.getAddress(); }

    public class ItemStruct04 implements MemoryRef {
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
        public long getAddress() { return this.ref.getAddress(); }
    }
}
