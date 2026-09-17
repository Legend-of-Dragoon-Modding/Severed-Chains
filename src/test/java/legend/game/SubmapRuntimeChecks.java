package legend.game;

import legend.game.submap.SMap;
import legend.game.submap.SubmapState;
import legend.game.wmap.WMap;
import legend.game.wmap.world.SubmapEndpoint;
import legend.game.wmap.world.WorldMapTransition;
import legend.game.wmap.world.WorldMapTravelTarget;
import legend.lodmod.LodEngineStateTypes;

import static legend.game.EngineStates.currentEngineState_8004dd04;
import static legend.game.Scus94491BpeSegment_800b.submapFullyLoaded_800bd7b4;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Real retail asset loading and typed return travel; no synthetic providers or scripts. */
public final class SubmapRuntimeChecks {
  // LodWorldMapData.locations[0]: the authored Forest arrival from the native world map.
  private static final SubmapEndpoint FOREST_ENTRY = new SubmapEndpoint(7, 12);

  private SubmapRuntimeChecks() { }

  public static void runAfterWorldMap() {
    final Journey journey = Harness.onEngineThread(() -> {
      final WMap map = assertInstanceOf(WMap.class, currentEngineState_8004dd04);
      assertTrue(map.canSave());
      final EngineDestination returnTo = EngineDestination.worldMap(map.currentWorldMapTarget());
      assertTrue(map.requestTravel(new EngineTransition(EngineDestination.submap(FOREST_ENTRY), returnTo)));
      return new Journey(map, returnTo);
    });

    Wait.waitFor(() -> Harness.onEngineThread(() -> currentEngineState_8004dd04 instanceof final SMap submap
      && submap.smapLoadingStage_800cb430 == SubmapState.RENDER_SUBMAP_12 && submapFullyLoaded_800bd7b4
      && submap.submapLifetime().pendingCount() == 0), 60_000, "native Forest submap assets and scripts ready");

    final SMap loaded = Harness.onEngineThread(() -> {
      final SMap submap = assertInstanceOf(SMap.class, currentEngineState_8004dd04);
      assertTrue(journey.source.lifetime().isClosed(), "Departed world map lifetime must close");
      assertFalse(submap.lifetime().isClosed());
      assertFalse(submap.submapLifetime().isClosed());
      assertNotNull(submap.returnDestination());
      assertEquals(LodEngineStateTypes.WORLD_MAP.getId(), submap.returnDestination().engineState());
      assertEquals(EngineDestination.worldMapTarget(journey.returnTo.data()), EngineDestination.worldMapTarget(submap.returnDestination().data()));
      return submap;
    });

    Wait.waitFor(() -> Harness.onEngineThread(() -> {
      assertEquals(loaded, currentEngineState_8004dd04, "Native submap should remain active until return is requested");
      return loaded.requestReturn();
    }), 15_000, "native submap accepts its typed return destination");

    Wait.waitFor(() -> Harness.onEngineThread(() -> currentEngineState_8004dd04 instanceof final WMap map
      && map.worldMapTransitionPhase() == WorldMapTransition.Phase.IDLE && map.canSave()),
      60_000, "world map playable after native submap return");

    Harness.onEngineThread(() -> {
      final WMap map = assertInstanceOf(WMap.class, currentEngineState_8004dd04);
      assertTrue(loaded.lifetime().isClosed(), "Departed submap lifetime must close");
      assertTrue(loaded.submapLifetime().isClosed(), "Departed submap resource generation must close");
      final WorldMapTravelTarget.Route expected = assertInstanceOf(WorldMapTravelTarget.Route.class, EngineDestination.worldMapTarget(journey.returnTo.data()));
      final WorldMapTravelTarget.Route actual = assertInstanceOf(WorldMapTravelTarget.Route.class, map.currentWorldMapTarget());
      assertEquals(expected.id(), actual.id());
      assertEquals(expected.progress(), actual.progress(), 0.0001f);
      return null;
    });
  }

  private record Journey(WMap source, EngineDestination returnTo) { }
}
