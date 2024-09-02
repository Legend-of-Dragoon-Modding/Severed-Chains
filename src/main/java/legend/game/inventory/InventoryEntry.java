package legend.game.inventory;

public interface InventoryEntry {
  int getIcon();
  String getNameTranslationKey();
  String getDescriptionTranslationKey();
  int getPrice();
}
