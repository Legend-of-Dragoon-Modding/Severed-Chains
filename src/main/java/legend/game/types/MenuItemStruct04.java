package legend.game.types;

import legend.game.inventory.Item;

public class MenuItemStruct04 {
  public Item item;
  public int slot;
  /**
   * <ul>
   *   <li>0x1000 - Equipped</li>
   *   <li>0x2000 - Can't be discarded</li>
   *   <li>0x4000 - Cures status, but no character is afflicted</li>
   * </ul>
   */
  public int flags;
}
