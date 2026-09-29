package legend.game;

import legend.core.GameEngine;

import static legend.core.GameEngine.RENDERER;

public final class Bootstrapper {
  private Bootstrapper() { }

  /**
   * Start the engine on a background thread
   */
  public static Thread loadEngine() {
    final java.util.concurrent.CompletableFuture<Void> initialized = new java.util.concurrent.CompletableFuture<>();
    final Thread engine = new Thread(() -> {
      try {
        Class.forName("legend.core.GameEngine");
        initialized.complete(null);
        Main.main(new String[0]);
      } catch(final Throwable failure) {
        initialized.completeExceptionally(failure);
        throw new AssertionError("Engine startup failed", failure);
      }
    });
    engine.start();
    try {
      initialized.get(60, java.util.concurrent.TimeUnit.SECONDS);
    } catch(final Exception failure) {
      throw new AssertionError("Engine initialization failed", failure);
    }
    Wait.waitFor(() -> !GameEngine.isLoading(), 60_000, "engine loading");
    Wait.waitFor(() -> RENDERER.window() != null, 30_000, "SDL window created");
    Input.focusGameWindow();
    return engine;
  }
}
