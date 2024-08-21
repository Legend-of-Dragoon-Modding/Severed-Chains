package legend.game.modding.events.inventory;

import legend.game.inventory.Item;
import org.legendofdragoon.modloader.events.Event;

public class RepeatItemReturnEvent extends Event {
  public final Item item;
  public boolean returnItem;

  public RepeatItemReturnEvent(final Item item, final boolean returnItem) {
    this.item = item;
    this.returnItem = returnItem;
  }
}
