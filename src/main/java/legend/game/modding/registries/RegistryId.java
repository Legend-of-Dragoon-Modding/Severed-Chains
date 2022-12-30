package legend.game.modding.registries;

import java.util.regex.Pattern;

public record RegistryId(String modId, String entryId) {
  private static final Pattern matcher = Pattern.compile("[a-z][a-z0-9_]*:[a-z][a-z0-9_]*");

  public static RegistryId fromFull(final String full) {
    if(!matcher.matcher(full).matches()) {
      throw new IllegalArgumentException("Invalid ID " + full);
    }

    final String[] parts = full.split(":");
    return new RegistryId(parts[0], parts[1]);
  }

  @Override
  public String toString() {
    return this.modId + ':' + this.entryId;
  }
}
