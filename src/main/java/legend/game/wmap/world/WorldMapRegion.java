package legend.game.wmap.world;

import legend.game.wmap.Continent;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.Locale;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * A separately loaded world map, identified by its registry ID rather than its legacy template.
 * Every region still requires a playable native Continent for retail audio/cinematic conventions.
 * Portals bound to this region must carry the same compatibility continent. If the model provider
 * supplies only a custom renderer, the native template TMD is loaded but not drawn: it anchors
 * existing camera/indicator transforms. Native assets therefore remain a runtime prerequisite.
 * Model coordinates must align with authored graph points; no terrain height fitting is implied.
 */
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
