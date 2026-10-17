package legend.game.wmap.world;

import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.Objects;

/** Stable endpoint identity shared only by exactly matching path endpoints. */
public record WorldMapNode(RegistryId id, WorldMapPoint position) {
  public WorldMapNode {
    Objects.requireNonNull(id, "id");
    Objects.requireNonNull(position, "position");
  }
}
