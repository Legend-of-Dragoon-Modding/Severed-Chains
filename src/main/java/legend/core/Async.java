package legend.core;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public final class Async {
  private Async() { }

  private static final int AVAILABLE_PROCESSORS = Runtime.getRuntime().availableProcessors();
  private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(AVAILABLE_PROCESSORS);

  public static Future<?> run(final Runnable task) {
    return EXECUTOR.submit(task);
  }

  public static <T> Future<T> run(final Callable<T> task) {
    return EXECUTOR.submit(task);
  }

  public static void allLoaded(final AtomicInteger counter, final int totalCount, final Runnable action, final Runnable onCompletion) {
    action.run();

    if(counter.incrementAndGet() == totalCount) {
      onCompletion.run();
    }
  }

  public static void shutdown() {
    EXECUTOR.shutdown();
  }
}
