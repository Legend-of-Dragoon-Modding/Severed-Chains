package legend.game.inventory;

import legend.game.modding.events.registries.RegistryEvent;
import legend.game.modding.registries.MutableRegistry;

public class EquipmentRegistryEvent extends RegistryEvent.Register<Equipment> {
  public EquipmentRegistryEvent(final MutableRegistry<Equipment> registry) {
    super(registry);
  }
}
