package legend.game.modding.registries;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Registry<Type extends RegistryEntry> implements Iterable<Type> {
  protected final Map<RegistryId, Type> entries = new HashMap<>();

  public Type getEntry(final RegistryId id) {
    return this.entries.get(id);
  }

  @Override
  public Iterator<Type> iterator() {
    return this.entries.values().iterator();
  }
}
