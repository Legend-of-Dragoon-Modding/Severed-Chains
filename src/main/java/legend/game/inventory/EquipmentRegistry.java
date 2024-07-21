package legend.game.inventory;

import org.legendofdragoon.modloader.registries.MutableRegistry;
import org.legendofdragoon.modloader.registries.RegistryId;

public class EquipmentRegistry extends MutableRegistry<Equipment> {
  public EquipmentRegistry() {
    super(new RegistryId("lod-core", "equipment"));
  }
}
