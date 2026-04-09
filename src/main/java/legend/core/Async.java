package legend.core;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public final class Async {
  private Async() { }

  private static final int AVAILABLE_PROCESSORS = Runtime.getRuntime().availableProcessors();
  private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(AVAILABLE_PROCESSORS);

  public static CompletableFuture<Void> run(final Runnable task) {
    return CompletableFuture.runAsync(task, EXECUTOR);
  }

  public static <T> CompletableFuture<T> run(final Supplier<T> task) {
    return CompletableFuture.supplyAsync(task, EXECUTOR);
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
