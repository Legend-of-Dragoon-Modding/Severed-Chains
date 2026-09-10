package legend.game.wmap.world;

import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.Objects;

/** Directed teleport travel link with the destination player's world-map placement. */
public record WorldMapTeleportLink(int order, RegistryId source, RegistryId destination, WorldMapPoint translation) {
  public WorldMapTeleportLink {
    Objects.requireNonNull(source, "source");
    Objects.requireNonNull(destination, "destination");
    Objects.requireNonNull(translation, "translation");
  }
}
