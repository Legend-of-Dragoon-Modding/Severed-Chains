package legend.game.wmap.world;

import legend.core.Registries;
import legend.game.types.Flags;
import legend.game.wmap.CoolonWarpDestination20;
import legend.game.wmap.TeleportationLocation0c;
import legend.game.wmap.registries.WorldMapDataEntry;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.legendofdragoon.modloader.registries.Registry;
import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/** Compiles registered data once per map initialization; never relies on registry iteration order. */
public final class WorldMapRegistrySnapshot {
  public record Value<T>(RegistryId id, T data) { }
  private final Map<RegistryId, WorldMapAvatar> avatars;
  private final Map<RegistryId, WorldMapRegion> regions;
  private final List<WorldMapTraversalProfile> traversalProfiles;

  private final WorldMapDefinition definition;
  private final List<WorldMapStoryPreset> story;
  private final List<Value<WorldMapCoolonDestination>> coolon;
  private final List<WorldMapTeleportLink> teleports;
  private final List<WorldMapEncounterPool> encounters;
  private final WorldMapPresentationProfile presentation;

  private WorldMapRegistrySnapshot(final Registries registries) {
    final Map<RegistryId, WorldMapAvatar> avatars = new HashMap<>();
    for(final Value<WorldMapAvatar> value : resolve(registries.worldMapAvatars)) avatars.put(value.id(), value.data());
    this.avatars = Map.copyOf(avatars);
    final Map<RegistryId, WorldMapRegion> regions = new HashMap<>();
    for(final Value<WorldMapRegion> value : resolve(registries.worldMapRegions)) {
      regions.put(value.id(), value.data());
    }
    this.regions = Map.copyOf(regions);
    this.traversalProfiles = resolve(registries.worldMapTraversalProfiles).stream().sorted(Comparator.<Value<WorldMapTraversalProfile>>comparingInt(value -> value.data().priority()).thenComparing(value -> value.id().toString())).map(Value::data).toList();
    final List<WorldMapPortal> portals = data(WorldMapSlots.allocate(resolve(registries.worldMapPortals)));
    final List<Value<WorldMapGeometry>> registeredGeometry = WorldMapSlots.allocate(resolve(registries.worldMapGeometry));
    final List<Value<WorldMapEncounterPool>> registeredPools = WorldMapSlots.allocate(resolve(registries.worldMapEncounterPools));
    final Map<RegistryId, Integer> geometryIndices = new HashMap<>();
    for(final Value<WorldMapGeometry> value : registeredGeometry) geometryIndices.put(value.id(), value.data().legacyIndex());
    final Map<RegistryId, Integer> poolIndices = new HashMap<>();
    for(final Value<WorldMapEncounterPool> value : registeredPools) poolIndices.put(value.id(), value.data().legacyIndex());
    final List<WorldMapRoute> routes = WorldMapSlots.allocate(resolve(registries.worldMapRoutes)).stream().map(value -> {
      final WorldMapRouteData route = value.data();
      if(route.encounterPool() == null && route.encounterRate() != 0 && route.legacyEncounterPlaceholder() != -1) {
        throw new IllegalArgumentException("Active WMAP route requires an encounter pool ID: " + value.id());
      }
      final Integer geometryIndex = geometryIndices.get(route.geometry());
      final Integer poolIndex = route.encounterPool() == null ? Integer.valueOf(route.legacyEncounterPlaceholder()) : poolIndices.get(route.encounterPool());
      if(geometryIndex == null || poolIndex == null) throw new IllegalArgumentException("Unresolved geometry " + route.geometry() + " or encounter pool " + route.encounterPool() + " on WMAP route " + value.id());
      return new WorldMapRoute(value.id(), route.legacyIndex(), route.start(), route.end(), geometryIndex, route.direction(), route.encounterRate(), route.battleStage(), poolIndex, route.modelIndex(), route.avatar());
    }).sorted(Comparator.comparingInt(WorldMapRoute::legacyIndex)).toList();
    final List<WorldMapPlace> places = data(WorldMapSlots.allocate(resolve(registries.worldMapPlaces)));
    final List<WorldMapGeometry> geometry = data(registeredGeometry).stream().sorted(Comparator.comparingInt(WorldMapGeometry::legacyIndex)).toList();
    if(portals.size() < 256) {
      throw new IllegalArgumentException("WMAP requires at least the 256 legacy portal slots");
    }
    for(int i = 0; i < geometry.size(); i++) {
      if(geometry.get(i).legacyIndex() != i) {
        throw new IllegalArgumentException("WMAP geometry indices must be unique and dense at " + i);
      }
    }
    this.definition = new WorldMapDefinition(portals, routes, places, data(resolve(registries.worldMapNodes)), geometry.stream().map(WorldMapGeometry::points).toList(), geometryIndices, poolIndices);
    this.story = data(resolve(registries.worldMapStoryPresets)).stream().sorted(Comparator.comparingInt(WorldMapStoryPreset::order)).toList();
    this.coolon = resolve(registries.worldMapCoolonDestinations).stream().sorted(Comparator.comparingInt(value -> value.data().order())).toList();
    this.teleports = data(resolve(registries.worldMapTeleportLinks)).stream().sorted(Comparator.comparingInt(WorldMapTeleportLink::order)).toList();
    this.encounters = data(registeredPools).stream().sorted(Comparator.comparingInt(WorldMapEncounterPool::legacyIndex)).toList();
    final List<WorldMapPresentationProfile> profiles = data(resolve(registries.worldMapPresentationProfiles));
    if(profiles.size() != 1) {
      throw new IllegalArgumentException("WMAP requires one presentation profile; use replaces to override lod:wmap_presentation");
    }
    this.presentation = profiles.getFirst();
    this.validate(registries);
  }

  public static WorldMapRegistrySnapshot read(final Registries registries) {
    return new WorldMapRegistrySnapshot(registries);
  }

  /** Explicit overlays preserve the target ID; equal-priority replacements fail instead of depending on load order. */
  public static <T> List<Value<T>> resolve(final Registry<? extends WorldMapDataEntry<T>> registry) {
    final Map<RegistryId, WorldMapDataEntry<T>> base = new LinkedHashMap<>();
    final Map<RegistryId, List<WorldMapDataEntry<T>>> replacements = new HashMap<>();
    final List<RegistryId> ids = new ArrayList<>();
    for(final RegistryId id : registry) {
      ids.add(id);
    }
    ids.sort(Comparator.comparing(RegistryId::toString));
    for(final RegistryId id : ids) {
      final WorldMapDataEntry<T> entry = registry.getEntry(id).get();
      if(entry.replaces == null) {
        base.put(id, entry);
      } else {
        replacements.computeIfAbsent(entry.replaces, key -> new ArrayList<>()).add(entry);
      }
    }
    final Map<RegistryId, WorldMapDataEntry<T>> originals = Map.copyOf(base);
    for(final var replacement : replacements.entrySet()) {
      if(!base.containsKey(replacement.getKey())) {
        throw new IllegalArgumentException("WMAP replacement target is not a base entry: " + replacement.getKey());
      }
      final List<WorldMapDataEntry<T>> candidates = replacement.getValue();
      candidates.sort(Comparator.comparingInt(entry -> entry.priority));
      for(int i = 1; i < candidates.size(); i++) {
        if(candidates.get(i - 1).priority == candidates.get(i).priority) {
          throw new IllegalArgumentException("Conflicting WMAP replacements for " + replacement.getKey() + " at priority " + candidates.get(i).priority);
        }
      }
      base.put(replacement.getKey(), candidates.getLast());
    }
    return base.entrySet().stream().map(entry -> {
      T value = entry.getValue().create(entry.getKey());
      if(entry.getValue() != originals.get(entry.getKey())) {
        final int originalIndex = WorldMapSlots.index(originals.get(entry.getKey()).create(entry.getKey()));
        if(originalIndex >= 0) {
          if(WorldMapSlots.index(value) >= 0 && WorldMapSlots.index(value) != originalIndex) {
            throw new IllegalArgumentException("WMAP replacement changes reserved slot for " + entry.getKey());
          }
          value = WorldMapSlots.assign(value, originalIndex);
        }
      }
      final RegistryId identity = switch(value) {
        case WorldMapPortal portal -> portal.id();
        case WorldMapPlace place -> place.id();
        case WorldMapNode node -> node.id();
        default -> entry.getKey();
      };
      if(!entry.getKey().equals(identity)) throw new IllegalArgumentException("WMAP factory changed effective registry identity " + entry.getKey() + " to " + identity);
      return new Value<>(entry.getKey(), value);
    }).toList();
  }

  private static <T> List<T> data(final List<Value<T>> values) {
    return values.stream().map(Value::data).toList();
  }

  public WorldMapDefinition definition() { return this.definition; }

  public Map<RegistryId, WorldMapRegion> regions() { return this.regions; }

  public WorldMapRegion region(final RegistryId id) {
    return Objects.requireNonNull(this.regions.get(id), "Unknown WMAP region " + id);
  }

  public RegistryId regionId(final WorldMapPortal portal) {
    return portal.region() == null ? WorldMapRegion.legacyId(portal.continent()) : portal.region();
  }

  public WorldMapRegion regionForPortal(final WorldMapPortal portal) {
    return this.region(this.regionId(portal));
  }

  public List<WorldMapTraversalProfile> traversalProfiles() { return this.traversalProfiles; }
  public WorldMapAvatar avatar(final RegistryId id) {
    final WorldMapAvatar avatar = this.avatars.get(id);
    if(avatar == null) throw new IllegalArgumentException("Unknown WMAP avatar " + id);
    return avatar;
  }
  public WorldMapPresentationProfile presentation() { return this.presentation; }
  public List<WorldMapStoryPreset> story() { return this.story; }
  public List<Value<WorldMapCoolonDestination>> coolon() { return this.coolon; }

  /** Menu-only origins are not projected onto the current continent's route geometry. */
  public int coolonOriginFallback(final SubmapEndpoint origin, final WorldMapDefinition definition) {
    int fallback = 0;
    boolean found = false;
    for(int i = 0; i < this.coolon.size(); i++) {
      final WorldMapCoolonDestination destination = this.coolon.get(i).data();
      if(destination.opensMenuOnArrival()) {
        if(definition.portal(destination.portal()).from().equals(origin)) {
          return i;
        }
        if(!found) {
          fallback = i;
          found = true;
        }
      }
    }
    return fallback;
  }

  public void configureBehaviours(final Registries registries, final WorldMapDefinition.Builder definition, final WorldMapRules.Builder rules) {
    rules.arrival((origin, progression, world) -> this.arrival(origin, world));
    final List<WorldMapBehaviour> behaviours = new ArrayList<>();
    for(final RegistryId id : registries.worldMapBehaviours) {
      behaviours.add(registries.worldMapBehaviours.getEntry(id).get());
    }
    behaviours.sort(Comparator.comparingInt(WorldMapBehaviour::priority).thenComparing(behaviour -> behaviour.getRegistryId().toString()));
    for(final WorldMapBehaviour behaviour : behaviours) {
      behaviour.configure(definition, rules);
    }
  }

  public WorldMapTravel.Arrival arrival(final SubmapEndpoint origin) {
    return this.arrival(origin, this.definition);
  }

  private WorldMapTravel.Arrival arrival(final SubmapEndpoint origin, final WorldMapDefinition definition) {
    for(final WorldMapTeleportLink link : this.teleports) {
      if(link.autoStartOnArrival() && definition.portal(link.source()).from().equals(origin)) return WorldMapTravel.Arrival.TELEPORT;
    }
    for(final Value<WorldMapCoolonDestination> value : this.coolon) {
      if(value.data().opensMenuOnArrival() && definition.portal(value.data().portal()).from().equals(origin)) return WorldMapTravel.Arrival.COOLON;
    }
    return WorldMapTravel.Arrival.NORMAL;
  }

  @Nullable
  private WorldMapStoryPreset selected(final Flags flags) {
    WorldMapStoryPreset selected = null;
    for(final WorldMapStoryPreset preset : this.story) {
      if(preset.storyFlag() < 0 || preset.storyFlag() >= 1024) throw new IllegalArgumentException("WMAP story flag out of range: " + preset.storyFlag());
      if(flags.get(preset.storyFlag())) {
        selected = preset;
      }
    }
    return selected;
  }

  public void applyStory(final Flags story, final Flags locations, final WorldMapDefinition definition) {
    locations.ensureCapacity((definition.portals().size() + 31) / 32);
    final WorldMapStoryPreset selected = this.selected(story);
    if(selected != null) {
      for(int i = 0; i < locations.count(); i++) {
        locations.setRaw(i, 0);
      }
      for(final RegistryId id : selected.enabledPortals()) {
        locations.set(definition.portal(id).legacyIndex(), true);
      }
    }
  }

  @Nullable
  public WorldMapObjective objective(final Flags story, final WorldMapDefinition definition) {
    final WorldMapStoryPreset selected = this.selected(story);
    if(selected == null || selected.place() == null) {
      return null;
    }
    final WorldMapPlace place = definition.places().stream().filter(value -> value.id().equals(selected.place())).findFirst().orElseThrow(() -> new IllegalArgumentException("Unknown WMAP objective place " + selected.place()));
    return new WorldMapObjective(selected.x(), selected.y(), place.name());
  }

  public CoolonWarpDestination20[] coolonData(final WorldMapDefinition definition) {
    final CoolonWarpDestination20[] destinations = new CoolonWarpDestination20[this.coolon.size()];
    for(int i = 0; i < destinations.length; i++) {
      final WorldMapCoolonDestination value = this.coolon.get(i).data();
      final WorldMapPoint point = value.position();
      int defaultIndex = -1;
      for(int j = 0; j < destinations.length; j++) {
        if(this.coolon.get(j).id().equals(value.defaultDestination())) {
          defaultIndex = j;
          break;
        }
      }
      if(defaultIndex == -1) throw new IllegalArgumentException("Unknown Coolon default destination " + value.defaultDestination());
      destinations[i] = new CoolonWarpDestination20(new Vector3f(point.x(), point.y(), point.z()), definition.portal(value.portal()).legacyIndex(), defaultIndex, value.x(), value.y(), value.label());
    }
    return destinations;
  }

  public int[][] teleportEndpoints(final WorldMapDefinition definition) {
    return this.teleports.stream().map(link -> new int[]{definition.portal(link.source()).legacyIndex(), definition.portal(link.destination()).legacyIndex()}).toArray(int[][]::new);
  }

  public TeleportationLocation0c[] teleportLocations(final WorldMapDefinition definition) {
    return this.teleports.stream().map(link -> new TeleportationLocation0c(definition.portal(link.source()).legacyIndex(), new Vector3i((int)link.translation().x(), (int)link.translation().y(), (int)link.translation().z()))).toArray(TeleportationLocation0c[]::new);
  }

  public RegistryId encounter(final int pool, final int roll) {
    final List<RegistryId> choices = this.encounters.get(Math.max(0, pool)).encounters();
    return choices.get(pool == -1 || roll < 35 ? 0 : roll < 70 ? 1 : roll < 90 ? 2 : 3);
  }

  private void validate(final Registries registries) {
    final HashSet<Integer> storyOrders = new HashSet<>();
    for(final WorldMapStoryPreset preset : this.story) {
      if(!storyOrders.add(preset.order())) {
        throw new IllegalArgumentException("Duplicate WMAP story order " + preset.order());
      }
      if(preset.storyFlag() < 0 || preset.storyFlag() >= 1024) {
        throw new IllegalArgumentException("WMAP story flag outside save range: " + preset.storyFlag());
      }
      for(final RegistryId id : preset.enabledPortals()) this.definition.portal(id);
      if(preset.place() != null && this.definition.places().stream().noneMatch(place -> place.id().equals(preset.place()))) throw new IllegalArgumentException("Unknown WMAP objective place " + preset.place());
    }
    if(this.coolon.isEmpty() || this.encounters.isEmpty()) throw new IllegalArgumentException("WMAP requires Coolon destinations and encounter pools");
    final HashSet<Integer> coolonOrders = new HashSet<>();
    for(final Value<WorldMapCoolonDestination> value : this.coolon) {
      if(!coolonOrders.add(value.data().order())) throw new IllegalArgumentException("Duplicate Coolon destination order " + value.data().order());
      finite(value.data().position(), value.id().toString());
    }
    final HashSet<RegistryId> teleportSources = new HashSet<>();
    final HashSet<Integer> teleportOrders = new HashSet<>();
    for(final WorldMapTeleportLink link : this.teleports) {
      if(!teleportOrders.add(link.order())) {
        throw new IllegalArgumentException("Duplicate WMAP teleport order " + link.order());
      }
      if(!teleportSources.add(link.source())) throw new IllegalArgumentException("Duplicate WMAP teleport source " + link.source());
      finite(link.translation(), link.source().toString());
      if(link.translation().x() != (int)link.translation().x() || link.translation().y() != (int)link.translation().y() || link.translation().z() != (int)link.translation().z()) throw new IllegalArgumentException("Teleport translations require integer coordinates: " + link.source());
    }
    for(final WorldMapTeleportLink link : this.teleports) {
      if(!teleportSources.contains(link.destination())) throw new IllegalArgumentException("Missing teleport placement for destination " + link.destination());
    }
    if(this.presentation.mapPositions().size() < 8 || this.presentation.regions().size() < 3 || this.presentation.services().size() < 5 || this.presentation.waterClutYs().size() < 14 || this.presentation.playerAvatarVramSlots().size() < 4 || this.presentation.textureAdjustments().size() < 22) {
      throw new IllegalArgumentException("WMAP presentation profile omits required renderer slots");
    }
    for(final WorldMapPoint point : this.presentation.mapPositions()) {
      finite(point, "map camera");
    }
    for(final int slot : this.presentation.playerAvatarVramSlots()) {
      if(slot < 0 || slot >= this.presentation.textureAdjustments().size()) {
        throw new IllegalArgumentException("Unknown WMAP avatar texture adjustment slot " + slot);
      }
    }
    for(int i = 0; i < this.encounters.size(); i++) {
      final WorldMapEncounterPool pool = this.encounters.get(i);
      if(pool.legacyIndex() != i || pool.encounters().size() != 4) throw new IllegalArgumentException("WMAP encounter pools require dense indices and four weighted slots at " + i);
      for(final RegistryId id : pool.encounters()) Objects.requireNonNull(registries.encounters.getEntry(id).get(), "Unknown encounter " + id);
    }
    this.validateDefinition(this.definition, false);
  }

  /** Validate cross-registry references again after registered behaviours and configure listeners. */
  public void validateDefinition(final WorldMapDefinition definition) {
    this.validateDefinition(definition, true);
  }

  private void validateDefinition(final WorldMapDefinition definition, final boolean configured) {
    final Map<RegistryId, RegistryId> routeRegions = new HashMap<>();
    for(final WorldMapPortal portal : definition.portals()) {
      if(portal.region() != null || portal.route() != null) {
        this.regionForPortal(portal);
      }
      if(portal.route() != null) {
        final RegistryId region = this.regionId(portal);
        final RegistryId previous = routeRegions.putIfAbsent(portal.route(), region);
        if(previous != null && !previous.equals(region)) {
          throw new IllegalArgumentException("WMAP route belongs to multiple regions: " + portal.route());
        }
      }
    }
    if(configured) {
      for(final WorldMapTraversalProfile profile : this.traversalProfiles) {
        for(final RegistryId route : profile.routes()) {
          definition.route(route);
        }
      }
    }
    if(definition.portals().size() < 256) {
      throw new IllegalArgumentException("WMAP requires at least 256 portal slots for save compatibility");
    }
    for(final WorldMapStoryPreset preset : this.story) {
      for(final RegistryId id : preset.enabledPortals()) {
        definition.portal(id);
      }
      if(preset.place() != null && definition.places().stream().noneMatch(place -> place.id().equals(preset.place()))) {
        throw new IllegalArgumentException("Unknown WMAP objective place " + preset.place());
      }
    }
    for(final WorldMapRoute route : definition.routes()) {
      if(route.avatar() != null && !this.avatars.containsKey(route.avatar())) throw new IllegalArgumentException("Unknown WMAP avatar " + route.avatar() + " on route " + route.id());
      if(route.encounterRate() != 0 && (route.encounterIndex() < -1 || route.encounterIndex() >= this.encounters.size())) throw new IllegalArgumentException("Unknown encounter pool on WMAP route " + route.id());
    }
    this.coolonData(definition);
    this.teleportEndpoints(definition);
  }

  private static void finite(final WorldMapPoint point, final String source) {
    if(!Float.isFinite(point.x()) || !Float.isFinite(point.y()) || !Float.isFinite(point.z())) throw new IllegalArgumentException("Non-finite WMAP coordinates at " + source);
  }
}
