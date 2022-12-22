package legend.game.modding.registries;

public class MutableRegistry<Type extends RegistryEntry> extends Registry<Type> {
  private boolean locked;

  public Type register(final Type entry) {
    if(this.locked) {
      throw new RegistryLockedException();
    }

    this.entries.put(entry.id, entry);
    return entry;
  }

  void lock() {
    this.locked = true;
  }
}
