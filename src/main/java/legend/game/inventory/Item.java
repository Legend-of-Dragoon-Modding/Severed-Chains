package legend.game.inventory;

import legend.game.modding.registries.RegistryEntry;
import legend.game.types.ItemStats0c;

public class Item extends RegistryEntry {
  public final String name;

  public Item(final String name, final ItemStats0c stats) {
    this(name);
  }

  public Item(final String name) {
    this.name = name;
  }
}
