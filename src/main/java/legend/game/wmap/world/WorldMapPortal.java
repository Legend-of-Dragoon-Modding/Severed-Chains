package legend.game.wmap.world;

import legend.game.wmap.Continent;
import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;
import java.util.Objects;

/** One legacy portal row. Rows are retained even when they are padding or otherwise invalid for travel. */
public record WorldMapPortal(RegistryId id, int legacyIndex, @Nullable RegistryId route, @Nullable RegistryId place, SubmapEndpoint from, SubmapEndpoint to, int junctionIndex, Continent continent, boolean fullBrightness, int effectFlags) {
  public WorldMapPortal {
    Objects.requireNonNull(id, "id");
    Objects.requireNonNull(from, "from");
    Objects.requireNonNull(to, "to");
    Objects.requireNonNull(continent, "continent");
  }

  public WorldMapPortal withDestination(final SubmapEndpoint destination) {
    return new WorldMapPortal(this.id, this.legacyIndex, this.route, this.place, this.from, destination, this.junctionIndex, this.continent, this.fullBrightness, this.effectFlags);
  }

  public WorldMapPortal withSource(final SubmapEndpoint source) {
    return new WorldMapPortal(this.id, this.legacyIndex, this.route, this.place, source, this.to, this.junctionIndex, this.continent, this.fullBrightness, this.effectFlags);
  }

  public WorldMapPortal withRoute(final RegistryId route) {
    return new WorldMapPortal(this.id, this.legacyIndex, Objects.requireNonNull(route, "route"), this.place, this.from, this.to, this.junctionIndex, this.continent, this.fullBrightness, this.effectFlags);
  }

  public WorldMapPortal withPlace(final RegistryId place) {
    return new WorldMapPortal(this.id, this.legacyIndex, this.route, Objects.requireNonNull(place, "place"), this.from, this.to, this.junctionIndex, this.continent, this.fullBrightness, this.effectFlags);
  }

  public WorldMapPortal withPresentation(final WorldMapPresentation presentation) {
    Objects.requireNonNull(presentation, "presentation");
    final int effectFlags = this.effectFlags & ~0x3c | presentation.atmosphereLegacyMode() | presentation.smoke().legacyMode();
    return new WorldMapPortal(this.id, this.legacyIndex, this.route, this.place, this.from, this.to, this.junctionIndex, this.continent, presentation.fullBrightness(), effectFlags);
  }
}
