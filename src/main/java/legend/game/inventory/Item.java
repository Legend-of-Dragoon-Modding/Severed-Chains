package legend.game.inventory;

import legend.game.modding.registries.RegistryEntry;
import legend.game.modding.registries.RegistryId;

public abstract class Item extends RegistryEntry {
  public final String name;
  public final String description;
  public final int price;

  public Item(final RegistryId id, final String name, final String description, final int price) {
    super(id);
    this.name = name;
    this.description = description;
    this.price = price;
  }

  public abstract int getIcon();

  public boolean isEquippable() {
    return false;
  }

  /** TODO Can only be called when S_ITEM is loaded */
  public boolean isEquippableBy(final int charId) {
    return false;
  }

  public EquipmentSlot getEquipmentSlot() {
    return null;
  }

  public boolean canBeDiscarded() {
    return true;
  }

  public int getUseFlags() {
    return 0;
  }

  public boolean canBeUsedNow() {
    return false;
  }

  public void use(final UseItemResponse response, final int charId) {

  }
}
