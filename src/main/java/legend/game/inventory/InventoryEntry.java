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
  boolean isEmpty();

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
