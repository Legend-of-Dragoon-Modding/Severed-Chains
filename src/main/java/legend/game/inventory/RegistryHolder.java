package legend.game.inventory;

import legend.game.modding.registries.Registry;
import legend.game.modding.registries.RegistryEntry;
import legend.game.modding.registries.RegistryId;

public class RegistryHolder<Type extends RegistryEntry> {
  private final Registry<Type> registry;
  private final RegistryId id;

  private Type value;

  public RegistryHolder(final Registry<Type> registry, final RegistryId id) {
    this.registry = registry;
    this.id = id;
  }

  public Type get() {
    if(this.value == null) {
      this.value = this.registry.getEntry(this.id);
    }

    return this.value;
  }
}
