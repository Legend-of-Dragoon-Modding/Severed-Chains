package legend.game.wmap.world;

import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.Objects;

/** One selectable Coolon destination, including its world position and menu presentation. */
public record WorldMapCoolonDestination(int order, RegistryId portal, RegistryId defaultDestination, WorldMapPoint position, int x, int y, String label, boolean worldMapArrival) {
  public WorldMapCoolonDestination {
    Objects.requireNonNull(portal, "portal");
    Objects.requireNonNull(defaultDestination, "defaultDestination");
    Objects.requireNonNull(position, "position");
    Objects.requireNonNull(label, "label");
  }
}
