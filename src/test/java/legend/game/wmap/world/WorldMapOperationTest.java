package legend.game.wmap.world;

import legend.game.types.GameState52c;
import org.junit.jupiter.api.Test;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WorldMapOperationTest {
  private static final WorldMapTravelTarget TARGET = new WorldMapTravelTarget.Portal(new RegistryId("test", "arrival"));

  @Test void activationCommitsOnlyAfterReadinessAndReleasesRecovery() {
    final var operation = new WorldMapTravelOperation<String>();
    final GameState52c state = new GameState52c();
    final List<String> calls = new ArrayList<>();
    final WorldMapActivation activation = new WorldMapActivation();
    activation.add(() -> calls.add("activate"), () -> calls.add("undo"));
    assertTrue(operation.prepare(true));
    operation.admit(TARGET, true, activation, "next", new WorldMapTravelOperation.Recovery<>("old", new WorldMapCampaignSnapshot(state), TARGET));
    assertFalse(operation.prepare(true));
    assertTrue(operation.begin());
    operation.loading();
    assertEquals("next", operation.takeCandidate());
    operation.activate();
    assertFalse(operation.activateWhenReady(false, () -> fail("not ready")));
    assertTrue(calls.isEmpty());
    assertTrue(operation.activateWhenReady(true, () -> calls.add("publish")));
    assertEquals(List.of("activate", "publish"), calls);
    assertEquals(WorldMapTransition.Phase.IDLE, operation.phase());
    assertFalse(operation.hasRecovery());
    operation.close();
    assertEquals(List.of("activate", "publish"), calls);
  }

  @Test void partialActivationRestoresCampaignAndAllowsOnlyOneRecovery() {
    final var operation = new WorldMapTravelOperation<String>();
    final GameState52c state = new GameState52c();
    final WorldMapActivation activation = new WorldMapActivation();
    activation.add(() -> { state.scriptFlags2_bc.set(7, true); throw new IllegalStateException("partial"); }, () -> state.scriptFlags2_bc.set(7, false));
    operation.prepare(true);
    operation.admit(TARGET, true, activation, "next", new WorldMapTravelOperation.Recovery<>("old", new WorldMapCampaignSnapshot(state), TARGET));
    operation.begin();
    operation.loading();
    operation.takeCandidate();
    operation.activate();
    final RuntimeException failure = assertThrows(RuntimeException.class, () -> operation.activateWhenReady(true, () -> fail("must not publish")));
    assertTrue(operation.recover(failure, () -> { }));
    assertFalse(state.scriptFlags2_bc.get(7));
    assertEquals("old", operation.takeCandidate());
    assertFalse(operation.recover(new IllegalStateException("recovery failed"), () -> fail("second recovery")));
  }

  @Test void cleanupFailureDoesNotPreventRestoration() {
    final var operation = new WorldMapTravelOperation<String>();
    final GameState52c state = new GameState52c();
    operation.prepare(true);
    operation.admit(TARGET, true, new WorldMapActivation(), null, new WorldMapTravelOperation.Recovery<>("old", new WorldMapCampaignSnapshot(state), TARGET));
    state.scriptFlags2_bc.set(2, true);
    final var failure = new IllegalStateException("load");
    assertFalse(operation.recover(failure, () -> { throw new IllegalStateException("cleanup"); }));
    assertFalse(state.scriptFlags2_bc.get(2));
    assertEquals(1, failure.getSuppressed().length);
  }
}
