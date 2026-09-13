package legend.game.wmap.preset;

import legend.core.Registries;
import legend.game.modding.events.worldmap.WorldMapTraversalEvent;
import legend.game.tmd.UvAdjustmentMetrics14;
import legend.game.wmap.Continent;
import legend.game.wmap.TeleportationLocation0c;
import legend.lodmod.LodEncounters;
import legend.lodmod.LodWorldMapData;
import legend.lodmod.LodWorldMapAuthoringData;
import legend.game.wmap.registries.WorldMapDataEntry;
import legend.game.wmap.world.LegacyWorldMap;
import legend.game.wmap.world.WorldMapAccess;
import legend.game.wmap.world.WorldMapAvatar;
import legend.game.wmap.world.WorldMapBattleStage;
import legend.game.wmap.world.WorldMapCameraSettings;
import legend.game.wmap.world.WorldMapCoolonDestination;
import legend.game.wmap.world.WorldMapDefinition;
import legend.game.wmap.world.WorldMapEncounterPool;
import legend.game.wmap.world.WorldMapGeometry;
import legend.game.wmap.world.WorldMapNode;
import legend.game.wmap.world.WorldMapPlace;
import legend.game.wmap.world.WorldMapPoint;
import legend.game.wmap.world.WorldMapPolicy;
import legend.game.wmap.world.WorldMapPortal;
import legend.game.wmap.world.WorldMapPresentationProfile;
import legend.game.wmap.world.WorldMapPresentation;
import legend.game.wmap.world.WorldMapRegion;
import legend.game.wmap.world.WorldMapRegistrySnapshot;
import legend.game.wmap.world.WorldMapRoute;
import legend.game.wmap.world.WorldMapRouteData;
import legend.game.wmap.world.WorldMapService;
import legend.game.wmap.world.WorldMapSound;
import legend.game.wmap.world.WorldMapSubmapDestination;
import legend.game.wmap.world.WorldMapThumbnail;
import legend.game.wmap.world.WorldMapRules;
import legend.game.wmap.world.WorldMapStoryPreset;
import legend.game.wmap.world.WorldMapTeleportLink;
import legend.game.wmap.world.WorldMapTravel;
import legend.game.wmap.world.WorldMapTravelTarget;
import legend.game.wmap.world.WorldMapTraversalProfile;
import org.legendofdragoon.modloader.registries.Registry;
import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/** Immutable, callback-free WMAP document. Overlays are local to one initialized map. */
public record WorldMapPreset(RegistryId id, String name, String description, Set<String> requiredMods,
                             Path packageRoot,
                             Map<RegistryId, ThumbnailDefinition> thumbnailDefinitions,
                             Map<RegistryId, WorldMapService> serviceDefinitions,
                             Map<RegistryId, WorldMapSound> soundDefinitions,
                             Map<RegistryId, WorldMapBattleStage> battleStageDefinitions,
                             Map<RegistryId, WorldMapSubmapDestination> submapDestinations,
                             Map<RegistryId, WorldMapNode> nodes,
                             Map<RegistryId, WorldMapGeometry> geometry,
                             Map<RegistryId, WorldMapPlace> places,
                             Map<RegistryId, WorldMapRouteData> routes,
                             Map<RegistryId, WorldMapPortal> portals,
                             Map<RegistryId, WorldMapEncounterPool> encounterPools,
                             Map<RegistryId, WorldMapStoryPreset> storyPresets,
                             Map<RegistryId, WorldMapCoolonDestination> coolonDestinations,
                             Map<RegistryId, WorldMapTeleportLink> teleportLinks,
                             Map<RegistryId, Region> regions,
                             Map<RegistryId, Avatar> avatars,
                             Map<RegistryId, TraversalProfile> traversalProfiles,
                             Map<RegistryId, PresentationProfile> presentationProfiles,
                             Rules rules, @Nullable Set<RegistryId> behaviours,
                             Set<Removal> removals, Map<RegistryId, String> thumbnails) {
  public WorldMapPreset(final RegistryId id, final String name, final String description, final Set<String> requiredMods,
                        final Path packageRoot, final Map<RegistryId, WorldMapNode> nodes,
                        final Map<RegistryId, WorldMapGeometry> geometry, final Map<RegistryId, WorldMapPlace> places,
                        final Map<RegistryId, WorldMapRouteData> routes, final Map<RegistryId, WorldMapPortal> portals,
                        final Map<RegistryId, WorldMapEncounterPool> encounterPools,
                        final Map<RegistryId, WorldMapStoryPreset> storyPresets,
                        final Map<RegistryId, WorldMapCoolonDestination> coolonDestinations,
                        final Map<RegistryId, WorldMapTeleportLink> teleportLinks, final Map<RegistryId, Region> regions,
                        final Map<RegistryId, Avatar> avatars, final Map<RegistryId, TraversalProfile> traversalProfiles,
                        final Map<RegistryId, PresentationProfile> presentationProfiles, final Rules rules,
                        @Nullable final Set<RegistryId> behaviours, final Set<Removal> removals,
                        final Map<RegistryId, String> thumbnails) {
    this(id, name, description, requiredMods, packageRoot, Map.of(), Map.of(), Map.of(), Map.of(), Map.of(), nodes, geometry,
      places, routes, portals, encounterPools, storyPresets, coolonDestinations, teleportLinks, regions, avatars,
      traversalProfiles, presentationProfiles, rules, behaviours, removals, thumbnails);
  }

  public WorldMapPreset {
    Objects.requireNonNull(id, "id");
    if(Objects.requireNonNull(name, "name").isBlank()) throw new IllegalArgumentException("Preset name is empty");
    Objects.requireNonNull(description, "description");
    requiredMods = Set.copyOf(requiredMods);
    packageRoot = Objects.requireNonNull(packageRoot, "packageRoot").toAbsolutePath().normalize();
    thumbnailDefinitions = Map.copyOf(thumbnailDefinitions);
    serviceDefinitions = Map.copyOf(serviceDefinitions);
    soundDefinitions = Map.copyOf(soundDefinitions);
    battleStageDefinitions = Map.copyOf(battleStageDefinitions);
    submapDestinations = Map.copyOf(submapDestinations);
    nodes = Map.copyOf(nodes);
    geometry = Map.copyOf(geometry);
    places = Map.copyOf(places);
    routes = Map.copyOf(routes);
    portals = Map.copyOf(portals);
    encounterPools = Map.copyOf(encounterPools);
    storyPresets = Map.copyOf(storyPresets);
    coolonDestinations = Map.copyOf(coolonDestinations);
    teleportLinks = Map.copyOf(teleportLinks);
    regions = Map.copyOf(regions);
    avatars = Map.copyOf(avatars);
    traversalProfiles = Map.copyOf(traversalProfiles);
    presentationProfiles = Map.copyOf(presentationProfiles);
    Objects.requireNonNull(rules, "rules");
    behaviours = behaviours == null ? null : Set.copyOf(behaviours);
    removals = Set.copyOf(removals);
    thumbnails = Map.copyOf(thumbnails);
    nodes.forEach((key, value) -> requireIdentity(key, value.id()));
    places.forEach((key, value) -> requireIdentity(key, value.id()));
    portals.forEach((key, value) -> requireIdentity(key, value.id()));
  }

  private static void requireIdentity(final RegistryId key, final RegistryId value) {
    if(!key.equals(value)) throw new IllegalArgumentException("Preset entry " + key + " changes identity to " + value);
  }

  public record Region(Continent legacyTemplate, RegistryId provider, RegistryId presentationProvider,
                       WorldMapCameraSettings camera, @Nullable RegionAssets assets) { }

  public record Removal(String kind, RegistryId id) {
    public Removal {
      if(!Set.of("portals", "routes", "places", "nodes", "geometry").contains(kind)) throw new IllegalArgumentException("Unsupported preset removal kind " + kind);
      Objects.requireNonNull(id, "id");
    }
  }

  public WorldMapDefinition removeFrom(final WorldMapDefinition definition) {
    if(this.removals.isEmpty()) return definition;
    final WorldMapDefinition.Builder builder = definition.toBuilder();
    for(final String kind : List.of("portals", "routes", "places", "nodes", "geometry")) {
      for(final Removal removal : this.removals.stream().filter(value -> value.kind.equals(kind)).sorted(java.util.Comparator.comparing(value -> value.id.toString())).toList()) {
        switch(kind) {
          case "portals" -> builder.removePortal(removal.id);
          case "routes" -> builder.removeRoute(removal.id);
          case "places" -> builder.removePlace(removal.id);
          case "nodes" -> builder.removeNode(removal.id);
          case "geometry" -> builder.removeGeometry(removal.id);
          default -> throw new IllegalArgumentException("Unsupported removal kind " + kind);
        }
      }
    }
    return builder.build();
  }

  public record RegionAssets(String model, boolean retailAnimations, List<String> textures) {
    public RegionAssets { textures = List.copyOf(textures); }
  }

  /** Serializable thumbnail definition. A provider preserves a programmatic registry dependency. */
  public record ThumbnailDefinition(int nativeIndex, @Nullable String asset, @Nullable String label, @Nullable RegistryId provider) {
    public ThumbnailDefinition {
      final int sources = (nativeIndex >= 0 ? 1 : 0) + (asset != null ? 1 : 0) + (provider != null ? 1 : 0);
      if(sources != 1) throw new IllegalArgumentException("WMAP thumbnail definition requires exactly one native index, asset, or provider");
    }
  }

  public record Avatar(@Nullable RegistryId provider, @Nullable AvatarAssets assets) {
    public Avatar {
      if((provider == null) == (assets == null)) throw new IllegalArgumentException("Avatar requires exactly one provider or file asset source");
    }
  }

  public record AvatarAssets(String model, @Nullable String texture, WorldMapPoint scale, float shadowScale,
                             int idleAnimation, int walkAnimation, int runAnimation, int textureSlot,
                             List<String> animations) {
    public AvatarAssets { animations = List.copyOf(animations); }
  }

  public record Warp(WorldMapTraversalEvent.Phase phase, @Nullable RegistryId marker, RegistryId target,
                     boolean respectAccess) { }

  public record TraversalProfile(int priority, @Nullable RegistryId provider, Set<RegistryId> routes,
                                 List<WorldMapTraversalProfile.Marker> markers, boolean includeReverseRoutes,
                                 float speedMultiplier, @Nullable RegistryId avatar,
                                 @Nullable WorldMapPoint visualOffset, List<Warp> warps) {
    public TraversalProfile {
      routes = Set.copyOf(routes);
      markers = List.copyOf(markers);
      warps = List.copyOf(warps);
      if(!Float.isFinite(speedMultiplier) || speedMultiplier < 0) throw new IllegalArgumentException("Traversal speed must be finite and nonnegative");
    }
  }

  public enum UvMode { NORMAL, NONE, PNG }

  public record TextureAdjustment(int index, int clutX, int clutY, int tpageX, int tpageY, UvMode mode) {
    public UvAdjustmentMetrics14 resolve() {
      return switch(this.mode) {
        case NORMAL -> new UvAdjustmentMetrics14(this.index, this.clutX, this.clutY, this.tpageX, this.tpageY);
        case NONE -> UvAdjustmentMetrics14.NONE;
        case PNG -> UvAdjustmentMetrics14.PNG;
      };
    }

    static TextureAdjustment from(final UvAdjustmentMetrics14 value) {
      if(value.getClass() != UvAdjustmentMetrics14.class && value != UvAdjustmentMetrics14.NONE && value != UvAdjustmentMetrics14.PNG) {
        throw new IllegalArgumentException("Cannot export custom UV callback; use a registered presentation provider");
      }
      return new TextureAdjustment(value.index, value.clutX, value.clutY, value.tpageX, value.tpageY,
        value == UvAdjustmentMetrics14.NONE ? UvMode.NONE : value == UvAdjustmentMetrics14.PNG ? UvMode.PNG : UvMode.NORMAL);
    }
  }

  public record PresentationProfile(List<WorldMapPoint> mapPositions, List<String> regions, List<String> services,
                                    List<Integer> waterClutYs, List<Integer> playerAvatarVramSlots,
                                    List<TextureAdjustment> textureAdjustments) {
    public PresentationProfile {
      mapPositions = List.copyOf(mapPositions);
      regions = List.copyOf(regions);
      services = List.copyOf(services);
      waterClutYs = List.copyOf(waterClutYs);
      playerAvatarVramSlots = List.copyOf(playerAvatarVramSlots);
      textureAdjustments = List.copyOf(textureAdjustments);
    }

    WorldMapPresentationProfile resolve() {
      return new WorldMapPresentationProfile(this.mapPositions, this.regions, this.services, this.waterClutYs,
        this.playerAvatarVramSlots, this.textureAdjustments.stream().map(TextureAdjustment::resolve).toList());
    }
  }

  public record Rules(@Nullable WorldMapPolicy policy, Map<WorldMapTravel.Capability, Boolean> capabilities,
                      Map<RegistryId, WorldMapAccess> portals) {
    public static final Rules EMPTY = new Rules(null, Map.of(), Map.of());
    public Rules {
      capabilities = Map.copyOf(capabilities);
      portals = Map.copyOf(portals);
    }

    public void configure(final WorldMapRules.Builder builder) {
      if(this.policy != null) builder.policy(this.policy);
      this.capabilities.forEach((id, allowed) -> builder.capability(id, progression -> allowed));
      this.portals.forEach((id, access) -> builder.portal(id, (portal, action, progression) -> access));
    }
  }

  /** Asset manifest used when installing a complete immutable campaign package. */
  public Set<String> assetPaths() {
    final Set<String> paths = new HashSet<>();
    paths.addAll(this.thumbnails.values());
    this.thumbnailDefinitions.values().forEach(value -> {
      if(value.asset != null) paths.add(value.asset);
    });
    this.regions.values().forEach(region -> {
      if(region.assets != null) {
        paths.add(region.assets.model);
        paths.addAll(region.assets.textures);
      }
    });
    this.avatars.values().forEach(avatar -> {
      if(avatar.assets != null) {
        paths.add(avatar.assets.model);
        if(avatar.assets.texture != null) paths.add(avatar.assets.texture);
        paths.addAll(avatar.assets.animations);
      }
    });
    paths.forEach(WorldMapPresetAssets::validateRelative);
    return Set.copyOf(paths);
  }

  public Map<RegistryId, WorldMapRegion> resolveRegions(final Registries registries) {
    final Map<RegistryId, WorldMapRegion> providers = values(registries.worldMapRegions);
    return transform(this.regions, (id, region) -> new WorldMapRegion(region.legacyTemplate,
      region.assets == null ? require(providers, region.provider, "region model provider", id).model() :
        gameState -> WorldMapPresetAssets.region(this.packageRoot, id, region.assets),
      region.camera, require(providers, region.presentationProvider, "region presentation provider", id).presentation()));
  }

  public Map<RegistryId, WorldMapAvatar> resolveAvatars(final Registries registries) {
    final Map<RegistryId, WorldMapAvatar> providers = values(registries.worldMapAvatars);
    return transform(this.avatars, (id, avatar) -> avatar.assets == null ? require(providers, avatar.provider, "avatar provider", id) :
      new WorldMapAvatar(gameState -> WorldMapPresetAssets.avatar(this.packageRoot, id, avatar.assets)));
  }

  public Map<RegistryId, WorldMapThumbnail> resolveThumbnails(final Registries registries) {
    final Map<RegistryId, WorldMapThumbnail> providers = values(registries.worldMapThumbnails);
    return transform(this.thumbnailDefinitions, (id, value) -> {
      if(value.provider != null) return require(providers, value.provider, "thumbnail provider", id);
      if(value.asset != null) return new WorldMapThumbnail(-1, value.asset, value.label, null);
      return new WorldMapThumbnail(value.nativeIndex, null, value.label, null);
    });
  }

  public Map<RegistryId, WorldMapTraversalProfile> resolveTraversalProfiles(final Registries registries) {
    final Map<RegistryId, WorldMapTraversalProfile> providers = values(registries.worldMapTraversalProfiles);
    return transform(this.traversalProfiles, (id, profile) -> {
      final WorldMapTraversalProfile provider = profile.provider == null ? null : require(providers, profile.provider, "traversal handler provider", id);
      return new WorldMapTraversalProfile(profile.priority, profile.routes, profile.markers, event -> {
        if(provider != null) provider.handler().accept(event);
        if(event.phase == WorldMapTraversalEvent.Phase.TICK) {
          event.speedMultiplier *= profile.speedMultiplier;
          if(profile.avatar != null) event.avatar = profile.avatar;
          if(profile.visualOffset != null) event.visualOffset = profile.visualOffset;
        }
        for(final Warp warp : profile.warps) {
          if(event.phase == warp.phase && (warp.marker == null || warp.marker.equals(event.marker))) {
            event.getEngineState().requestWorldMapTravel(new WorldMapTravelTarget.Portal(warp.target), warp.respectAccess);
            break;
          }
        }
      }, profile.includeReverseRoutes);
    });
  }

  public Map<RegistryId, WorldMapPresentationProfile> resolvePresentationProfiles() {
    return transform(this.presentationProfiles, (id, value) -> value.resolve());
  }

  public void validateReferences(final WorldMapDefinition definition, final Set<RegistryId> avatarIds) {
    this.thumbnails.keySet().forEach(id -> {
      if(definition.places().stream().noneMatch(place -> place.id().equals(id))) throw new IllegalArgumentException("Unknown preset thumbnail place " + id);
    });
    this.traversalProfiles.forEach((id, profile) -> {
      if(profile.avatar != null && !avatarIds.contains(profile.avatar)) throw new IllegalArgumentException("Unknown avatar " + profile.avatar + " in preset traversal " + id);
      for(final Warp warp : profile.warps) {
        definition.portal(warp.target);
        if(warp.marker != null && profile.markers.stream().noneMatch(marker -> marker.id().equals(warp.marker))) {
          throw new IllegalArgumentException("Unknown marker " + warp.marker + " in preset traversal " + id);
        }
      }
    });
    this.rules.portals.keySet().forEach(definition::portal);
  }

  private static <T> T require(final Map<RegistryId, T> providers, final RegistryId provider, final String kind, final RegistryId owner) {
    final T value = providers.get(provider);
    if(value == null) throw new IllegalArgumentException("Unknown " + kind + ' ' + provider + " for preset entry " + owner);
    return value;
  }

  private static <T, R> Map<RegistryId, R> transform(final Map<RegistryId, T> source, final java.util.function.BiFunction<RegistryId, T, R> mapper) {
    final Map<RegistryId, R> result = new LinkedHashMap<>();
    source.forEach((id, value) -> result.put(id, mapper.apply(id, value)));
    return Map.copyOf(result);
  }

  private static <T> Map<RegistryId, T> values(final Registry<? extends WorldMapDataEntry<T>> registry) {
    final Map<RegistryId, T> result = new LinkedHashMap<>();
    WorldMapRegistrySnapshot.resolve(registry).forEach(value -> result.put(value.id(), value.data()));
    return Map.copyOf(result);
  }

  /** Exports the supplied registries' resolved declarative data; callbacks remain registry references. */
  public static WorldMapPreset export(final Registries registries, final RegistryId id, final String name, final String description) {
    final Builder builder = new Builder(id, name).description(description);
    values(registries.worldMapThumbnails).forEach((key, value) -> builder.thumbnailDefinitions.put(key,
      new ThumbnailDefinition(-1, null, value.label(), key)));
    builder.serviceDefinitions.putAll(values(registries.worldMapServices));
    builder.soundDefinitions.putAll(values(registries.worldMapSounds));
    builder.battleStageDefinitions.putAll(values(registries.worldMapBattleStages));
    builder.submapDestinations.putAll(values(registries.worldMapSubmapDestinations));
    builder.nodes.putAll(values(registries.worldMapNodes));
    builder.geometry.putAll(values(registries.worldMapGeometry));
    builder.places.putAll(values(registries.worldMapPlaces));
    builder.routes.putAll(values(registries.worldMapRoutes));
    builder.portals.putAll(values(registries.worldMapPortals));
    builder.encounterPools.putAll(values(registries.worldMapEncounterPools));
    builder.storyPresets.putAll(values(registries.worldMapStoryPresets));
    builder.coolonDestinations.putAll(values(registries.worldMapCoolonDestinations));
    builder.teleportLinks.putAll(values(registries.worldMapTeleportLinks));
    values(registries.worldMapRegions).forEach((key, value) -> builder.regions.put(key, new Region(value.legacyTemplate(), key, key, value.camera(), null)));
    values(registries.worldMapAvatars).forEach((key, value) -> builder.avatars.put(key, new Avatar(key, null)));
    values(registries.worldMapTraversalProfiles).forEach((key, value) -> builder.traversalProfiles.put(key,
      new TraversalProfile(value.priority(), key, value.routes(), value.markers(), value.includeReverseRoutes(), 1.0f, null, null, List.of())));
    values(registries.worldMapPresentationProfiles).forEach((key, value) -> builder.presentationProfiles.put(key,
      new PresentationProfile(value.mapPositions(), value.regions(), value.services(), value.waterClutYs(), value.playerAvatarVramSlots(),
        value.textureAdjustments().stream().map(TextureAdjustment::from).toList())));
    builder.behaviours = new HashSet<>();
    for(final RegistryId behaviour : registries.worldMapBehaviours) builder.behaviours.add(behaviour);
    final WorldMapPreset preset = builder.build();
    final Set<String> mods = new HashSet<>();
    // Every exported registry owner is required; callback providers are attributed to these IDs.
    for(final Map<RegistryId, ?> section : List.of(preset.thumbnailDefinitions, preset.serviceDefinitions, preset.soundDefinitions,
      preset.battleStageDefinitions, preset.submapDestinations, preset.nodes, preset.geometry, preset.places, preset.routes, preset.portals,
      preset.encounterPools, preset.storyPresets, preset.coolonDestinations, preset.teleportLinks, preset.regions, preset.avatars,
      preset.traversalProfiles, preset.presentationProfiles)) {
      section.keySet().forEach(key -> mods.add(key.toString().split(":", 2)[0]));
    }
    builder.behaviours.forEach(key -> mods.add(key.toString().split(":", 2)[0]));
    builder.thumbnailDefinitions.values().forEach(value -> {
      if(value.provider != null) mods.add(value.provider.modId());
    });
    // Replacements keep the target's identity, so preserve contributing mod dependencies too.
    for(final Registry<?> registry : List.<Registry<?>>of(registries.worldMapThumbnails, registries.worldMapServices, registries.worldMapSounds,
      registries.worldMapBattleStages, registries.worldMapSubmapDestinations, registries.worldMapNodes, registries.worldMapGeometry,
      registries.worldMapPlaces, registries.worldMapRoutes, registries.worldMapPortals, registries.worldMapEncounterPools,
      registries.worldMapStoryPresets, registries.worldMapCoolonDestinations, registries.worldMapTeleportLinks,
      registries.worldMapRegions, registries.worldMapAvatars, registries.worldMapTraversalProfiles,
      registries.worldMapPresentationProfiles, registries.worldMapBehaviours)) {
      for(final RegistryId contributor : registry) mods.add(contributor.modId());
    }
    builder.encounterPools.values().forEach(pool -> pool.encounters().forEach(encounter -> mods.add(encounter.modId())));
    return builder.requiredMods(mods).build();
  }

  /** Retail source tables only; never reads active registries or invokes asset providers. */
  public static WorldMapPreset vanilla() {
    final WorldMapDefinition definition = LegacyWorldMap.importDefinition(LodWorldMapData.locations_800f0e34,
      LodWorldMapData.places_800f0234, LodWorldMapData.directionalPathSegmentData_800f2248,
      LodWorldMapData.pathDotPosArr_800f591c, LodWorldMapData.pathSegmentLengths_800f5810);
    final Builder builder = new Builder(lod("wmap_vanilla"), "Vanilla world map")
      .description("Complete retail world map. Native extracted assets and lod providers are required.").requiredMods(Set.of("lod"));
    definition.nodes().forEach(value -> builder.nodes.put(value.id(), value));
    definition.places().forEach(value -> {
      final RegistryId thumbnailId = LodWorldMapAuthoringData.thumbnailId(value.thumbnail());
      builder.thumbnailDefinitions.putIfAbsent(thumbnailId, new ThumbnailDefinition(value.thumbnail(), null, "Native thumbnail " + value.thumbnail(), null));
      final List<RegistryId> services = new ArrayList<>();
      for(int bit = 0; bit < 5; bit++) {
        if((value.services() & 1 << bit) != 0) {
          final RegistryId serviceId = LodWorldMapAuthoringData.serviceId(bit);
          services.add(serviceId);
          builder.serviceDefinitions.putIfAbsent(serviceId, new WorldMapService(WorldMapPresentationProfile.legacy().services().get(bit), bit));
        }
      }
      final List<RegistryId> sounds = value.sounds().stream().filter(sound -> sound > 0).map(sound -> {
        final RegistryId soundId = LodWorldMapAuthoringData.soundId(sound);
        builder.soundDefinitions.putIfAbsent(soundId, new WorldMapSound(sound, "Native location sound " + sound));
        return soundId;
      }).toList();
      builder.places.put(value.id(), new WorldMapPlace(value.id(), value.legacyIndex(), value.name(), value.thumbnail(), value.services(),
        value.sounds(), thumbnailId, services, sounds));
    });
    definition.portals().forEach(value -> {
      final RegistryId fromId = LodWorldMapAuthoringData.submapDestinationId(value.from());
      final RegistryId toId = LodWorldMapAuthoringData.submapDestinationId(value.to());
      builder.submapDestinations.putIfAbsent(fromId, new WorldMapSubmapDestination(value.from().cut(), value.from().scene(),
        "Submap cut " + value.from().cut() + ", scene " + value.from().scene()));
      builder.submapDestinations.putIfAbsent(toId, new WorldMapSubmapDestination(value.to().cut(), value.to().scene(),
        "Submap cut " + value.to().cut() + ", scene " + value.to().scene()));
      final WorldMapPresentation presentation = WorldMapPresentation.from(value);
      builder.portals.put(value.id(), new WorldMapPortal(value.id(), value.legacyIndex(), value.route(), value.place(), value.from(), value.to(),
        value.junctionIndex(), value.continent(), value.fullBrightness(), value.effectFlags(), value.region(), fromId, toId,
        presentation.atmosphere(), presentation.smoke()));
    });
    for(int i = 0; i < definition.geometry().size(); i++) builder.geometry.put(lod("wmap_geometry_" + i), new WorldMapGeometry(i, definition.geometry().get(i)));
    for(final WorldMapRoute route : definition.routes()) {
      final RegistryId battleStageId = LodWorldMapAuthoringData.battleStageId(route.battleStage());
      builder.battleStageDefinitions.putIfAbsent(battleStageId, new WorldMapBattleStage(route.battleStage(),
        route.battleStage() == -1 ? "Default world-map stage" : "Native battle stage " + route.battleStage()));
      builder.routes.put(route.id(), new WorldMapRouteData(route.legacyIndex(), route.start(), route.end(), lod("wmap_geometry_" + route.segmentIndex()),
        route.direction(), route.encounterRate(), route.battleStage(), route.encounterIndex() < 0 || route.encounterIndex() >= LodWorldMapData.encounterIds_800ef364.length ? null : lod("wmap_encounter_pool_" + route.encounterIndex()),
        route.modelIndex(), route.encounterIndex(), route.avatar(), battleStageId));
    }
    for(int i = 0; i < 49; i++) {
      final var value = LodWorldMapData.wmapDestinationMarkers_800f5a6c[i];
      final List<RegistryId> enabled = new ArrayList<>();
      for(final WorldMapPortal portal : definition.portals()) {
        if((value.flags_04[portal.legacyIndex() >>> 5] & 1 << (portal.legacyIndex() & 31)) != 0) enabled.add(portal.id());
      }
      builder.storyPresets.put(lod("wmap_story_preset_" + i), new WorldMapStoryPreset(i, value.packedFlag_00, enabled, value.x_24, value.y_26, i == 0 ? null : definition.place(value.placeIndex_28).id()));
    }
    for(int i = 0; i < LodWorldMapData.coolonWarpDest_800ef228.length; i++) {
      final var value = LodWorldMapData.coolonWarpDest_800ef228[i];
      builder.coolonDestinations.put(lod("wmap_coolon_destination_" + i), new WorldMapCoolonDestination(i, definition.portal(value.locationIndex_10).id(), lod("wmap_coolon_destination_" + value.defaultDestLocationIndex_14),
        new WorldMapPoint(value.destPosition_00.x, value.destPosition_00.y, value.destPosition_00.z), value.x_18, value.y_1a, value.placeName_1c, i != 8, i == 8));
    }
    for(int i = 0; i < LodWorldMapData.teleportationEndpointIndices_800ef698.length; i++) {
      final int[] endpoints = LodWorldMapData.teleportationEndpointIndices_800ef698[i];
      TeleportationLocation0c source = null;
      for(final TeleportationLocation0c candidate : LodWorldMapData.teleportationLocations_800ef6c8) {
        if(candidate.locationIndex_00 == endpoints[0]) source = candidate;
      }
      if(source == null) throw new IllegalStateException("Missing vanilla teleport placement " + endpoints[0]);
      builder.teleportLinks.put(lod("wmap_teleport_link_" + i), new WorldMapTeleportLink(i, definition.portal(endpoints[0]).id(), definition.portal(endpoints[1]).id(),
        new WorldMapPoint(source.translation_04.x, source.translation_04.y, source.translation_04.z), i != 4));
    }
    for(int i = 0; i < LodWorldMapData.encounterIds_800ef364.length; i++) {
      final List<RegistryId> ids = Arrays.stream(LodWorldMapData.encounterIds_800ef364[i]).mapToObj(index -> lod(LodEncounters.LEGACY[index])).toList();
      builder.encounterPools.put(lod("wmap_encounter_pool_" + i), new WorldMapEncounterPool(i, ids));
    }
    for(final Continent continent : Continent.values()) {
      if(continent != Continent.NONE_8) {
        final RegistryId id = WorldMapRegion.legacyId(continent);
        builder.regions.put(id, new Region(continent, id, id, WorldMapCameraSettings.legacy(null), null));
      }
    }
    for(final String avatar : List.of("leader", "dart", "lavitz", "shana", "rose", "haschel", "albert", "meru", "kongol", "miranda", "ship", "coolon", "teleport")) {
      final RegistryId id = lod("wmap_avatar_" + avatar);
      builder.avatars.put(id, new Avatar(id, null));
    }
    final WorldMapPresentationProfile presentation = WorldMapPresentationProfile.legacy();
    builder.presentationProfiles.put(lod("wmap_presentation"), new PresentationProfile(presentation.mapPositions(), presentation.regions(), presentation.services(),
      presentation.waterClutYs(), presentation.playerAvatarVramSlots(), presentation.textureAdjustments().stream().map(TextureAdjustment::from).toList()));
    return builder.behaviours(Set.of(lod("wmap_behaviour"))).build();
  }

  private static RegistryId lod(final String entry) { return new RegistryId("lod", entry); }

  /** Mutable authoring convenience; build defensively copies every collection. */
  public static final class Builder {
    public final RegistryId id;
    public final String name;
    public String description = "";
    public Set<String> requiredMods = Set.of();
    public Path packageRoot = Path.of(".");
    public final Map<RegistryId, ThumbnailDefinition> thumbnailDefinitions = new LinkedHashMap<>();
    public final Map<RegistryId, WorldMapService> serviceDefinitions = new LinkedHashMap<>();
    public final Map<RegistryId, WorldMapSound> soundDefinitions = new LinkedHashMap<>();
    public final Map<RegistryId, WorldMapBattleStage> battleStageDefinitions = new LinkedHashMap<>();
    public final Map<RegistryId, WorldMapSubmapDestination> submapDestinations = new LinkedHashMap<>();
    public final Map<RegistryId, WorldMapNode> nodes = new LinkedHashMap<>();
    public final Map<RegistryId, WorldMapGeometry> geometry = new LinkedHashMap<>();
    public final Map<RegistryId, WorldMapPlace> places = new LinkedHashMap<>();
    public final Map<RegistryId, WorldMapRouteData> routes = new LinkedHashMap<>();
    public final Map<RegistryId, WorldMapPortal> portals = new LinkedHashMap<>();
    public final Map<RegistryId, WorldMapEncounterPool> encounterPools = new LinkedHashMap<>();
    public final Map<RegistryId, WorldMapStoryPreset> storyPresets = new LinkedHashMap<>();
    public final Map<RegistryId, WorldMapCoolonDestination> coolonDestinations = new LinkedHashMap<>();
    public final Map<RegistryId, WorldMapTeleportLink> teleportLinks = new LinkedHashMap<>();
    public final Map<RegistryId, Region> regions = new LinkedHashMap<>();
    public final Map<RegistryId, Avatar> avatars = new LinkedHashMap<>();
    public final Map<RegistryId, TraversalProfile> traversalProfiles = new LinkedHashMap<>();
    public final Map<RegistryId, PresentationProfile> presentationProfiles = new LinkedHashMap<>();
    public Rules rules = Rules.EMPTY;
    @Nullable public Set<RegistryId> behaviours;
    public final Set<Removal> removals = new HashSet<>();
    public final Map<RegistryId, String> thumbnails = new LinkedHashMap<>();

    public Builder(final RegistryId id, final String name) { this.id = id; this.name = name; }
    public Builder description(final String value) { this.description = value; return this; }
    public Builder requiredMods(final Set<String> value) { this.requiredMods = Set.copyOf(value); return this; }
    public Builder packageRoot(final Path value) { this.packageRoot = value; return this; }
    public Builder rules(final Rules value) { this.rules = value; return this; }
    public Builder behaviours(@Nullable final Set<RegistryId> value) { this.behaviours = value; return this; }

    public WorldMapPreset build() {
      return new WorldMapPreset(this.id, this.name, this.description, this.requiredMods, this.packageRoot,
        this.thumbnailDefinitions, this.serviceDefinitions, this.soundDefinitions, this.battleStageDefinitions, this.submapDestinations, this.nodes,
        this.geometry, this.places, this.routes, this.portals, this.encounterPools, this.storyPresets, this.coolonDestinations,
        this.teleportLinks, this.regions, this.avatars, this.traversalProfiles, this.presentationProfiles, this.rules, this.behaviours, this.removals, this.thumbnails);
    }
  }
}
