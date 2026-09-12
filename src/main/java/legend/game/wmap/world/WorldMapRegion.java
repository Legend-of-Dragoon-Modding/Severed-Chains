package legend.game.wmap.world;

import legend.game.wmap.Continent;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.Locale;
import java.util.Objects;
import java.util.function.Supplier;

/** A separately loaded world map. The template preserves retail script/audio conventions. */
public record WorldMapRegion(Continent legacyTemplate, WorldMapModelProvider model, WorldMapCameraSettings camera, Supplier<WorldMapPresentationController> presentation) {
  public WorldMapRegion {
    Objects.requireNonNull(legacyTemplate, "legacyTemplate");
    Objects.requireNonNull(model, "model");
    Objects.requireNonNull(camera, "camera");
    Objects.requireNonNull(presentation, "presentation");
    if(legacyTemplate == Continent.NONE_8) {
      throw new IllegalArgumentException("A world map region requires a playable legacy template");
    }
  }

  public static RegistryId legacyId(final Continent continent) {
    final String name = continent.name();
    return new RegistryId("lod", "wmap_region_" + name.substring(0, name.lastIndexOf('_')).toLowerCase(Locale.ROOT));
  }

  public static RegistryId idFor(final WorldMapPortal portal) {
    return portal.region() == null ? legacyId(portal.continent()) : portal.region();
  }
}
