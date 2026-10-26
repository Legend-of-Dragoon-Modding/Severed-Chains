package legend.game.wmap.world;

import legend.game.tmd.UvAdjustmentMetrics14;
import org.legendofdragoon.modloader.registries.RegistryId;
import java.util.Objects;

/** Stable texture identity independent of retail array slots. */
public record WorldMapPresentationTexture(RegistryId id, UvAdjustmentMetrics14 adjustment) {
  public WorldMapPresentationTexture {
    Objects.requireNonNull(id, "id");
    Objects.requireNonNull(adjustment, "adjustment");
  }
}
