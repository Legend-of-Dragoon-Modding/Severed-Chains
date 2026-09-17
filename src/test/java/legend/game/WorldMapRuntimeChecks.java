package legend.game;

import legend.core.tags.Tag;
import legend.game.combat.Battle;
import legend.game.combat.postbattleactions.PostBattleAction;
import legend.game.combat.postbattleactions.PostBattleActionInstance;
import legend.game.scripting.RunningScript;
import legend.game.types.GameState52c;
import legend.game.wmap.WMap;
import legend.game.wmap.world.WorldMapAction;
import legend.game.wmap.world.WorldMapLegacyAdapter;
import legend.game.wmap.world.WorldMapPortalState;
import legend.game.wmap.world.WorldMapSave;
import legend.game.wmap.world.WorldMapTransition;
import legend.game.wmap.world.WorldMapTravelRequestResult;
import legend.game.wmap.world.WorldMapTravelTarget;
import legend.lodmod.LodEngineStateTypes;
import org.legendofdragoon.modloader.registries.RegistryId;

import static legend.game.EngineStates.currentEngineState_8004dd04;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.postBattleAction_800bc974;
import static legend.game.EngineStates.postBattleEngineState_800bc91c;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Real native world loading, access resolution, fades, travel, and in-memory save restoration. */
public final class WorldMapRuntimeChecks {
  private static final RegistryId START = new RegistryId("lod", "wmap_location_5");

  private WorldMapRuntimeChecks() { }

  public static void runAfterBattle() {
    final WorldMapPortalState previous = Harness.onEngineThread(() -> {
      assertInstanceOf(Battle.class, currentEngineState_8004dd04);
      final WorldMapPortalState snapshot = new WorldMapPortalState();
      snapshot.set(gameState_800babc8.worldMapPortalState);
      // The fresh test campaign has not run Hellena's escape script yet.
      // Admit its native starting portal through the real progression override API.
      gameState_800babc8.worldMapPortalState.setEnabled(START, true);
      postBattleAction_800bc974 = new ReturnToWorldMapAction().inst(null);
      return snapshot;
    });

    try {
      waitForPlayableWorldMap();
      final WorldMapTravelTarget.Route target = Harness.onEngineThread(() -> {
        final WMap map = assertInstanceOf(WMap.class, currentEngineState_8004dd04);
        assertTrue(WorldMapLegacyAdapter.isNativeLayout(map.getWorldMapDefinition()));
        assertFalse(map.getWorldMapDefinition().routes().isEmpty());
        assertNotNull(map.getWorldMapRegion());
        assertTrue(map.canSave());

        gameState_800babc8.worldMapPortalState.setEnabled(START, false);
        assertFalse(map.queryWorldMap(START, WorldMapAction.TRAVERSE).allowed());
        assertEquals(WorldMapTravelRequestResult.DENIED, map.requestWorldMapTravel(new WorldMapTravelTarget.Portal(START), true));
        assertEquals(WorldMapTransition.Phase.IDLE, map.worldMapTransitionPhase());
        gameState_800babc8.worldMapPortalState.setEnabled(START, true);

        final RegistryId route = map.getWorldMapDefinition().portal(START).route();
        final WorldMapTravelTarget.Route destination = WorldMapTravelTarget.atRouteDistance(route, 0.370000000123);
        assertTrue(map.requestTravel(EngineDestination.worldMap(destination)));
        assertFalse(map.canSave(), "Pending travel must block saving");
        assertEquals(WorldMapTravelRequestResult.BUSY, map.requestWorldMapTravel(destination));
        return destination;
      });

      waitForPlayableWorldMap();
      Harness.onEngineThread(() -> {
        final WMap map = assertInstanceOf(WMap.class, currentEngineState_8004dd04);
        final WorldMapTravelTarget.Route actual = assertInstanceOf(WorldMapTravelTarget.Route.class, map.currentWorldMapTarget());
        assertEquals(target.id(), actual.id());
        assertEquals(target.progress(), actual.progress(), 0.00001f);
        assertEquals(actual, map.currentWorldMapTarget(), "Reading position must not advance traversal");

        // Exercise engine serialization and versioned restoration using scratch state only.
        final GameState52c written = new GameState52c();
        final Tag saved = map.writeSaveData(written);
        final GameState52c restored = new GameState52c();
        final RegistryId savedRoute = WorldMapSave.read(restored, saved);
        assertEquals(target.id(), savedRoute);
        WorldMapSave.restoreRoute(restored, map.getWorldMapDefinition(), savedRoute, saved);
        final var route = map.getWorldMapDefinition().route(savedRoute);
        final var geometry = map.getWorldMapDefinition().geometry().get(route.segmentIndex());
        if(saved.asMap().has("routeDistance")) {
          assertEquals(target.preciseProgress(), actual.preciseProgress(), 0.000000000001);
          final double distance = legend.game.wmap.world.WorldMapPositionSave.read(saved, route, geometry, restored.dotIndex_4da, restored.dotOffset_4dc);
          assertTrue(Double.isFinite(distance));
          assertEquals(route.direction() > 0 ? target.preciseProgress() : 1.0 - target.preciseProgress(), distance / new legend.game.wmap.world.WorldMapRouteMetric(geometry).length(), 0.000000000001);
        }
        assertEquals(written.directionalPathIndex_4de, restored.directionalPathIndex_4de);
        assertEquals(written.pathIndex_4d8, restored.pathIndex_4d8);
        assertEquals(written.dotIndex_4da, restored.dotIndex_4da);
        assertEquals(written.dotOffset_4dc, restored.dotOffset_4dc, 0.00001f);
        assertTrue(map.canSave());
        return null;
      });
    } finally {
      Harness.onEngineThread(() -> {
        gameState_800babc8.worldMapPortalState.set(previous);
        if(currentEngineState_8004dd04 instanceof final WMap map) map.invalidateWorldMap();
        return null;
      });
    }
  }

  public static void runPresetSwitch() {
    Harness.onEngineThread(() -> {
      final WMap map = assertInstanceOf(WMap.class, currentEngineState_8004dd04);
      gameState_800babc8.worldMapPortalState.setEnabled(START, true);
      final var route = map.getWorldMapDefinition().route(map.getWorldMapDefinition().portal(START).route());
      final var preset = new legend.game.wmap.preset.WorldMapPreset.Builder(new RegistryId("test", "owned_travel"), "E2E authored presentation");
      preset.geometry.put(new RegistryId("lod", "wmap_geometry_" + route.segmentIndex()), new legend.game.wmap.world.WorldMapGeometry(route.segmentIndex(), map.getWorldMapDefinition().geometry().get(route.segmentIndex()), legend.game.wmap.world.WorldMapGeometry.Motion.DISTANCE, 1.0f));
      preset.presentationProfiles.put(new RegistryId("lod", "wmap_presentation"), new legend.game.wmap.preset.WorldMapPreset.PresentationProfile(null, null, null, null, null, null,
        java.util.List.of(new legend.game.wmap.world.WorldMapPresentationElement(new RegistryId("test", "caption"), new legend.game.wmap.world.WorldMapPoint(1, 2, 3), "Test caption", null)), java.util.List.of(),
        new legend.game.wmap.world.WorldMapPresentationCapabilities(false, false, true)));
      assertEquals(WorldMapTravelRequestResult.ACCEPTED, map.requestWorldMapPreset(preset.build()));
      assertEquals(WorldMapTravelRequestResult.BUSY, map.requestWorldMapPreset(null));
      return null;
    });
    waitForPlayableWorldMap();
    Harness.onEngineThread(() -> {
      final WMap map = (WMap)currentEngineState_8004dd04;
      assertFalse(map.getWorldMapPresentation().supportsRetailLabels());
      assertEquals("Test caption", map.getWorldMapPresentation().element(new RegistryId("test", "caption")).label());
      final var target = WorldMapTravelTarget.atRouteDistance(map.getWorldMapDefinition().portal(START).route(), 0.370000000123);
      assertEquals(WorldMapTravelRequestResult.ACCEPTED, map.requestWorldMapTravel(target));
      return null;
    });
    waitForPlayableWorldMap();
    Harness.onEngineThread(() -> {
      final WMap map = (WMap)currentEngineState_8004dd04;
      final var target = assertInstanceOf(WorldMapTravelTarget.Route.class, map.currentWorldMapTarget());
      assertEquals(0.370000000123, target.preciseProgress(), 1.0e-12);
      final GameState52c scratch = new GameState52c();
      final Tag saved = map.writeSaveData(scratch);
      assertTrue(saved.asMap().has("routeDistance"));
      final var route = map.getWorldMapDefinition().route(target.id());
      final double retained = legend.game.wmap.world.WorldMapPositionSave.read(saved, route, map.getWorldMapDefinition().geometry().get(route.segmentIndex()), scratch.dotIndex_4da, scratch.dotOffset_4dc);
      assertTrue(Double.isFinite(retained));
      assertEquals(WorldMapTravelRequestResult.ACCEPTED, map.requestWorldMapPreset(null));
      return null;
    });
    waitForPlayableWorldMap();
    Harness.onEngineThread(() -> {
      assertEquals("", gameState_800babc8.worldMapPreset);
      assertTrue(((WMap)currentEngineState_8004dd04).getWorldMapPresentation().supportsRetailLabels());
      gameState_800babc8.worldMapPortalState.clearEnabledOverride(START);
      return null;
    });
  }

  private static void waitForPlayableWorldMap() {
    Wait.waitFor(() -> Harness.onEngineThread(() -> currentEngineState_8004dd04 instanceof final WMap map && map.worldMapTransitionPhase() == WorldMapTransition.Phase.IDLE && map.canSave()), 60_000, "native world map travel activation to complete");
  }

  /** Uses Battle's normal fade, script/bent cleanup, and engine-return stages. */
  private static final class ReturnToWorldMapAction extends PostBattleAction<ReturnInstance, ReturnToWorldMapAction> {
    @Override
    protected int getTotalDuration(final Battle battle, final ReturnInstance instance) {
      return 1;
    }

    @Override
    protected int getFadeDuration(final Battle battle, final ReturnInstance instance) {
      return 1;
    }

    @Override
    protected void onCameraFadeoutStart(final Battle battle, final ReturnInstance instance) { }

    @Override
    protected void onCameraFadeoutFinish(final Battle battle, final ReturnInstance instance) { }

    @Override
    protected void performAction(final Battle battle, final ReturnInstance instance) {
      postBattleEngineState_800bc91c = LodEngineStateTypes.WORLD_MAP.get();
      EngineStates.engineStateData = EngineDestination.worldMap(new WorldMapTravelTarget.Portal(START)).data();
    }

    @Override
    public ReturnInstance inst(final RunningScript<?> script) {
      return new ReturnInstance(this);
    }
  }

  private static final class ReturnInstance extends PostBattleActionInstance<ReturnToWorldMapAction, ReturnInstance> {
    private ReturnInstance(final ReturnToWorldMapAction action) {
      super(action);
    }
  }
}
