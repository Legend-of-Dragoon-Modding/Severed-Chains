package legend.game.inventory;

import legend.game.types.Shop;
import org.legendofdragoon.modloader.registries.MutableRegistry;
import org.legendofdragoon.modloader.registries.RegistryId;

public class ShopRegistry extends MutableRegistry<Shop> {
  public ShopRegistry() {
    super(new RegistryId("lod_core", "shops"));
  }
}
