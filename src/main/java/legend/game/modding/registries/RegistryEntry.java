package legend.game.modding.registries;

public class RegistryEntry {
  public final RegistryId id;

  public RegistryEntry(final RegistryId id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return this.getClass().getSimpleName() + ' ' + this.id;
  }
}
