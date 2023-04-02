package legend.game.modding.registries;

public record RegistryId(String modId, String entryId) {
  public static RegistryId of(final String id) {
    final String[] parts = id.split(":");

    if(parts.length != 2) {
      throw new IllegalArgumentException("Invalid registry ID " + id);
    }

    return new RegistryId(parts[0], parts[1]);
  }

  @Override
  public String toString() {
    return this.modId + ':' + this.entryId;
  }
}
