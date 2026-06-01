package legend.game;

import legend.core.GameEngine;

import static legend.core.GameEngine.PLATFORM;

public final class Bootstrapper {
  private Bootstrapper() { }

  /**
   * Start the engine on a background thread
   */
  public static Thread loadEngine() {
    final Thread engine = new Thread(() -> Main.main(new String[0]));
    engine.start();
    Wait.waitFor(() -> !GameEngine.isLoading(), 60_000, "engine loading");
    Wait.waitFor(() -> PLATFORM.getLastWindow() != null, 30_000, "SDL window created");
    return engine;
  }
}
