package legend.game.modding.events.inventory;

import legend.game.modding.events.Event;

public class RepeatItemReturnEvent extends Event {
  public final int itemId;
  public boolean returnItem;

  public RepeatItemReturnEvent(final int itemId, final boolean returnItem) {
    this.itemId = itemId;
    this.returnItem = returnItem;
  }
}
