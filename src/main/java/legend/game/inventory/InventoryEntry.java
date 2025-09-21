package legend.game.inventory;

import legend.game.types.Renderable58;
import org.legendofdragoon.modloader.registries.RegistryId;

public interface InventoryEntry {
  RegistryId getRegistryId();
  ItemIcon getIcon();
  String getNameTranslationKey();
  String getDescriptionTranslationKey();
  int getPrice();
  int getSize();
  int getMaxSize();

  default Renderable58 renderIcon(final int x, final int y, final int flags) {
    return this.getIcon().render(x, y, flags);
  }
}
