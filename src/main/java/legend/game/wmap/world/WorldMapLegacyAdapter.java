package legend.game.wmap.world;

import legend.game.wmap.preset.WorldMapPreset;
import org.legendofdragoon.modloader.registries.RegistryId;

/** Owns native portal-slot assumptions that standalone worlds intentionally do not inherit. */
public final class WorldMapLegacyAdapter {
  private static final int NATIVE_PORTALS = 256;

  private WorldMapLegacyAdapter() { }

  public static boolean isNativeLayout(final WorldMapDefinition definition) {
    if(definition.portals().size() < NATIVE_PORTALS) return false;
    for(int index = 0; index < NATIVE_PORTALS; index++) {
      if(!definition.portal(index).id().equals(new RegistryId("lod", "wmap_location_" + index))) return false;
    }
    return true;
  }

  public static void validateRetail(final WorldMapDefinition definition) {
    if(!isNativeLayout(definition)) throw new IllegalArgumentException("Retail WMAP requires the canonical first 256 portal slots");
  }

  public static WorldMapPortal startingPortal(final WorldMapDefinition definition, final WorldMapPreset preset) {
    if(preset == null || !preset.standalone()) return nativePortal(definition);
    return configuredOrFirstUsable(definition, preset.startingPortal(), "starting");
  }

  public static WorldMapPortal recoveryPortal(final WorldMapDefinition definition, final WorldMapPreset preset) {
    if(preset == null || !preset.standalone()) return nativePortal(definition);
    return configuredOrFirstUsable(definition, preset.recoveryPortal() == null ? preset.startingPortal() : preset.recoveryPortal(), "recovery");
  }

  public static SubmapEndpoint nativeArrival(final WorldMapDefinition definition) {
    nativePortal(definition);
    return new SubmapEndpoint(13, 17);
  }

  private static WorldMapPortal nativePortal(final WorldMapDefinition definition) {
    validateRetail(definition);
    final WorldMapPortal portal = definition.portal(5);
    return portal;
  }

  private static WorldMapPortal configuredOrFirstUsable(final WorldMapDefinition definition, final RegistryId id, final String kind) {
    if(id != null) {
      final WorldMapPortal portal = definition.portal(id);
      if(portal.route() == null) throw new IllegalArgumentException("Standalone WMAP " + kind + " portal has no route: " + id);
      return portal;
    }
    return definition.portals().stream().filter(portal -> portal.route() != null).findFirst()
      .orElseThrow(() -> new IllegalArgumentException("Standalone WMAP requires a usable portal for " + kind + " fallback"));
  }
}
