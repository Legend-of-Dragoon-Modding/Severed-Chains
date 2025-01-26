package legend.game.inventory;

import legend.game.types.ShopStruct40;
import org.legendofdragoon.modloader.registries.MutableRegistry;
import org.legendofdragoon.modloader.registries.RegistryId;

public class ShopRegistry extends MutableRegistry<ShopStruct40> {
  public ShopRegistry() {
    super(new RegistryId("lod_core", "shops"));
  }
}
