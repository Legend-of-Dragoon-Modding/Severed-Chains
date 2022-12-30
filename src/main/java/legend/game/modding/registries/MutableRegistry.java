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

  public void mapId(final int id, final RegistryId registryId) {
    if(this.locked) {
      throw new RegistryLockedException();
    }

    this.idToName.put(id, registryId);
    this.nameToId.put(registryId, id);
  }

  protected int getNextId() {
    for(int i = 0; i < Integer.MAX_VALUE; i++) {
      if(!this.idToName.containsKey(i)) {
        return i;
      }
    }

    throw new RuntimeException("Ran out of IDs");
  }

  void lock() {
    for(final RegistryId registryId : this.entries.keySet()) {
      if(!this.nameToId.containsKey(registryId)) {
        this.mapId(this.getNextId(), registryId);
      }
    }

    this.locked = true;
  }
}
