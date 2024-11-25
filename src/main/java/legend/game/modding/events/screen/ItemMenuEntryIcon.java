package legend.game.modding.events.screen;

import legend.game.inventory.Item;
import org.legendofdragoon.modloader.events.Event;

public class ItemMenuEntryIcon extends Event {
  public final Item item;
  public int icon;

  public ItemMenuEntryIcon(final Item item) {
    this.item = item;
    this.icon = item.getIcon();
  }
}
