package legend.game.modding.events.inventory;

import legend.game.inventory.Item;
import org.legendofdragoon.modloader.events.Event;

public class TakeItemEvent extends Event {
  public final Item item;
  public int itemSlot;
  public boolean takeItem;

  public TakeItemEvent(final Item item, final int itemSlot, final boolean takeItem) {
    this.item = item;
    this.itemSlot = itemSlot;
    this.takeItem = takeItem;
  }
}
