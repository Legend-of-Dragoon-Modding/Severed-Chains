package legend.game.wmap.world;

import legend.game.modding.events.worldmap.WorldMapArrivalEvent;
import legend.game.modding.events.worldmap.WorldMapJunctionEvent;
import legend.game.wmap.Continent;
import org.junit.jupiter.api.Test;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WorldMapEventsTest {
  @Test
  void unchangedArrivalPreservesRetailFallbackWithoutRecheckingItsFlag() {
    final WorldMapDefinition definition = LegacyWorldMap.importDefinition();
    final WorldMapPortal fallback = definition.portal(5);
    assertEquals(fallback, WorldMapArrivalEvent.validateSelection(fallback.id(), fallback.id(), new SubmapEndpoint(13, 17), definition, index -> {
      throw new AssertionError("Unchanged fallback must not be filtered a second time");
    }));
  }

  @Test
  void changedArrivalRequiresKnownAllowedPortalButCanUseQueenFurySentinel() {
    final WorldMapDefinition definition = LegacyWorldMap.importDefinition();
    final RegistryId original = definition.portal(5).id();
    final WorldMapPortal queenFury = definition.portal(93);
    assertEquals(null, queenFury.route());
    assertEquals(queenFury, WorldMapArrivalEvent.validateSelection(original, queenFury.id(), new SubmapEndpoint(242, 3), definition, index -> index == 93));
    assertThrows(IllegalArgumentException.class, () -> WorldMapArrivalEvent.validateSelection(original, definition.portal(0).id(), new SubmapEndpoint(13, 17), definition, index -> false));
    assertThrows(IllegalArgumentException.class, () -> WorldMapArrivalEvent.validateSelection(original, new RegistryId("test", "unknown"), new SubmapEndpoint(13, 17), definition, index -> true));
    assertThrows(NullPointerException.class, () -> WorldMapArrivalEvent.validateSelection(original, null, new SubmapEndpoint(13, 17), definition, index -> true));
    assertThrows(NullPointerException.class, () -> WorldMapArrivalEvent.validateSelection(original, original, null, definition, index -> true));
  }

  private static WorldMapTraversal.Connection connection(final int portal) {
    return new WorldMapTraversal.Connection(portal, 0, 1, Continent.SOUTH_SERDIO_0, new WorldMapPoint(10, 0, 20));
  }

  @Test
  void junctionAllowsFilteringReorderingAndOriginalMultiplicity() {
    final var first = connection(1);
    final var second = connection(2);
    final List<WorldMapTraversal.Connection> available = List.of(first, second, first);
    assertEquals(available, WorldMapJunctionEvent.validateConnections(available, available), "no listener preserves exact vanilla order and duplicates");
    assertEquals(List.of(second, first, first), WorldMapJunctionEvent.validateConnections(available, List.of(second, first, first)));
    assertEquals(List.of(second), WorldMapJunctionEvent.validateConnections(available, List.of(second)));
    assertEquals(List.of(), WorldMapJunctionEvent.validateConnections(available, List.of()));
    final List<WorldMapTraversal.Connection> selected = new ArrayList<>(available);
    final List<WorldMapTraversal.Connection> result = WorldMapJunctionEvent.validateConnections(available, selected);
    selected.clear();
    assertEquals(available, result, "validated result is detached from listener list");
    assertThrows(UnsupportedOperationException.class, () -> result.add(first));
  }

  @Test
  void junctionRejectsInventedConnectionsExtraDuplicatesAndNulls() {
    final var first = connection(1);
    final List<WorldMapTraversal.Connection> available = List.of(first);
    assertThrows(IllegalArgumentException.class, () -> WorldMapJunctionEvent.validateConnections(available, List.of(connection(2))));
    assertThrows(IllegalArgumentException.class, () -> WorldMapJunctionEvent.validateConnections(available, List.of(first, first)));
    final var changedPoint = new WorldMapTraversal.Connection(1, 0, 1, Continent.SOUTH_SERDIO_0, new WorldMapPoint(99, 0, 20));
    assertThrows(IllegalArgumentException.class, () -> WorldMapJunctionEvent.validateConnections(available, List.of(changedPoint)));
    final List<WorldMapTraversal.Connection> withNull = new ArrayList<>();
    withNull.add(null);
    assertThrows(NullPointerException.class, () -> WorldMapJunctionEvent.validateConnections(available, withNull));
  }
}
