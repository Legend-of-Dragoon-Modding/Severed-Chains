package legend.game.modding.events.registries;

import legend.game.modding.events.Event;
import legend.game.modding.registries.MutableRegistry;
import legend.game.modding.registries.RegistryEntry;

public abstract class RegistryEvent<Type extends RegistryEntry> extends Event {
  protected final MutableRegistry<Type> registry;

  RegistryEvent(final MutableRegistry<Type> registry) {
    this.registry = registry;
  }

  public static abstract class Register<Type extends RegistryEntry> extends RegistryEvent<Type> {
    public Register(final MutableRegistry<Type> registry) {
      super(registry);
    }

    public Type register(final Type entry) {
      return this.registry.register(entry);
    }
  }
}
