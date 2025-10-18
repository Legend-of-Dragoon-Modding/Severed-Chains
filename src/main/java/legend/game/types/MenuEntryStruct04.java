package legend.game.types;

import legend.game.inventory.InventoryEntry;

public class MenuEntryStruct04<T extends InventoryEntry> {
  public final T item_00;
  public int itemSlot_01;
  /**
   * <ul>
   *   <li>Low bits are the ID of the character this equipment is equipped on</li>
   *   <li>0x1000 - Equipped</li>
   *   <li>0x2000 - Can't be discarded</li>
   *   <li>0x4000 - Cures status, but no character is afflicted</li>
   * </ul>
   */
  public int flags_02;

  public MenuEntryStruct04(final T entry) {
    this.item_00 = entry;
  }

  public String getNameTranslationKey() {
    return this.item_00.getNameTranslationKey();
  }

  public String getDescriptionTranslationKey() {
    return this.item_00.getDescriptionTranslationKey();
  }

  public int getSize() {
    return this.item_00.getSize();
  }

  public int getMaxSize() {
    return this.item_00.getMaxSize();
  }

  public boolean isEmpty() {
    return this.item_00.isEmpty();
  }
}
