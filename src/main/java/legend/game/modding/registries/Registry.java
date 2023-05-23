package legend.game.modding.registries;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Registry<Type extends RegistryEntry> implements Iterable<RegistryId> {
  protected final Map<RegistryId, Type> entries = new HashMap<>();
  protected final Map<RegistryId, RegistryDelegate<Type>> delegates = new HashMap<>();

  public boolean hasEntry(final RegistryId id) {
    return this.entries.containsKey(id);
  }

  public RegistryDelegate<Type> getEntry(final RegistryId id) {
    return this.delegates.computeIfAbsent(id, key -> new RegistryDelegate<>(id, (Class<Registry<Type>>)this.getClass(), () -> this.entries.get(key)));
  }

  public RegistryDelegate<Type> getEntry(final String id) {
    return this.getEntry(RegistryId.of(id));
  }

  public RegistryDelegate<Type> getEntry(final String modId, final String entryId) {
    return this.getEntry(new RegistryId(modId, entryId));
  }

  @Override
  public Iterator<RegistryId> iterator() {
    return this.entries.keySet().iterator();
  }
}
