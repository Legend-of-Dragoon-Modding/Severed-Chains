package legend.game.modding.registries;

public class MutableRegistry<Type extends RegistryEntry> extends Registry<Type> {
  private boolean locked;

  public Type register(final RegistryId id, final Type entry) {
    if(this.locked) {
      throw new RegistryLockedException();
    }

    if(this.entries.containsKey(id)) {
      throw new DuplicateRegistryIdException("Registry ID " + id + " already registered");
    }

    entry.setRegistryId(id);
    this.entries.put(id, entry);
    return entry;
  }

  void lock() {
    this.locked = true;
  }

  void reset() {
    this.locked = false;

    for(final RegistryDelegate<Type> delegate : this.delegates.values()) {
      delegate.clear();
    }

    this.entries.clear();
    this.delegates.clear();
  }
}
