package legend.game.modding.events.inventory;

import legend.game.modding.events.Event;

public class TakeItemEvent extends Event {
  public final int itemId;
  public boolean takeItem;

  public TakeItemEvent(final int itemId, final boolean takeItem) {
    this.itemId = itemId;
    this.takeItem = takeItem;
  }
}
