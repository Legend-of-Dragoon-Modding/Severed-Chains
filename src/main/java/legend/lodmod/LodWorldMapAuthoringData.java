package legend.lodmod;

import legend.core.GameEngine;
import legend.game.wmap.Location14;
import legend.game.wmap.Place0c;
import legend.game.wmap.registries.RegisterWorldMapBattleStagesEvent;
import legend.game.wmap.registries.RegisterWorldMapServicesEvent;
import legend.game.wmap.registries.RegisterWorldMapSoundsEvent;
import legend.game.wmap.registries.RegisterWorldMapSubmapDestinationsEvent;
import legend.game.wmap.registries.RegisterWorldMapThumbnailsEvent;
import legend.game.wmap.registries.WorldMapBattleStageEntry;
import legend.game.wmap.registries.WorldMapServiceEntry;
import legend.game.wmap.registries.WorldMapSoundEntry;
import legend.game.wmap.registries.WorldMapSubmapDestinationEntry;
import legend.game.wmap.registries.WorldMapThumbnailEntry;
import legend.game.wmap.world.SubmapEndpoint;
import legend.game.wmap.world.WorldMapBattleStage;
import legend.game.wmap.world.WorldMapService;
import legend.game.wmap.world.WorldMapSound;
import legend.game.wmap.world.WorldMapSubmapDestination;
import legend.game.wmap.world.WorldMapThumbnail;
import org.legendofdragoon.modloader.registries.Registrar;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.LinkedHashSet;
import java.util.Set;

/** Native reusable definitions referenced by authored world-map data. */
public final class LodWorldMapAuthoringData {
  private static final Registrar<WorldMapThumbnailEntry, RegisterWorldMapThumbnailsEvent> THUMBNAILS = new Registrar<>(GameEngine.REGISTRIES.worldMapThumbnails, LodMod.MOD_ID);
  private static final Registrar<WorldMapServiceEntry, RegisterWorldMapServicesEvent> SERVICES = new Registrar<>(GameEngine.REGISTRIES.worldMapServices, LodMod.MOD_ID);
  private static final Registrar<WorldMapSoundEntry, RegisterWorldMapSoundsEvent> SOUNDS = new Registrar<>(GameEngine.REGISTRIES.worldMapSounds, LodMod.MOD_ID);
  private static final Registrar<WorldMapBattleStageEntry, RegisterWorldMapBattleStagesEvent> BATTLE_STAGES = new Registrar<>(GameEngine.REGISTRIES.worldMapBattleStages, LodMod.MOD_ID);
  private static final Registrar<WorldMapSubmapDestinationEntry, RegisterWorldMapSubmapDestinationsEvent> SUBMAP_DESTINATIONS = new Registrar<>(GameEngine.REGISTRIES.worldMapSubmapDestinations, LodMod.MOD_ID);

  private static final String[] SERVICE_NAMES = {"Save Point", "Hotel", "Clinic", "Weapon Shop", "Item Shop"};

  static {
    final Set<Integer> thumbnails = new LinkedHashSet<>();
    final Set<Integer> sounds = new LinkedHashSet<>();
    for(final Place0c place : LodWorldMapData.places_800f0234) {
      thumbnails.add(place.fileIndex_04);
      for(final int sound : place.soundIndices_06) {
        if(sound > 0) sounds.add(sound);
      }
    }
    for(final int index : thumbnails) {
      THUMBNAILS.register(thumbnailId(index).entryId(), () -> new WorldMapThumbnailEntry(id -> new WorldMapThumbnail(index, null, "Native thumbnail " + index, null)));
    }
    for(int i = 0; i < SERVICE_NAMES.length; i++) {
      final int index = i;
      SERVICES.register(serviceId(index).entryId(), () -> new WorldMapServiceEntry(id -> new WorldMapService(SERVICE_NAMES[index], index)));
    }
    for(final int index : sounds) {
      SOUNDS.register(soundId(index).entryId(), () -> new WorldMapSoundEntry(id -> new WorldMapSound(index, "Native location sound " + index)));
    }

    final Set<Integer> stages = new LinkedHashSet<>();
    for(final var route : LodWorldMapData.directionalPathSegmentData_800f2248) stages.add(route.battleStage_04);
    for(final int index : stages) {
      final String label = index == -1 ? "Default world-map stage" : "Native battle stage " + index;
      BATTLE_STAGES.register(battleStageId(index).entryId(), () -> new WorldMapBattleStageEntry(id -> new WorldMapBattleStage(index, label)));
    }

    final Set<SubmapEndpoint> endpoints = new LinkedHashSet<>();
    for(final Location14 portal : LodWorldMapData.locations_800f0e34) {
      endpoints.add(new SubmapEndpoint(portal.submapCutFrom_04, portal.submapSceneFrom_06));
      endpoints.add(new SubmapEndpoint(portal.submapCutTo_08, portal.submapSceneTo_0a));
    }
    for(final SubmapEndpoint endpoint : endpoints) {
      SUBMAP_DESTINATIONS.register(submapDestinationId(endpoint).entryId(), () -> new WorldMapSubmapDestinationEntry(id ->
        new WorldMapSubmapDestination(endpoint.cut(), endpoint.scene(), "Submap cut " + endpoint.cut() + ", scene " + endpoint.scene())));
    }
  }

  private LodWorldMapAuthoringData() { }

  public static RegistryId thumbnailId(final int index) {
    return id("wmap_thumbnail_" + number(index));
  }

  public static RegistryId serviceId(final int bit) {
    return id("wmap_service_" + switch(bit) {
      case 0 -> "save_point";
      case 1 -> "hotel";
      case 2 -> "clinic";
      case 3 -> "weapon_shop";
      case 4 -> "item_shop";
      default -> throw new IllegalArgumentException("Unknown native WMAP service bit " + bit);
    });
  }

  public static RegistryId soundId(final int index) {
    return id("wmap_sound_" + number(index));
  }

  public static RegistryId battleStageId(final int index) {
    return id(index == -1 ? "wmap_battle_stage_default" : "wmap_battle_stage_" + number(index));
  }

  public static RegistryId submapDestinationId(final SubmapEndpoint endpoint) {
    return id("wmap_submap_" + number(endpoint.cut()) + '_' + number(endpoint.scene()));
  }

  private static String number(final int value) {
    return value < 0 ? "n" + -(long)value : Integer.toString(value);
  }

  private static RegistryId id(final String entry) {
    return new RegistryId(LodMod.MOD_ID, entry);
  }

  static void register(final RegisterWorldMapThumbnailsEvent event) {
    THUMBNAILS.registryEvent(event);
  }

  static void register(final RegisterWorldMapServicesEvent event) {
    SERVICES.registryEvent(event);
  }

  static void register(final RegisterWorldMapSoundsEvent event) {
    SOUNDS.registryEvent(event);
  }

  static void register(final RegisterWorldMapBattleStagesEvent event) {
    BATTLE_STAGES.registryEvent(event);
  }

  static void register(final RegisterWorldMapSubmapDestinationsEvent event) {
    SUBMAP_DESTINATIONS.registryEvent(event);
  }
}
