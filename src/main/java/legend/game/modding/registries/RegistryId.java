package legend.game.modding.registries;

public record RegistryId(String modId, String entryId) {
  @Override
  public String toString() {
    return this.modId + ':' + this.entryId;
  }
}
