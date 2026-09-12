package legend.game.wmap.world;

import legend.game.wmap.Continent;
import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;
import java.util.Objects;

/** One legacy portal row. Rows are retained even when they are padding or otherwise invalid for travel. */
public record WorldMapPortal(RegistryId id, int legacyIndex, @Nullable RegistryId route, @Nullable RegistryId place, SubmapEndpoint from, SubmapEndpoint to, int junctionIndex, Continent continent, boolean fullBrightness, int effectFlags, @Nullable RegistryId region) {
  public WorldMapPortal(final RegistryId id, final int legacyIndex, @Nullable final RegistryId route, @Nullable final RegistryId place, final SubmapEndpoint from, final SubmapEndpoint to, final int junctionIndex, final Continent continent, final boolean fullBrightness, final int effectFlags) {
    this(id, legacyIndex, route, place, from, to, junctionIndex, continent, fullBrightness, effectFlags, null);
  }

  public WorldMapPortal(final RegistryId id, @Nullable final RegistryId route, @Nullable final RegistryId place, final SubmapEndpoint from, final SubmapEndpoint to, final int junctionIndex, final Continent continent, final boolean fullBrightness, final int effectFlags) {
    this(id, -1, route, place, from, to, junctionIndex, continent, fullBrightness, effectFlags);
  }

  public WorldMapPortal withLegacyIndex(final int legacyIndex) {
    return new WorldMapPortal(this.id, legacyIndex, this.route, this.place, this.from, this.to, this.junctionIndex, this.continent, this.fullBrightness, this.effectFlags, this.region);
  }

  public WorldMapPortal withRegion(@Nullable final RegistryId region) {
    return new WorldMapPortal(this.id, this.legacyIndex, this.route, this.place, this.from, this.to, this.junctionIndex, this.continent, this.fullBrightness, this.effectFlags, region);
  }

  public WorldMapPortal {
    Objects.requireNonNull(id, "id");
    Objects.requireNonNull(from, "from");
    Objects.requireNonNull(to, "to");
    Objects.requireNonNull(continent, "continent");
  }

  public WorldMapPortal withDestination(final SubmapEndpoint destination) {
    return new WorldMapPortal(this.id, this.legacyIndex, this.route, this.place, this.from, destination, this.junctionIndex, this.continent, this.fullBrightness, this.effectFlags, this.region);
  }

  public WorldMapPortal withSource(final SubmapEndpoint source) {
    return new WorldMapPortal(this.id, this.legacyIndex, this.route, this.place, source, this.to, this.junctionIndex, this.continent, this.fullBrightness, this.effectFlags, this.region);
  }

  public WorldMapPortal withRoute(final RegistryId route) {
    return new WorldMapPortal(this.id, this.legacyIndex, Objects.requireNonNull(route, "route"), this.place, this.from, this.to, this.junctionIndex, this.continent, this.fullBrightness, this.effectFlags, this.region);
  }

  public WorldMapPortal withPlace(final RegistryId place) {
    return new WorldMapPortal(this.id, this.legacyIndex, this.route, Objects.requireNonNull(place, "place"), this.from, this.to, this.junctionIndex, this.continent, this.fullBrightness, this.effectFlags, this.region);
  }

  public WorldMapPortal withPresentation(final WorldMapPresentation presentation) {
    Objects.requireNonNull(presentation, "presentation");
    final int effectFlags = this.effectFlags & ~0x3c | presentation.atmosphereLegacyMode() | presentation.smoke().legacyMode();
    return new WorldMapPortal(this.id, this.legacyIndex, this.route, this.place, this.from, this.to, this.junctionIndex, this.continent, presentation.fullBrightness(), effectFlags, this.region);
  }
}
