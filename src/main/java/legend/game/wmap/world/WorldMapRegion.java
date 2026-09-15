package legend.game.wmap.world;

import legend.game.wmap.Continent;
import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * A separately loaded world map identified by registry ID instead of a legacy template.
 * Legacy regions retain their playable native {@link Continent}; independent regions provide
 * their scene anchor directly and require no native TMD.
 * Model coordinates must align with authored graph points; no terrain height fitting is implied.
 */
public record WorldMapRegion(@Nullable Continent legacyTemplate, WorldMapModelProvider model, WorldMapCameraSettings camera, Supplier<WorldMapPresentationController> presentation, WorldMapScene scene) {
  public WorldMapRegion {
    Objects.requireNonNull(model, "model");
    Objects.requireNonNull(camera, "camera");
    Objects.requireNonNull(presentation, "presentation");
    Objects.requireNonNull(scene, "scene");
  }

  /** Preserves retail map assets, transforms, animations, and presentation behavior. */
  public WorldMapRegion(final Continent legacyTemplate, final WorldMapModelProvider model, final WorldMapCameraSettings camera, final Supplier<WorldMapPresentationController> presentation) {
    this(Objects.requireNonNull(legacyTemplate, "legacyTemplate"), model, camera, presentation, WorldMapScene.identity());
    if(legacyTemplate == Continent.NONE_8) throw new IllegalArgumentException("A world map region requires a playable legacy template");
  }

  /** Creates a region whose provider owns all map rendering and scene anchoring. */
  public static WorldMapRegion independent(final WorldMapModelProvider model, final WorldMapCameraSettings camera, final Supplier<WorldMapPresentationController> presentation, final WorldMapScene scene) {
    return new WorldMapRegion(null, model, camera, presentation, scene);
  }

  public boolean hasLegacyTemplate() {
    return this.legacyTemplate != null;
  }

  public static RegistryId legacyId(final Continent continent) {
    final String name = continent.name();
    return new RegistryId("lod", "wmap_region_" + name.substring(0, name.lastIndexOf('_')).toLowerCase(Locale.ROOT));
  }

  public static RegistryId idFor(final WorldMapPortal portal) {
    return portal.region() == null ? legacyId(portal.continent()) : portal.region();
  }
}
