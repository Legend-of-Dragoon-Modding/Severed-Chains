package legend.game.wmap.world;

import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.Objects;

/** One selectable Coolon destination, including its world position and menu presentation. */
public record WorldMapCoolonDestination(int order, RegistryId portal, RegistryId defaultDestination, WorldMapPoint position, int x, int y, String label, boolean worldMapArrival, boolean opensMenuOnArrival) {
  public WorldMapCoolonDestination(final int order, final RegistryId portal, final RegistryId defaultDestination, final WorldMapPoint position, final int x, final int y, final String label, final boolean worldMapArrival) {
    this(order, portal, defaultDestination, position, x, y, label, worldMapArrival, false);
  }
  public WorldMapCoolonDestination {
    Objects.requireNonNull(portal, "portal");
    Objects.requireNonNull(defaultDestination, "defaultDestination");
    Objects.requireNonNull(position, "position");
    Objects.requireNonNull(label, "label");
  }
}
