package legend.game.types;

import legend.game.inventory.InventoryEntry;

import java.util.function.Function;
import java.util.function.ToIntFunction;

public class MenuEntryStruct04<T> {
  private final Function<T, String> nameTranslationKey;
  private final Function<T, String> descriptionTranslationKey;
  private final ToIntFunction<T> icon;

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

  public MenuEntryStruct04(final Function<T, String> nameTranslationKey, final Function<T, String> descriptionTranslationKey, final ToIntFunction<T> icon, final T entry) {
    this.nameTranslationKey = nameTranslationKey;
    this.descriptionTranslationKey = descriptionTranslationKey;
    this.icon = icon;
    this.item_00 = entry;
  }

  public static <T extends InventoryEntry> MenuEntryStruct04<T> make(final T entry) {
    return new MenuEntryStruct04<>(InventoryEntry::getNameTranslationKey, InventoryEntry::getDescriptionTranslationKey, InventoryEntry::getIcon, entry);
  }

  public String getNameTranslationKey() {
    return this.nameTranslationKey.apply(this.item_00);
  }

  public String getDescriptionTranslationKey() {
    return this.descriptionTranslationKey.apply(this.item_00);
  }

  public int getIcon() {
    return this.icon.applyAsInt(this.item_00);
  }
}
