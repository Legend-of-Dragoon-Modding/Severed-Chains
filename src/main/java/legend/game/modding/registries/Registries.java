package legend.game.modding.registries;

import legend.game.inventory.Item;
import legend.game.inventory.ItemRegistry;
import legend.game.inventory.ItemRegistryEvent;
import legend.game.modding.events.EventManager;
import legend.game.modding.events.registries.RegistryEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Registries {
  private final List<MutableRegistry<?>> registries = new ArrayList<>();
  private final List<Function<MutableRegistry<?>, RegistryEvent.Register<?>>> registryEvents = new ArrayList<>();

  public final Registry<Item> items = this.addRegistry(new ItemRegistry(), ItemRegistryEvent::new);

  private <Type extends RegistryEntry> Registry<Type> addRegistry(final Registry<Type> registry, final Function<MutableRegistry<Type>, RegistryEvent.Register<Type>> registryEvent) {
    this.registries.add((MutableRegistry<Type>)registry);
    this.registryEvents.add((Function<MutableRegistry<?>, RegistryEvent.Register<?>>)(Object)registryEvent);
    return registry;
  }

  public class Access {
    public void initialize() {
      for(int i = 0; i < Registries.this.registries.size(); i++) {
        final MutableRegistry<?> registry = Registries.this.registries.get(i);
        EventManager.INSTANCE.postEvent(Registries.this.registryEvents.get(i).apply(registry));
        registry.lock();
      }
    }
  }
}
