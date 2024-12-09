package legend.game.modding.events.inventory;

import legend.game.inventory.Item;
import org.legendofdragoon.modloader.events.Event;

public class GiveItemEvent extends Event {
  public Item item;
  public boolean override = false;

  public GiveItemEvent(final Item item) {
    this.item = item;
  }
}