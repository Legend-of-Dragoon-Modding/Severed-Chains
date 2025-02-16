package legend.game.inventory;

import org.legendofdragoon.modloader.events.Event;

import java.util.Map;

/** Used to override which icon is rendered for any given icon */
public class IconMapEvent extends Event {
  private final Map<ItemIcon, ItemIcon> mapping;

  public IconMapEvent(final Map<ItemIcon, ItemIcon> mapping) {
    this.mapping = mapping;
  }

  public IconMapEvent addMapping(final ItemIcon original, final ItemIcon mapped) {
    this.mapping.put(original, mapped);
    return this;
  }
}
