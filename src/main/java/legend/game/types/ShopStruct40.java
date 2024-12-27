package legend.game.types;

import legend.game.inventory.InventoryEntry;
import org.legendofdragoon.modloader.registries.RegistryEntry;

import java.util.Arrays;
import java.util.function.Supplier;

public class ShopStruct40 extends RegistryEntry {
  public final int shopType_00;
  private final Supplier<InventoryEntry>[] items_00;

  @SafeVarargs
  public ShopStruct40(final int shopType, final Supplier<InventoryEntry>... items) {
    this.shopType_00 = shopType;
    this.items_00 = Arrays.copyOf(items, items.length);
  }

  public InventoryEntry getItem(int index) {
    if(index < 0 || index >= items_00.length) {
      throw new IndexOutOfBoundsException("Invalid shop item index: " + index);
    }
    return items_00[index].get();
  }

  public int getItemCount() {
    return items_00.length;
  }
}
