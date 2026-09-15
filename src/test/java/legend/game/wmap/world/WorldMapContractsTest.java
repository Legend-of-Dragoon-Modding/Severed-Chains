package legend.game.wmap.world;

import legend.game.progression.CampaignProgression;
import legend.game.types.Flags;
import legend.game.wmap.Continent;
import org.junit.jupiter.api.Test;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WorldMapContractsTest {
  private static final RegistryId PORTAL = new RegistryId("test", "portal");

  @Test
  void legacyFlagWritesInvalidateCampaignSnapshotsWithoutChangingPastFacts() {
    final Flags story = new Flags(32);
    final Flags locations = new Flags(1);
    final CampaignProgression campaign = new CampaignProgression(story, locations);
    final CampaignProgression.Snapshot before = campaign.snapshot();
    story.set(7, true);
    assertTrue(campaign.revision() > before.revision);
    assertFalse(before.storyFlag(7));
    final long changed = campaign.revision();
    story.set(7, true);
    assertEquals(changed, campaign.revision());
    locations.setRaw(0, 4);
    assertTrue(campaign.revision() > changed);
  }

  @Test
  void explicitPortalOverrideSurvivesRebindingAndCanBeCleared() {
    final WorldMapPortalState identities = new WorldMapPortalState();
    final Flags enabled = new Flags(1);
    final Flags visited = new Flags(1);
    final WorldMapDefinition original = definition(PORTAL);
    identities.bind(original, enabled, visited, true);
    identities.setEnabled(PORTAL, false);
    identities.applyOverrides(original, enabled);
    assertFalse(enabled.get(0));
    final WorldMapPortalState restored = new WorldMapPortalState();
    restored.read(identities.write(enabled, visited));
    final WorldMapDefinition reordered = definition(new RegistryId("test", "other"), PORTAL);
    restored.bind(reordered, enabled, visited, true);
    enabled.set(1, true); // A story default is still subordinate to an authored override
    restored.applyOverrides(reordered, enabled);
    assertFalse(enabled.get(1));
    restored.clearEnabledOverride(PORTAL);
    enabled.set(1, true);
    restored.applyOverrides(reordered, enabled);
    assertTrue(enabled.get(1));
  }

  @Test
  void unevenRoutesRetainExactEndpointsAndSkipZeroLengthIntervals() {
    final WorldMapRouteMetric metric = new WorldMapRouteMetric(List.of(point(0), point(0), point(1), point(100)));
    final WorldMapTraversalPosition position = new WorldMapTraversalPosition();
    position.set(new RegistryId("test", "route"), metric, 0);
    final WorldMapRouteMetric.Cursor first = position.advance(50);
    assertEquals(50.0, position.distance());
    assertEquals(0.5f, position.progress());
    assertEquals(2, first.index());
    position.synchronize(position.route(), metric, first.index(), first.offset());
    assertEquals(50.0, position.distance());
    final WorldMapRouteMetric.Cursor end = position.advance(50);
    assertEquals(1, end.endpoint());
    assertEquals(100.0, position.distance());
    assertEquals(1.0f, position.progress());
    position.advance(-1);
    assertEquals(99.0, position.distance());
  }

  @Test
  void explicitLegacyCursorWriteRepositionsAuthoredTraversal() {
    final WorldMapRouteMetric metric = new WorldMapRouteMetric(List.of(point(0), point(100)));
    final WorldMapTraversalPosition position = new WorldMapTraversalPosition();
    position.set(PORTAL, metric, 77);
    position.synchronize(PORTAL, metric, 0, 2);
    assertEquals(50.0, position.distance());
  }

  @Test
  void partialActivationFailureCompensatesInReverseOrder() {
    final WorldMapActivation activation = new WorldMapActivation();
    final List<String> calls = new ArrayList<>();
    activation.add(() -> calls.add("first"), () -> calls.add("undo first"));
    activation.add(() -> { calls.add("second"); throw new IllegalStateException("partial"); }, () -> calls.add("undo second"));
    final IllegalStateException failure = assertThrows(IllegalStateException.class, activation::apply);
    activation.rollback(failure);
    activation.rollback(failure);
    assertEquals(List.of("first", "second", "undo second", "undo first"), calls);
  }

  @Test
  void abstainingHighPriorityDecisionUsesLowerPriorityAndConflictsAreExplicit() {
    final WorldMapProgression progression = new WorldMapProgression.Builder(new Flags(32), new Flags(1)).build();
    final WorldMapRules.Builder builder = new WorldMapRules.Builder();
    builder.departureDecision(new RegistryId("test", "low"), 1, (origin, facts) -> WorldMapTravel.Departure.FIRST_QUEEN_FURY);
    builder.departureDecision(new RegistryId("test", "high"), 2, (origin, facts) -> null);
    assertEquals(WorldMapTravel.Departure.FIRST_QUEEN_FURY, builder.build().departure(new SubmapEndpoint(2, 0), progression));
    builder.departureDecision(new RegistryId("test", "conflict"), 1, (origin, facts) -> WorldMapTravel.Departure.LATER_QUEEN_FURY);
    assertThrows(IllegalStateException.class, () -> builder.build().departure(new SubmapEndpoint(2, 0), progression));
  }

  private static WorldMapPoint point(final float x) {
    return new WorldMapPoint(x, 0, 0);
  }

  private static WorldMapDefinition definition(final RegistryId... ids) {
    final List<WorldMapPortal> portals = new ArrayList<>();
    for(int i = 0; i < ids.length; i++) portals.add(new WorldMapPortal(ids[i], i, null, null, new SubmapEndpoint(2, 0), new SubmapEndpoint(2, 0), 0, Continent.NONE_8, false, 0));
    return new WorldMapDefinition(portals, List.of(), List.of(), List.of(), List.of());
  }
}
