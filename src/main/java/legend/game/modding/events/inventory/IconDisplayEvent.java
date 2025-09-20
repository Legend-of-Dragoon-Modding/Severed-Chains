package legend.game.modding.events.inventory;

import legend.game.inventory.ItemIcon;
import org.legendofdragoon.modloader.events.Event;
import org.legendofdragoon.modloader.registries.RegistryEntry;

public class IconDisplayEvent extends Event {
  public final RegistryEntry key;
  public ItemIcon icon;

  /**
   * DEPRECATED: subject to removal, use not recommended. Better ways to do this will be introduced in the future.
   */
  @Deprecated
  public IconDisplayEvent(final RegistryEntry key, final ItemIcon icon) {
    this.key = key;
    this.icon = icon;
  }
}
