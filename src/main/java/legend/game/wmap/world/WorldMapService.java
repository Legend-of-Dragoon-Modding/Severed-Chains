package legend.game.wmap.world;

import javax.annotation.Nullable;
import java.util.Objects;

/** Named service displayed by a world-map place. */
public record WorldMapService(String label, @Nullable Integer legacyBit) {
  public WorldMapService(final String label) {
    this(label, null);
  }

  public WorldMapService {
    if(Objects.requireNonNull(label, "label").isBlank()) {
      throw new IllegalArgumentException("WMAP service label cannot be blank");
    }
    if(legacyBit != null && (legacyBit < 0 || legacyBit >= 31)) throw new IllegalArgumentException("WMAP service legacy bit out of range");
  }
}
