package legend.core;

import java.util.concurrent.atomic.AtomicInteger;

public final class Async {
  private Async() { }

  public static void allLoaded(final AtomicInteger counter, final int totalCount, final Runnable action, final Runnable onCompletion) {
    action.run();

    if(counter.incrementAndGet() == totalCount) {
      onCompletion.run();
    }

    System.out.printf("Load %d/%d%n", counter.get(), totalCount);
  }
}
