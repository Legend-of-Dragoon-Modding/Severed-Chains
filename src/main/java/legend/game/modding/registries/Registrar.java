package legend.game.modding.registries;

import legend.game.modding.events.registries.RegistryEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class Registrar<Type extends RegistryEntry, EventType extends RegistryEvent.Register<Type>> {
  private final Registry<Type> registry;
  private final String modId;

  private final Map<RegistryId, Supplier<Type>> entries = new HashMap<>();

  public Registrar(final Registry<Type> registry, final String modId) {
    this.registry = registry;
    this.modId = modId;
  }

  @SuppressWarnings("unchecked")
  public <T extends Type> RegistryDelegate<T> register(final String entryId, final Supplier<T> entry) {
    final RegistryId id = new RegistryId(this.modId, entryId);

    if(this.entries.containsKey(id)) {
      throw new DuplicateRegistryIdException("Registry ID " + id + " already registered");
    }

    this.entries.put(id, (Supplier<Type>)entry);
    return (RegistryDelegate<T>)this.registry.getEntry(id);
  }

  public void registryEvent(final EventType registryEvent) {
    this.entries.forEach((entryId, supplier) -> registryEvent.register(entryId, supplier.get()));
  }
}
