package legend.game.wmap.world;

import legend.core.Registries;
import legend.game.types.Flags;
import legend.game.wmap.CoolonWarpDestination20;
import legend.game.wmap.TeleportationLocation0c;
import legend.game.wmap.preset.WorldMapPreset;
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
  private final Map<RegistryId, WorldMapThumbnail> thumbnails;
  private final Map<RegistryId, WorldMapService> services;
  private final Map<RegistryId, WorldMapSound> sounds;
  private final Map<RegistryId, WorldMapBattleStage> battleStages;
  private final Map<RegistryId, WorldMapSubmapDestination> submapDestinations;
  private final List<WorldMapTraversalProfile> traversalProfiles;

  private final WorldMapDefinition definition;
  private final List<WorldMapStoryPreset> story;
  private final List<Value<WorldMapCoolonDestination>> coolon;
  private final List<WorldMapTeleportLink> teleports;
  private final List<WorldMapEncounterPool> encounters;
  private final WorldMapPresentationProfile presentation;
  @Nullable private final WorldMapPreset preset;

  private WorldMapRegistrySnapshot(final Registries registries, @Nullable final WorldMapPreset preset) {
    this.preset = preset;
    if(preset != null && preset.behaviours() != null) {
      final HashSet<RegistryId> available = new HashSet<>();
      for(final RegistryId id : registries.worldMapBehaviours) available.add(id);
      for(final RegistryId id : preset.behaviours()) {
        if(!available.contains(id)) throw new IllegalArgumentException("Unknown preset WMAP behaviour " + id);
      }
    }
    final Map<RegistryId, WorldMapAvatar> avatars = new HashMap<>();
    for(final Value<WorldMapAvatar> value : resolve(registries.worldMapAvatars, preset == null ? Map.of() : preset.resolveAvatars(registries))) avatars.put(value.id(), value.data());
    this.avatars = Map.copyOf(avatars);
    final Map<RegistryId, WorldMapRegion> regions = new HashMap<>();
    for(final Value<WorldMapRegion> value : resolve(registries.worldMapRegions, preset == null ? Map.of() : preset.resolveRegions(registries))) {
      regions.put(value.id(), value.data());
    }
    this.regions = Map.copyOf(regions);

    this.thumbnails = index(resolve(registries.worldMapThumbnails, preset == null ? Map.of() : preset.resolveThumbnails(registries)));
    this.services = index(resolve(registries.worldMapServices, preset == null ? Map.of() : preset.serviceDefinitions()));
    this.sounds = index(resolve(registries.worldMapSounds, preset == null ? Map.of() : preset.soundDefinitions()));
    this.battleStages = index(resolve(registries.worldMapBattleStages, preset == null ? Map.of() : preset.battleStageDefinitions()));
    this.submapDestinations = index(resolve(registries.worldMapSubmapDestinations, preset == null ? Map.of() : preset.submapDestinations()));
    this.traversalProfiles = resolve(registries.worldMapTraversalProfiles, preset == null ? Map.of() : preset.resolveTraversalProfiles(registries)).stream().sorted(Comparator.<Value<WorldMapTraversalProfile>>comparingInt(value -> value.data().priority()).thenComparing(value -> value.id().toString())).map(WorldMapRegistrySnapshot::attributedProfile).toList();
    final List<WorldMapPortal> portals = data(WorldMapSlots.allocate(resolve(registries.worldMapPortals, preset == null ? Map.of() : preset.portals()))).stream()
      .map(portal -> new WorldMapPortal(portal.id(), portal.legacyIndex(), portal.route(), portal.place(),
        this.destination(portal.fromId(), portal.from(), "source", portal.id()), this.destination(portal.toId(), portal.to(), "destination", portal.id()),
        portal.junctionIndex(), portal.continent(), portal.fullBrightness(), portal.effectFlags(), portal.region(), portal.fromId(), portal.toId(),
        portal.atmosphere(), portal.smoke()))
      .toList();
    final List<Value<WorldMapGeometry>> registeredGeometry = WorldMapSlots.allocate(resolve(registries.worldMapGeometry, preset == null ? Map.of() : preset.geometry()));
    final List<Value<WorldMapEncounterPool>> registeredPools = WorldMapSlots.allocate(resolve(registries.worldMapEncounterPools, preset == null ? Map.of() : preset.encounterPools()));
    final Map<RegistryId, Integer> geometryIndices = new HashMap<>();
    for(final Value<WorldMapGeometry> value : registeredGeometry) geometryIndices.put(value.id(), value.data().legacyIndex());
    final Map<RegistryId, Integer> poolIndices = new HashMap<>();
    for(final Value<WorldMapEncounterPool> value : registeredPools) poolIndices.put(value.id(), value.data().legacyIndex());
    final List<WorldMapRoute> routes = WorldMapSlots.allocate(resolve(registries.worldMapRoutes, preset == null ? Map.of() : preset.routes())).stream().map(value -> {
      final WorldMapRouteData route = value.data();
      if(route.encounterPool() == null && route.encounterRate() != 0 && route.legacyEncounterPlaceholder() != -1) {
        throw new IllegalArgumentException("Active WMAP route requires an encounter pool ID: " + value.id());
      }
      final Integer geometryIndex = geometryIndices.get(route.geometry());
      final Integer poolIndex = route.encounterPool() == null ? Integer.valueOf(route.legacyEncounterPlaceholder()) : poolIndices.get(route.encounterPool());
      if(geometryIndex == null || poolIndex == null) throw new IllegalArgumentException("Unresolved geometry " + route.geometry() + " or encounter pool " + route.encounterPool() + " on WMAP route " + value.id());
      final int battleStage = route.battleStageId() == null ? route.battleStage() : this.battleStage(route.battleStageId(), value.id());
      return new WorldMapRoute(value.id(), route.legacyIndex(), route.start(), route.end(), geometryIndex, route.direction(), route.encounterRate(),
        battleStage, poolIndex, route.modelIndex(), route.avatar(), route.battleStageId());
    }).sorted(Comparator.comparingInt(WorldMapRoute::legacyIndex)).toList();
    final List<WorldMapPlace> places = data(WorldMapSlots.allocate(resolve(registries.worldMapPlaces, preset == null ? Map.of() : preset.places())));
    for(final WorldMapPlace place : places) this.validatePlaceReferences(place);
    final List<WorldMapGeometry> geometry = data(registeredGeometry).stream().sorted(Comparator.comparingInt(WorldMapGeometry::legacyIndex)).toList();
    if(portals.size() < 256) {
      throw new IllegalArgumentException("WMAP requires at least the 256 legacy portal slots");
    }
    for(int i = 0; i < geometry.size(); i++) {
      if(geometry.get(i).legacyIndex() != i) {
        throw new IllegalArgumentException("WMAP geometry indices must be unique and dense at " + i);
      }
    }
    final WorldMapDefinition resolvedDefinition = new WorldMapDefinition(portals, routes, places, data(resolve(registries.worldMapNodes, preset == null ? Map.of() : preset.nodes())), geometry.stream().map(WorldMapGeometry::points).toList(), geometryIndices, poolIndices);
    this.definition = preset == null ? resolvedDefinition : preset.removeFrom(resolvedDefinition);
    this.story = data(resolve(registries.worldMapStoryPresets, preset == null ? Map.of() : preset.storyPresets())).stream().sorted(Comparator.comparingInt(WorldMapStoryPreset::order)).toList();
    this.coolon = resolve(registries.worldMapCoolonDestinations, preset == null ? Map.of() : preset.coolonDestinations()).stream().sorted(Comparator.comparingInt(value -> value.data().order())).toList();
    this.teleports = data(resolve(registries.worldMapTeleportLinks, preset == null ? Map.of() : preset.teleportLinks())).stream().sorted(Comparator.comparingInt(WorldMapTeleportLink::order)).toList();
    this.encounters = data(registeredPools).stream().sorted(Comparator.comparingInt(WorldMapEncounterPool::legacyIndex)).toList();
    final List<WorldMapPresentationProfile> profiles = data(resolve(registries.worldMapPresentationProfiles, preset == null ? Map.of() : preset.resolvePresentationProfiles()));
    if(profiles.size() != 1) {
      throw new IllegalArgumentException("WMAP requires one presentation profile; use replaces to override lod:wmap_presentation");
    }
    this.presentation = profiles.getFirst();
    this.validate(registries);
  }

  public static WorldMapRegistrySnapshot read(final Registries registries) {
    return new WorldMapRegistrySnapshot(registries, null);
  }

  public static WorldMapRegistrySnapshot read(final Registries registries, @Nullable final WorldMapPreset preset) {
    return new WorldMapRegistrySnapshot(registries, preset);
  }

  /** Preset overlays preserve frozen registrations and every preassigned compatibility slot. */
  private static <T> List<Value<T>> resolve(final Registry<? extends WorldMapDataEntry<T>> registry, final Map<RegistryId, T> overlay) {
    final Map<RegistryId, T> result = new LinkedHashMap<>();
    for(final Value<T> value : resolve(registry)) result.put(value.id(), value.data());
    overlay.forEach((id, replacement) -> {
      final T original = result.get(id);
      final int slot = original == null ? -1 : WorldMapSlots.index(original);
      if(WorldMapSlots.index(replacement) < -1) throw new IllegalArgumentException("Invalid preset WMAP compatibility slot for " + id);
      if(slot >= 0 && WorldMapSlots.index(replacement) >= 0 && WorldMapSlots.index(replacement) != slot) {
        throw new IllegalArgumentException("Preset cannot change WMAP compatibility slot for " + id);
      }
      result.put(id, slot >= 0 ? WorldMapSlots.assign(replacement, slot) : replacement);
    });
    return result.entrySet().stream().sorted(Map.Entry.comparingByKey(Comparator.comparing(RegistryId::toString)))
      .map(entry -> new Value<>(entry.getKey(), entry.getValue())).toList();
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

  private static <T> Map<RegistryId, T> index(final List<Value<T>> values) {
    final Map<RegistryId, T> result = new LinkedHashMap<>();
    values.forEach(value -> result.put(value.id(), value.data()));
    return Map.copyOf(result);
  }

  private SubmapEndpoint destination(@Nullable final RegistryId id, final SubmapEndpoint fallback, final String kind, final RegistryId portal) {
    if(id == null) return fallback;
    final WorldMapSubmapDestination destination = this.submapDestinations.get(id);
    if(destination == null) throw new IllegalArgumentException("Unknown WMAP " + kind + " destination " + id + " on portal " + portal);
    return destination.endpoint();
  }

  private int battleStage(final RegistryId id, final RegistryId route) {
    final WorldMapBattleStage stage = this.battleStages.get(id);
    if(stage == null) throw new IllegalArgumentException("Unknown WMAP battle stage " + id + " on route " + route);
    return stage.nativeIndex();
  }

  private void validatePlaceReferences(final WorldMapPlace place) {
    if(place.thumbnailId() != null && !this.thumbnails.containsKey(place.thumbnailId())) {
      throw new IllegalArgumentException("Unknown WMAP thumbnail " + place.thumbnailId() + " on place " + place.id());
    }
    if(place.serviceIds() != null) {
      for(final RegistryId id : place.serviceIds()) {
        if(!this.services.containsKey(id)) throw new IllegalArgumentException("Unknown WMAP service " + id + " on place " + place.id());
      }
    }
    if(place.soundIds() != null) {
      for(final RegistryId id : place.soundIds()) {
        if(!this.sounds.containsKey(id)) throw new IllegalArgumentException("Unknown WMAP sound " + id + " on place " + place.id());
      }
    }
  }

  public WorldMapDefinition definition() { return this.definition; }

  /** Resolves registry-backed values added by behaviours or configure-event listeners after the initial snapshot. */
  public WorldMapDefinition resolveReferences(final WorldMapDefinition definition) {
    final WorldMapDefinition.Builder builder = definition.toBuilder();
    for(final WorldMapPlace place : definition.places()) this.validatePlaceReferences(place);
    for(final WorldMapPortal portal : definition.portals()) {
      if(portal.fromId() != null || portal.toId() != null) {
        builder.replacePortal(new WorldMapPortal(portal.id(), portal.legacyIndex(), portal.route(), portal.place(),
          this.destination(portal.fromId(), portal.from(), "source", portal.id()), this.destination(portal.toId(), portal.to(), "destination", portal.id()),
          portal.junctionIndex(), portal.continent(), portal.fullBrightness(), portal.effectFlags(), portal.region(), portal.fromId(), portal.toId(),
          portal.atmosphere(), portal.smoke()));
      }
    }
    for(final WorldMapRoute route : definition.routes()) {
      if(route.battleStageId() != null) {
        builder.replaceRoute(new WorldMapRoute(route.id(), route.legacyIndex(), route.start(), route.end(), route.segmentIndex(), route.direction(),
          route.encounterRate(), this.battleStage(route.battleStageId(), route.id()), route.encounterIndex(), route.modelIndex(), route.avatar(), route.battleStageId()));
      }
    }
    return builder.build();
  }

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

  private static WorldMapTraversalProfile attributedProfile(final Value<WorldMapTraversalProfile> value) {
    final WorldMapTraversalProfile profile = value.data();
    return new WorldMapTraversalProfile(profile.priority(), profile.routes(), profile.markers(), event -> {
      try {
        profile.handler().accept(event);
      } catch(final RuntimeException failure) {
        throw new IllegalStateException("World map traversal profile " + value.id() + " failed during " + event.phase + " on route " + event.route.id(), failure);
      }
    }, profile.includeReverseRoutes());
  }

  /** Materialize route selectors against the final configured graph, not the registry snapshot. */
  public List<WorldMapTraversalProfile> traversalProfiles(final WorldMapDefinition definition) {
    return this.traversalProfiles.stream().map(profile -> profile.resolve(definition)).toList();
  }
  public WorldMapAvatar avatar(final RegistryId id) {
    final WorldMapAvatar avatar = this.avatars.get(id);
    if(avatar == null) throw new IllegalArgumentException("Unknown WMAP avatar " + id);
    return avatar;
  }

  @Nullable
  public WorldMapThumbnail thumbnail(final WorldMapPlace place) {
    if(place.thumbnailId() == null) return null;
    final WorldMapThumbnail thumbnail = this.thumbnails.get(place.thumbnailId());
    if(thumbnail == null) throw new IllegalArgumentException("Unknown WMAP thumbnail " + place.thumbnailId() + " on place " + place.id());
    return thumbnail;
  }

  public List<String> services(final WorldMapPlace place) {
    if(place.serviceIds() == null) {
      final List<String> labels = this.presentation.services();
      final List<String> result = new ArrayList<>();
      for(int bit = 0; bit < Math.min(5, labels.size()); bit++) {
        if((place.services() & 1 << bit) != 0) result.add(labels.get(bit));
      }
      return List.copyOf(result);
    }
    return place.serviceIds().stream().map(id -> {
      final WorldMapService service = this.services.get(id);
      if(service == null) throw new IllegalArgumentException("Unknown WMAP service " + id + " on place " + place.id());
      if(service.legacyBit() != null && service.legacyBit() < this.presentation.services().size()) {
        final List<String> nativeLabels = WorldMapPresentationProfile.legacy().services();
        if(service.legacyBit() < nativeLabels.size() && service.label().equals(nativeLabels.get(service.legacyBit()))) {
          return this.presentation.services().get(service.legacyBit());
        }
      }
      return service.label();
    }).toList();
  }

  public List<Integer> sounds(final WorldMapPlace place) {
    if(place.soundIds() == null) return place.sounds().stream().filter(index -> index > 0).toList();
    return place.soundIds().stream().map(id -> {
      final WorldMapSound sound = this.sounds.get(id);
      if(sound == null) throw new IllegalArgumentException("Unknown WMAP sound " + id + " on place " + place.id());
      return sound.nativeIndex();
    }).toList();
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
      if(this.preset == null || this.preset.behaviours() == null || this.preset.behaviours().contains(id)) {
        behaviours.add(registries.worldMapBehaviours.getEntry(id).get());
      }
    }
    behaviours.sort(Comparator.comparingInt(WorldMapBehaviour::priority).thenComparing(behaviour -> behaviour.getRegistryId().toString()));
    for(final WorldMapBehaviour behaviour : behaviours) {
      behaviour.configure(definition, rules);
    }
    if(this.preset != null) this.preset.rules().configure(rules);
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
    if(this.preset != null) this.preset.validateReferences(definition, this.avatars.keySet());
    final Map<RegistryId, RegistryId> routeRegions = new HashMap<>();
    for(final WorldMapPortal portal : definition.portals()) {
      if(portal.region() != null || portal.route() != null) {
        if(this.regionForPortal(portal).legacyTemplate() != portal.continent()) {
          throw new IllegalArgumentException("WMAP portal " + portal.id() + " must use its region's legacy continent template");
        }
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
