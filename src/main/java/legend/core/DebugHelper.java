package legend.core;

import legend.game.DrgnFiles;
import legend.game.unpacker.Loader;

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

  public static StackWalker.StackFrame getCallerFrame() {
    return StackWalker
      .getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE)
      .walk(frames -> frames
        .filter(frame ->
          frame.getDeclaringClass() != DebugHelper.class &&
          frame.getDeclaringClass() != DrgnFiles.class &&
          frame.getDeclaringClass() != Loader.class
        )
        .findFirst()
      )
      .get();
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
