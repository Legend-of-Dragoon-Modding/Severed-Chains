package legend.game;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;

class EngineStateLifetimeTest {
  @Test
  void explicitReturnOverridesStateDefaultIncludingNull() {
    final EngineDestination destination = new EngineDestination(new org.legendofdragoon.modloader.registries.RegistryId("test", "submap"), new legend.core.tags.MapTag());
    final EngineDestination defaultReturn = new EngineDestination(new org.legendofdragoon.modloader.registries.RegistryId("test", "world"), new legend.core.tags.MapTag());
    final EngineDestination alternateReturn = new EngineDestination(new org.legendofdragoon.modloader.registries.RegistryId("test", "alternate"), new legend.core.tags.MapTag());
    final EngineTransition fallback = new EngineTransition(destination, defaultReturn);
    assertSame(alternateReturn, fallback.withRequestedReturn(new EngineTransition(destination, alternateReturn)).returnTo());
    assertNull(fallback.withRequestedReturn(new EngineTransition(destination, null)).returnTo());
    assertSame(defaultReturn, fallback.withRequestedReturn(null).returnTo());
    assertSame(defaultReturn, fallback.withRequestedReturn(new EngineTransition(alternateReturn, null)).returnTo());
  }

  @Test
  void completionWaitsForOwnerPoll() {
    final EngineStateLifetime lifetime = new EngineStateLifetime("submap");
    final CompletableFuture<String> load = new CompletableFuture<>();
    final List<String> adopted = new ArrayList<>();
    lifetime.await("environment", load, adopted::add);
    load.complete("map");
    assertTrue(adopted.isEmpty());
    lifetime.poll();
    lifetime.poll();
    assertEquals(List.of("map"), adopted);
    assertEquals(0, lifetime.pendingCount());
  }

  @Test
  void closedGenerationDiscardsLateCompletionWithoutCancellingSharedFuture() {
    final EngineStateLifetime lifetime = new EngineStateLifetime("old-map");
    final CompletableFuture<String> load = new CompletableFuture<>();
    final List<String> discarded = new ArrayList<>();
    lifetime.await("assets", load, Duration.ofSeconds(60), ignored -> fail("stale adoption"), discarded::add);
    lifetime.close();
    assertFalse(load.isCancelled());
    load.complete("late assets");
    lifetime.poll();
    lifetime.close();
    assertEquals(List.of("late assets"), discarded);
  }

  @Test
  void closeDiscardsCompletedButUnadoptedResult() {
    final EngineStateLifetime lifetime = new EngineStateLifetime("old-map");
    final List<String> discarded = new ArrayList<>();
    lifetime.await("assets", CompletableFuture.completedFuture("assets"), Duration.ofSeconds(60), ignored -> fail("stale adoption"), discarded::add);
    lifetime.close();
    assertEquals(List.of("assets"), discarded);
  }

  @Test
  void timeoutHasContextAndDiscardsResultThatArrivesLater() {
    final AtomicLong clock = new AtomicLong();
    final EngineStateLifetime lifetime = new EngineStateLifetime("custom-submap", clock::get);
    final CompletableFuture<String> load = new CompletableFuture<>();
    final List<String> discarded = new ArrayList<>();
    lifetime.await("environment", load, Duration.ofNanos(10), ignored -> fail("timed-out adoption"), discarded::add);
    clock.set(9);
    lifetime.poll();
    clock.set(10);
    assertTrue(assertThrows(IllegalStateException.class, lifetime::poll).getMessage().contains("custom-submap/environment"));
    load.complete("late");
    lifetime.close();
    assertEquals(List.of("late"), discarded);
  }

  @Test
  void loadFailureSurfacesInsteadOfAdvancingLoadingStage() {
    final EngineStateLifetime lifetime = new EngineStateLifetime("map");
    final IllegalArgumentException cause = new IllegalArgumentException("bad assets");
    lifetime.await("assets", CompletableFuture.failedFuture(cause), ignored -> fail("failed adoption"));
    final IllegalStateException failure = assertThrows(IllegalStateException.class, lifetime::poll);
    assertSame(cause, failure.getCause());
    assertTrue(failure.getMessage().contains("map/assets"));
    lifetime.close();
  }

  @Test
  void reverseCleanupContinuesAfterFatalFailureAndClosesChildren() {
    final EngineStateLifetime lifetime = new EngineStateLifetime("engine");
    final List<Integer> order = new ArrayList<>();
    lifetime.own(() -> order.add(1));
    final EngineStateLifetime child = lifetime.child("map");
    child.own(() -> order.add(2));
    final AssertionError fatal = new AssertionError("cleanup failed");
    lifetime.own(() -> { order.add(3); throw fatal; });
    assertSame(fatal, assertThrows(AssertionError.class, lifetime::close));
    assertEquals(List.of(3, 2, 1), order);
    assertTrue(child.isClosed());
    lifetime.close();
    assertEquals(List.of(3, 2, 1), order);
  }
}
