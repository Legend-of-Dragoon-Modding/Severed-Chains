package legend.game;

import legend.core.DebugHelper;
import legend.core.GameEngine;
import legend.core.platform.input.InputKey;

public final class Bootstrapper {
  private Bootstrapper() { }

  public static Thread loadEngine() {
    // Init engine
    final Thread engine = new Thread(() -> Main.main(new String[0]));
    engine.start();
    Wait.waitFor(() -> !GameEngine.isLoading());

    DebugHelper.sleep(1000);

    // Skip intro
    Input.sendKeyPress(InputKey.RETURN);
    return engine;
  }
}
