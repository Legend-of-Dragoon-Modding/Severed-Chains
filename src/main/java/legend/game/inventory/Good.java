package legend.game.inventory;

import legend.game.types.Renderable58;
import org.legendofdragoon.modloader.registries.RegistryEntry;

import javax.annotation.Nullable;

public class Good extends RegistryEntry implements InventoryEntry<Good> {
  public final int sortingIndex;
  @Nullable
  public final ItemIcon icon;

  public Good(final int sortingIndex) {
    this(sortingIndex, null);
  }

  public Good(final int sortingIndex, final ItemIcon icon) {
    this.sortingIndex = sortingIndex;
    this.icon = icon;
  }

  @Nullable
  public ItemIcon getIcon() {
    return this.icon;
  }

  @Override
  public String getNameTranslationKey() {
    return this.getTranslationKey();
  }

  @Override
  public String getDescriptionTranslationKey() {
    return this.getTranslationKey("description");
  }

  @Override
  public int getBuyPrice() {
    return 0;
  }

  @Override
  public int getSellPrice() {
    return 0;
  }

  @Override
  public int getSize() {
    return 1;
  }

  @Override
  public int getMaxSize() {
    return 1;
  }

  @Override
  public boolean isEmpty() {
    return false;
  }

  public Renderable58 renderIcon(final int x, final int y, final int flags) {
    final ItemIcon icon = this.getIcon();

    if(icon == null) {
      return null;
    }

    return icon.render(x, y, flags);
  }

  public Renderable58 renderManualIcon(final int x, final int y, final int flags) {
    final ItemIcon icon = this.getIcon();

    if(icon == null) {
      return null;
    }

    return icon.renderManual(x, y, flags);
  }
}
