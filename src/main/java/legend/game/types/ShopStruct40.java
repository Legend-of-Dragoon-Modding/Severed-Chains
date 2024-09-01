package legend.game.types;

import legend.game.inventory.InventoryEntry;

import java.util.function.Supplier;

public class ShopStruct40 {
  public final int shopType_00;
  public final Supplier<InventoryEntry>[] items_00;

  @SafeVarargs
  public ShopStruct40(final int shopType, final Supplier<InventoryEntry>... items) {
    this.shopType_00 = shopType;
    this.items_00 = items;
  }
}
