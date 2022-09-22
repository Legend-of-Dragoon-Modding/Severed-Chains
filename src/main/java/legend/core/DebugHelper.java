package legend.core;

public final class DebugHelper {
  private DebugHelper() { }

  public static void sleep(final long millis) {
    try {
      Thread.sleep(millis);
    } catch(final InterruptedException ignored) { }
  }

  public static void pause() {
    final boolean running = true;
    while(running) {
      sleep(100);
    }
  }

  public static Timer timer(final long interval) {
    return new Timer(interval);
  }

  public static final class Timer {
    private final long timeout;

    private Timer(final long interval) {
      this.timeout = System.nanoTime() + interval;
    }

    public void check(final Runnable runnable) {
      if(System.nanoTime() >= this.timeout) {
        runnable.run();
        throw new RuntimeException("Timeout");
      }
    }
  }
}
