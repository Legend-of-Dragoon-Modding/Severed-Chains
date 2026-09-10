package legend.lodmod;

import legend.core.GameEngine;
import legend.game.wmap.CoolonWarpDestination20;
import legend.game.wmap.TeleportationLocation0c;
import legend.game.wmap.WMapDestinationMarker2c;
import legend.game.wmap.WmapStatics;
import legend.game.wmap.registries.RegisterWorldMapCoolonDestinationsEvent;
import legend.game.wmap.registries.RegisterWorldMapEncounterPoolsEvent;
import legend.game.wmap.registries.RegisterWorldMapStoryPresetsEvent;
import legend.game.wmap.registries.RegisterWorldMapTeleportLinksEvent;
import legend.game.wmap.registries.WorldMapCoolonDestinationEntry;
import legend.game.wmap.registries.WorldMapEncounterPoolEntry;
import legend.game.wmap.registries.WorldMapStoryPresetEntry;
import legend.game.wmap.registries.WorldMapTeleportLinkEntry;
import legend.game.wmap.world.LegacyWorldMap;
import legend.game.wmap.world.WorldMapCoolonDestination;
import legend.game.wmap.world.WorldMapDefinition;
import legend.game.wmap.world.WorldMapEncounterPool;
import legend.game.wmap.world.WorldMapPoint;
import legend.game.wmap.world.WorldMapStoryPreset;
import legend.game.wmap.world.WorldMapTeleportLink;
import org.joml.Vector3i;
import org.legendofdragoon.modloader.events.registries.RegistryEvent;
import org.legendofdragoon.modloader.registries.Registrar;
import org.legendofdragoon.modloader.registries.RegistryDelegate;
import org.legendofdragoon.modloader.registries.RegistryEntry;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;

/** Retail registrations for story, transport, and encounter world-map data. */
public final class LodWorldMapTravelData {
  private static final int STORY_PRESET_COUNT = 49;
  private static final Registrar<WorldMapStoryPresetEntry, RegisterWorldMapStoryPresetsEvent> STORY_PRESETS = new Registrar<>(GameEngine.REGISTRIES.worldMapStoryPresets, LodMod.MOD_ID);
  private static final Registrar<WorldMapCoolonDestinationEntry, RegisterWorldMapCoolonDestinationsEvent> COOLON_DESTINATIONS = new Registrar<>(GameEngine.REGISTRIES.worldMapCoolonDestinations, LodMod.MOD_ID);
  private static final Registrar<WorldMapTeleportLinkEntry, RegisterWorldMapTeleportLinksEvent> TELEPORT_LINKS = new Registrar<>(GameEngine.REGISTRIES.worldMapTeleportLinks, LodMod.MOD_ID);
  private static final Registrar<WorldMapEncounterPoolEntry, RegisterWorldMapEncounterPoolsEvent> ENCOUNTER_POOLS = new Registrar<>(GameEngine.REGISTRIES.worldMapEncounterPools, LodMod.MOD_ID);

  private static WorldMapDefinition eventDefinition;

  public static final List<RegistryDelegate<WorldMapStoryPresetEntry>> LEGACY_STORY_PRESETS = registerEntries(STORY_PRESETS, "wmap_story_preset_", STORY_PRESET_COUNT, LodWorldMapTravelData::storyPresetEntry);
  public static final List<RegistryDelegate<WorldMapCoolonDestinationEntry>> LEGACY_COOLON_DESTINATIONS = registerEntries(COOLON_DESTINATIONS, "wmap_coolon_destination_", WmapStatics.coolonWarpDest_800ef228.length, LodWorldMapTravelData::coolonDestinationEntry);
  public static final List<RegistryDelegate<WorldMapTeleportLinkEntry>> LEGACY_TELEPORT_LINKS = registerEntries(TELEPORT_LINKS, "wmap_teleport_link_", WmapStatics.teleportationEndpointIndices_800ef698.length, LodWorldMapTravelData::teleportLinkEntry);
  public static final List<RegistryDelegate<WorldMapEncounterPoolEntry>> LEGACY_ENCOUNTER_POOLS = registerEntries(ENCOUNTER_POOLS, "wmap_encounter_pool_", WmapStatics.encounterIds_800ef364.length, LodWorldMapTravelData::encounterPoolEntry);

  private LodWorldMapTravelData() {
  }

  private static <T extends RegistryEntry, E extends RegistryEvent.Register<T>> List<RegistryDelegate<T>> registerEntries(final Registrar<T, E> registrar, final String prefix, final int count, final IntFunction<T> factory) {
    final List<RegistryDelegate<T>> delegates = new ArrayList<>(count);
    for(int i = 0; i < count; i++) {
      final int index = i;
      delegates.add(registrar.register(prefix + index, () -> factory.apply(index)));
    }
    return List.copyOf(delegates);
  }

  private static WorldMapStoryPresetEntry storyPresetEntry(final int index) {
    final WMapDestinationMarker2c preset = WmapStatics.wmapDestinationMarkers_800f5a6c[index];
    final List<RegistryId> enabled = new ArrayList<>();
    for(int portalIndex = 0; portalIndex < eventDefinition.portals().size(); portalIndex++) {
      if((preset.flags_04[portalIndex >>> 5] & 1 << (portalIndex & 31)) != 0) enabled.add(eventDefinition.portal(portalIndex).id());
    }
    final RegistryId place = index == 0 ? null : eventDefinition.place(preset.placeIndex_28).id();
    return new WorldMapStoryPresetEntry(id -> new WorldMapStoryPreset(index, preset.packedFlag_00, enabled, preset.x_24, preset.y_26, place));
  }

  private static WorldMapCoolonDestinationEntry coolonDestinationEntry(final int index) {
    final CoolonWarpDestination20 destination = WmapStatics.coolonWarpDest_800ef228[index];
    final RegistryId portal = eventDefinition.portal(destination.locationIndex_10).id();
    final RegistryId defaultDestination = coolonDestinationId(destination.defaultDestLocationIndex_14);
    final WorldMapPoint position = point(destination.destPosition_00.x, destination.destPosition_00.y, destination.destPosition_00.z);
    return new WorldMapCoolonDestinationEntry(id -> new WorldMapCoolonDestination(index, portal, defaultDestination, position, destination.x_18, destination.y_1a, destination.placeName_1c, index != 8));
  }

  private static WorldMapTeleportLinkEntry teleportLinkEntry(final int index) {
    final int[] endpoints = WmapStatics.teleportationEndpointIndices_800ef698[index];
    final TeleportationLocation0c source = teleportationLocation(endpoints[0]);
    final RegistryId sourcePortal = eventDefinition.portal(endpoints[0]).id();
    final RegistryId destinationPortal = eventDefinition.portal(endpoints[1]).id();
    final WorldMapPoint translation = point(source.translation_04);
    return new WorldMapTeleportLinkEntry(id -> new WorldMapTeleportLink(index, sourcePortal, destinationPortal, translation));
  }

  private static WorldMapEncounterPoolEntry encounterPoolEntry(final int index) {
    final int[] legacy = WmapStatics.encounterIds_800ef364[index];
    final List<RegistryId> encounters = new ArrayList<>(legacy.length);
    for(final int encounter : legacy) encounters.add(legacyEncounterId(encounter));
    return new WorldMapEncounterPoolEntry(id -> new WorldMapEncounterPool(index, encounters));
  }

  private static TeleportationLocation0c teleportationLocation(final int portalIndex) {
    for(final TeleportationLocation0c location : WmapStatics.teleportationLocations_800ef6c8) {
      if(location.locationIndex_00 == portalIndex) return location;
    }
    throw new IllegalStateException("Missing legacy teleportation position for portal " + portalIndex);
  }

  private static RegistryId legacyEncounterId(final int index) {
    return new RegistryId(LodMod.MOD_ID, LodEncounters.LEGACY[index]);
  }

  private static RegistryId coolonDestinationId(final int index) {
    if(index < 0 || index >= LEGACY_COOLON_DESTINATIONS.size()) throw new IllegalStateException("Invalid legacy Coolon destination " + index);
    return LEGACY_COOLON_DESTINATIONS.get(index).getId();
  }

  private static WorldMapPoint point(final Vector3i value) {
    return new WorldMapPoint(value.x, value.y, value.z);
  }

  private static WorldMapPoint point(final float x, final float y, final float z) {
    return new WorldMapPoint(x, y, z);
  }

  static void register(final RegisterWorldMapStoryPresetsEvent event) {
    withDefinition(() -> STORY_PRESETS.registryEvent(event));
  }

  static void register(final RegisterWorldMapCoolonDestinationsEvent event) {
    withDefinition(() -> COOLON_DESTINATIONS.registryEvent(event));
  }

  static void register(final RegisterWorldMapTeleportLinksEvent event) {
    withDefinition(() -> TELEPORT_LINKS.registryEvent(event));
  }

  static void register(final RegisterWorldMapEncounterPoolsEvent event) {
    withDefinition(() -> ENCOUNTER_POOLS.registryEvent(event));
  }

  private static void withDefinition(final Runnable registration) {
    eventDefinition = LegacyWorldMap.importDefinition();
    try {
      registration.run();
    } finally {
      eventDefinition = null;
    }
  }
}
