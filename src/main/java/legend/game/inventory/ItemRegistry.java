package legend.game.inventory;

import org.legendofdragoon.modloader.registries.MutableRegistry;
import org.legendofdragoon.modloader.registries.RegistryId;

public class ItemRegistry extends MutableRegistry<Item> {
  public ItemRegistry() {
    super(new RegistryId("lod_core", "items"));
  }
}
