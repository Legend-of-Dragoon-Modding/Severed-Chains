package legend.lodmod;

import legend.core.GameEngine;
import legend.game.wmap.registries.RegisterWorldMapAvatarsEvent;
import legend.game.wmap.registries.RegisterWorldMapGeometryEvent;
import legend.game.wmap.registries.RegisterWorldMapNodesEvent;
import legend.game.wmap.registries.RegisterWorldMapPlacesEvent;
import legend.game.wmap.registries.RegisterWorldMapPortalsEvent;
import legend.game.wmap.registries.RegisterWorldMapRoutesEvent;
import legend.game.wmap.registries.WorldMapGeometryEntry;
import legend.game.wmap.registries.WorldMapNodeEntry;
import legend.game.wmap.registries.WorldMapPlaceEntry;
import legend.game.wmap.registries.WorldMapPortalEntry;
import legend.game.wmap.registries.WorldMapRouteEntry;
import legend.game.wmap.world.LegacyWorldMap;
import legend.game.wmap.world.WorldMapDefinition;
import legend.game.wmap.world.WorldMapGeometry;
import legend.game.wmap.world.WorldMapNode;
import legend.game.wmap.world.WorldMapPlace;
import legend.game.wmap.world.WorldMapPortal;
import legend.game.wmap.world.WorldMapRoute;
import legend.game.wmap.world.WorldMapRouteData;
import org.legendofdragoon.modloader.events.registries.RegistryEvent;
import org.legendofdragoon.modloader.registries.Registrar;
import org.legendofdragoon.modloader.registries.RegistryDelegate;
import org.legendofdragoon.modloader.registries.RegistryEntry;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;

/** Retail graph registration; each registry event imports fresh legacy source data. */
public final class LodWorldMap {
  static void register(final RegisterWorldMapAvatarsEvent event) {
    LodWorldMapAvatars.register(event);
  }
  private LodWorldMap() { }

  private static final Registrar<WorldMapNodeEntry, RegisterWorldMapNodesEvent> NODES = new Registrar<>(GameEngine.REGISTRIES.worldMapNodes, LodMod.MOD_ID);
  private static final Registrar<WorldMapGeometryEntry, RegisterWorldMapGeometryEvent> GEOMETRY = new Registrar<>(GameEngine.REGISTRIES.worldMapGeometry, LodMod.MOD_ID);
  private static final Registrar<WorldMapRouteEntry, RegisterWorldMapRoutesEvent> ROUTES = new Registrar<>(GameEngine.REGISTRIES.worldMapRoutes, LodMod.MOD_ID);
  private static final Registrar<WorldMapPlaceEntry, RegisterWorldMapPlacesEvent> PLACES = new Registrar<>(GameEngine.REGISTRIES.worldMapPlaces, LodMod.MOD_ID);
  private static final Registrar<WorldMapPortalEntry, RegisterWorldMapPortalsEvent> PORTALS = new Registrar<>(GameEngine.REGISTRIES.worldMapPortals, LodMod.MOD_ID);

  private static WorldMapDefinition eventDefinition;

  public static final List<RegistryDelegate<WorldMapNodeEntry>> LEGACY_NODES;
  public static final List<RegistryDelegate<WorldMapGeometryEntry>> LEGACY_GEOMETRY;
  public static final List<RegistryDelegate<WorldMapRouteEntry>> LEGACY_ROUTES;
  public static final List<RegistryDelegate<WorldMapPlaceEntry>> LEGACY_PLACES;
  public static final List<RegistryDelegate<WorldMapPortalEntry>> LEGACY_PORTALS;

  static {
    final WorldMapDefinition initial = LegacyWorldMap.importDefinition();
    LEGACY_NODES = registerEntries(NODES, "wmap_node_", initial.nodes().size(), LodWorldMap::nodesEntry);
    LEGACY_GEOMETRY = registerEntries(GEOMETRY, "wmap_geometry_", initial.geometry().size(), LodWorldMap::geometryEntry);
    LEGACY_ROUTES = registerEntries(ROUTES, "wmap_route_", initial.routes().size(), LodWorldMap::routesEntry);
    LEGACY_PLACES = registerEntries(PLACES, "wmap_place_", initial.places().size(), LodWorldMap::placesEntry);
    LEGACY_PORTALS = registerEntries(PORTALS, "wmap_location_", initial.portals().size(), LodWorldMap::portalsEntry);
  }

  private static <T extends RegistryEntry, E extends RegistryEvent.Register<T>> List<RegistryDelegate<T>> registerEntries(final Registrar<T, E> registrar, final String prefix, final int count, final IntFunction<T> factory) {
    final List<RegistryDelegate<T>> delegates = new ArrayList<>(count);
    for(int i = 0; i < count; i++) {
      final int index = i;
      delegates.add(registrar.register(prefix + index, () -> factory.apply(index)));
    }
    return List.copyOf(delegates);
  }

  private static WorldMapNodeEntry nodesEntry(final int index) {
    final WorldMapNode value = eventDefinition.nodes().get(index);
    return new WorldMapNodeEntry(id -> new WorldMapNode(id, value.position()));
  }

  static void register(final RegisterWorldMapNodesEvent event) {
    eventDefinition = LegacyWorldMap.importDefinition();
    try {
      NODES.registryEvent(event);
    } finally {
      eventDefinition = null;
    }
  }

  private static WorldMapGeometryEntry geometryEntry(final int index) {
    final WorldMapGeometry value = new WorldMapGeometry(index, eventDefinition.geometry().get(index));
    return new WorldMapGeometryEntry(id -> value);
  }

  static void register(final RegisterWorldMapGeometryEvent event) {
    eventDefinition = LegacyWorldMap.importDefinition();
    try {
      GEOMETRY.registryEvent(event);
    } finally {
      eventDefinition = null;
    }
  }

  private static WorldMapRouteEntry routesEntry(final int index) {
    final WorldMapRoute value = eventDefinition.route(index);
    return new WorldMapRouteEntry(id -> new WorldMapRouteData(value.legacyIndex(), value.start(), value.end(), new RegistryId(LodMod.MOD_ID, "wmap_geometry_" + value.segmentIndex()), value.direction(), value.encounterRate(), value.battleStage(), value.encounterIndex() < 0 || value.encounterIndex() >= LodWorldMapData.encounterIds_800ef364.length ? null : new RegistryId(LodMod.MOD_ID, "wmap_encounter_pool_" + value.encounterIndex()), value.modelIndex(), value.encounterIndex()));
  }

  static void register(final RegisterWorldMapRoutesEvent event) {
    eventDefinition = LegacyWorldMap.importDefinition();
    try {
      ROUTES.registryEvent(event);
    } finally {
      eventDefinition = null;
    }
  }

  private static WorldMapPlaceEntry placesEntry(final int index) {
    final WorldMapPlace value = eventDefinition.place(index);
    return new WorldMapPlaceEntry(id -> new WorldMapPlace(id, value.legacyIndex(), value.name(), value.thumbnail(), value.services(), value.sounds()));
  }

  static void register(final RegisterWorldMapPlacesEvent event) {
    eventDefinition = LegacyWorldMap.importDefinition();
    try {
      PLACES.registryEvent(event);
    } finally {
      eventDefinition = null;
    }
  }

  private static WorldMapPortalEntry portalsEntry(final int index) {
    final WorldMapPortal value = eventDefinition.portal(index);
    return new WorldMapPortalEntry(id -> new WorldMapPortal(id, value.legacyIndex(), value.route(), value.place(), value.from(), value.to(), value.junctionIndex(), value.continent(), value.fullBrightness(), value.effectFlags()));
  }

  static void register(final RegisterWorldMapPortalsEvent event) {
    eventDefinition = LegacyWorldMap.importDefinition();
    try {
      PORTALS.registryEvent(event);
    } finally {
      eventDefinition = null;
    }
  }

}
