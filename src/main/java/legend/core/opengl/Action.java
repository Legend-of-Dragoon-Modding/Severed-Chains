package legend.core.opengl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Action {
  private static final Logger LOGGER = LogManager.getFormatterLogger(Action.class);

  private final Runnable action;
  private int expectedFps;
  private int nanosPerTick;
  private long nextRunTime;

  public Action(final Runnable action, final int expectedFps) {
    this.action = action;
    this.setExpectedFps(expectedFps);
    this.nextRunTime = System.nanoTime();
    this.updateTimer();
  }

  public void setExpectedFps(final int expectedFps) {
    this.expectedFps = expectedFps;
    this.nanosPerTick = 1_000_000_000 / this.expectedFps;
  }

  public int getExpectedFps() {
    return this.expectedFps;
  }

  public void tick() {
    if(this.isReady()) {
      this.run();
    }
  }

  public long nanosUntilNextRun() {
    return this.nextRunTime - System.nanoTime();
  }

  public boolean isReady() {
    return this.nanosUntilNextRun() <= 0;
  }

  private void run() {
    this.updateTimer();
    this.action.run();
  }

  private void updateTimer() {
    this.nextRunTime += this.nanosPerTick;

    if(-(this.nextRunTime - System.nanoTime()) > this.nanosPerTick * 2) {
      LOGGER.debug("Action running behind, skipping ticks to catch up");
      this.nextRunTime = System.nanoTime() + this.nanosPerTick;
    }
  }
}
