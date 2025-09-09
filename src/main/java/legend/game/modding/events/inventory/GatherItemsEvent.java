package legend.game.modding.events.inventory;

import legend.game.inventory.Item;
import org.legendofdragoon.modloader.events.Event;

import java.util.ArrayList;
import java.util.List;

public class GatherItemsEvent extends Event {
  private final List<Item> items = new ArrayList<>();

  public void add(final Item item) {
    this.items.add(item);
  }

  public Item[] getItems() {
    return this.items.toArray(Item[]::new);
  }
}
