package legend.game.modding.registries;

import java.util.HashMap;
import java.util.Map;

public class Registry<Type extends RegistryEntry> {
  protected final Map<RegistryId, Type> entries = new HashMap<>();

  public Type getEntry(final RegistryId id) {
    return this.entries.get(id);
  }
}
