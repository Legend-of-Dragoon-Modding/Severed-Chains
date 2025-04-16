package legend.game.modding.events.screen;

import legend.game.inventory.Item;
import org.legendofdragoon.modloader.events.Event;

/**
 * DEPRECATED: subject to removal, use not recommended. Better ways to do this will be introduced in the future.
 */
@Deprecated
public class ItemDescriptionEvent extends Event {
  public final Item item;
  public String description;

  public ItemDescriptionEvent(final Item item, final String description) {
    this.item = item;
    this.description = description;
  }
}
