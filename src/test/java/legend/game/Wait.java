package legend.game;

import legend.core.DebugHelper;
import legend.game.fmv.Fmv;

import java.util.function.BooleanSupplier;

import static legend.game.Scus94491BpeSegment_8004.engineState_8004dd20;

public final class Wait {
  private Wait() { }

  public static void waitFor(final BooleanSupplier condition) {
    while(!condition.getAsBoolean()) {
      DebugHelper.sleep(10);
    }
  }

  public static void waitForEngineState(final EngineStateEnum state) {
    waitFor(() -> engineState_8004dd20 == state);
  }

  public static void waitForFmvToStart() {
    waitFor(() -> Fmv.isPlaying);
  }

  public static void waitForFmvToStop() {
    waitFor(() -> !Fmv.isPlaying);
  }
}
