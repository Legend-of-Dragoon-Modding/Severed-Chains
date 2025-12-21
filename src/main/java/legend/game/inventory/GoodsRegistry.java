package legend.game.inventory;

import org.legendofdragoon.modloader.registries.MutableRegistry;
import org.legendofdragoon.modloader.registries.RegistryId;

public class GoodsRegistry extends MutableRegistry<Good> {
  public GoodsRegistry() {
    super(new RegistryId("lod_core", "goods"));
  }
}
