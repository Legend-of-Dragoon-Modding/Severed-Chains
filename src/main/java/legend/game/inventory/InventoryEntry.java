package legend.game.inventory;

import legend.game.types.Renderable58;
import org.legendofdragoon.modloader.registries.RegistryId;

public interface InventoryEntry<T extends InventoryEntry<T>> {
  RegistryId getRegistryId();
  ItemIcon getIcon();
  String getNameTranslationKey();
  String getDescriptionTranslationKey();
  @Deprecated(forRemoval = true) // migrate to getBuyPrice() and getSellPrice()
  default int getPrice() {
    return 0;
  }

  default int getBuyPrice() {
    return this.getSellPrice() * 2;
  }

  default int getSellPrice() {
    return this.getPrice();
  }

  int getSize();
  int getMaxSize();
  boolean isEmpty();

  default T copy() {
    return (T)this;
  }

  default Renderable58 renderIcon(final int x, final int y, final int flags) {
    final ItemIcon icon = this.getIcon();

    if(icon == null) {
      return null;
    }

    return icon.render(x, y, flags);
  }

  default Renderable58 renderIconManual(final int x, final int y, final int flags) {
    final ItemIcon icon = this.getIcon();

    if(icon == null) {
      return null;
    }

    return icon.renderManual(x, y, flags);
  }
}
