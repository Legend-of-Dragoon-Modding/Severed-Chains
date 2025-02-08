package legend.game.modding.events.inventory;

import legend.game.inventory.Item;
import org.legendofdragoon.modloader.events.Event;

import java.util.ArrayList;
import java.util.List;

public class GiveItemEvent extends Event {
  public List<Item> items = new ArrayList<>();
  public boolean overflow = false;

  public GiveItemEvent(final Item item) {
    this.items.add(item);
  }
}
