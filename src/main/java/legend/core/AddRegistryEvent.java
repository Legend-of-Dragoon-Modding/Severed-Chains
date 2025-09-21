package legend.core;

import org.legendofdragoon.modloader.events.Event;
import org.legendofdragoon.modloader.events.registries.RegistryEvent;
import org.legendofdragoon.modloader.registries.MutableRegistry;
import org.legendofdragoon.modloader.registries.Registry;
import org.legendofdragoon.modloader.registries.RegistryEntry;

import java.util.function.Function;

public class AddRegistryEvent extends Event {
  private final Registries registries;

  public AddRegistryEvent(final Registries registries) {
    this.registries = registries;
  }

  public <Type extends RegistryEntry> void addRegistry(final Registry<Type> registry, final Function<MutableRegistry<Type>, RegistryEvent.Register<Type>> registryEvent) {
    this.registries.addRegistry(registry, registryEvent);
  }
}
