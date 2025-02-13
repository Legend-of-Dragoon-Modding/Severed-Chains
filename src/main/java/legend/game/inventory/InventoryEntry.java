package legend.game.inventory;

public interface InventoryEntry {
  ItemIcon getIcon();
  String getNameTranslationKey();
  String getDescriptionTranslationKey();
  int getPrice();
}
