package legend.game.inventory;

import org.legendofdragoon.modloader.events.registries.RegistryEvent;
import org.legendofdragoon.modloader.registries.MutableRegistry;

public class EquipmentRegistryEvent extends RegistryEvent.Register<Equipment> {
  public EquipmentRegistryEvent(final MutableRegistry<Equipment> registry) {
    super(registry);
  }
}
