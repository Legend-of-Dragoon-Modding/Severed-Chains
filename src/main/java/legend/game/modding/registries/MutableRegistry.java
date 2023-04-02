package legend.game.modding.registries;

public class MutableRegistry<Type extends RegistryEntry> extends Registry<Type> {
  private boolean locked;

  public Type register(final RegistryId id, final Type entry) {
    if(this.locked) {
      throw new RegistryLockedException();
    }

    entry.setRegistryId(id);
    this.entries.put(id, entry);
    return entry;
  }

  void lock() {
    this.locked = true;
  }
}
