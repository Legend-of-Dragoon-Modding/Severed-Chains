package legend.game.wmap.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/** Poll CPU futures on the owning WMap thread; discarded generations never adopt resources. */
public final class WorldMapResourceLoader {
  private final List<Pending<?>> pending = new ArrayList<>();

  private record Pending<T>(CompletableFuture<T> future, Consumer<T> adopt) {
    void finish() {
      this.adopt.accept(this.future.join());
    }
  }

  public <T> void load(final String label, final CompletableFuture<T> future, final Consumer<T> adopt) {
    Objects.requireNonNull(adopt, "adopt");
    final CompletableFuture<T> bounded = Objects.requireNonNull(future, "future for " + label).thenApply(value -> value).orTimeout(60, TimeUnit.SECONDS);
    this.pending.add(new Pending<>(bounded.handle((value, failure) -> {
      if(failure != null) throw new CompletionException("World map resource failed: " + label, failure);
      return value;
    }), adopt));
  }

  public void tick() {
    final List<Pending<?>> ready = this.pending.stream().filter(item -> item.future.isDone()).toList();
    this.pending.removeAll(ready);
    ready.forEach(Pending::finish);
  }

  public void clear() {
    // Providers may share futures. Drop this generation without cancelling another consumer's load.
    this.pending.clear();
  }
}
