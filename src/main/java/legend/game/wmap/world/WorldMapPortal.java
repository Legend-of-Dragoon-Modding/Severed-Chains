package legend.game.wmap.world;

import legend.game.wmap.Continent;
import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;
import java.util.Objects;

/** One legacy portal row. Rows are retained even when they are padding or otherwise invalid for travel. */
public record WorldMapPortal(RegistryId id, int legacyIndex, @Nullable RegistryId route, @Nullable RegistryId place,
                             SubmapEndpoint from, SubmapEndpoint to, int junctionIndex, Continent continent,
                             boolean fullBrightness, int effectFlags, @Nullable RegistryId region,
                             @Nullable RegistryId fromId, @Nullable RegistryId toId,
                             @Nullable WorldMapPresentation.Atmosphere atmosphere, @Nullable WorldMapPresentation.Smoke smoke) {
  public WorldMapPortal(final RegistryId id, final int legacyIndex, @Nullable final RegistryId route, @Nullable final RegistryId place,
                        final SubmapEndpoint from, final SubmapEndpoint to, final int junctionIndex, final Continent continent,
                        final boolean fullBrightness, final int effectFlags) {
    this(id, legacyIndex, route, place, from, to, junctionIndex, continent, fullBrightness, effectFlags, null, null, null, null, null);
  }

  public WorldMapPortal(final RegistryId id, final int legacyIndex, @Nullable final RegistryId route, @Nullable final RegistryId place,
                        final SubmapEndpoint from, final SubmapEndpoint to, final int junctionIndex, final Continent continent,
                        final boolean fullBrightness, final int effectFlags, @Nullable final RegistryId region) {
    this(id, legacyIndex, route, place, from, to, junctionIndex, continent, fullBrightness, effectFlags, region, null, null, null, null);
  }

  public WorldMapPortal(final RegistryId id, @Nullable final RegistryId route, @Nullable final RegistryId place,
                        final SubmapEndpoint from, final SubmapEndpoint to, final int junctionIndex, final Continent continent,
                        final boolean fullBrightness, final int effectFlags) {
    this(id, -1, route, place, from, to, junctionIndex, continent, fullBrightness, effectFlags);
  }

  public WorldMapPortal withLegacyIndex(final int legacyIndex) {
    return new WorldMapPortal(this.id, legacyIndex, this.route, this.place, this.from, this.to, this.junctionIndex, this.continent,
      this.fullBrightness, this.effectFlags, this.region, this.fromId, this.toId, this.atmosphere, this.smoke);
  }

  public WorldMapPortal withRegion(@Nullable final RegistryId region) {
    return new WorldMapPortal(this.id, this.legacyIndex, this.route, this.place, this.from, this.to, this.junctionIndex, this.continent,
      this.fullBrightness, this.effectFlags, region, this.fromId, this.toId, this.atmosphere, this.smoke);
  }

  public WorldMapPortal {
    Objects.requireNonNull(id, "id");
    Objects.requireNonNull(from, "from");
    Objects.requireNonNull(to, "to");
    Objects.requireNonNull(continent, "continent");
    if(atmosphere != null) effectFlags = effectFlags & ~0x30 | switch(atmosphere) {
      case NONE -> 0;
      case CLOUDS -> 0x10;
      case SNOW -> 0x20;
    };
    if(smoke != null) effectFlags = effectFlags & ~0x0c | switch(smoke) {
      case NONE -> 0;
      case MODE_1 -> 4;
      case MODE_2 -> 8;
    };
  }

  public WorldMapPortal withDestination(final SubmapEndpoint destination) {
    return new WorldMapPortal(this.id, this.legacyIndex, this.route, this.place, this.from, destination, this.junctionIndex, this.continent,
      this.fullBrightness, this.effectFlags, this.region, this.fromId, null, this.atmosphere, this.smoke);
  }

  public WorldMapPortal withDestinationId(final RegistryId destinationId) {
    return new WorldMapPortal(this.id, this.legacyIndex, this.route, this.place, this.from, this.to, this.junctionIndex, this.continent,
      this.fullBrightness, this.effectFlags, this.region, this.fromId, Objects.requireNonNull(destinationId, "destinationId"), this.atmosphere, this.smoke);
  }

  public WorldMapPortal withSource(final SubmapEndpoint source) {
    return new WorldMapPortal(this.id, this.legacyIndex, this.route, this.place, source, this.to, this.junctionIndex, this.continent,
      this.fullBrightness, this.effectFlags, this.region, null, this.toId, this.atmosphere, this.smoke);
  }

  public WorldMapPortal withSourceId(final RegistryId sourceId) {
    return new WorldMapPortal(this.id, this.legacyIndex, this.route, this.place, this.from, this.to, this.junctionIndex, this.continent,
      this.fullBrightness, this.effectFlags, this.region, Objects.requireNonNull(sourceId, "sourceId"), this.toId, this.atmosphere, this.smoke);
  }

  public WorldMapPortal withRoute(final RegistryId route) {
    return new WorldMapPortal(this.id, this.legacyIndex, Objects.requireNonNull(route, "route"), this.place, this.from, this.to,
      this.junctionIndex, this.continent, this.fullBrightness, this.effectFlags, this.region, this.fromId, this.toId, this.atmosphere, this.smoke);
  }

  public WorldMapPortal withPlace(final RegistryId place) {
    return new WorldMapPortal(this.id, this.legacyIndex, this.route, Objects.requireNonNull(place, "place"), this.from, this.to,
      this.junctionIndex, this.continent, this.fullBrightness, this.effectFlags, this.region, this.fromId, this.toId, this.atmosphere, this.smoke);
  }

  public WorldMapPortal withPresentation(final WorldMapPresentation presentation) {
    Objects.requireNonNull(presentation, "presentation");
    return new WorldMapPortal(this.id, this.legacyIndex, this.route, this.place, this.from, this.to, this.junctionIndex, this.continent,
      presentation.fullBrightness(), this.effectFlags, this.region, this.fromId, this.toId, presentation.atmosphere(), presentation.smoke());
  }
}
