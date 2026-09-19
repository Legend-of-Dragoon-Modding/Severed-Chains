package legend.game;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.LongSupplier;

/** Engine-thread ownership for subscriptions, prepared CPU results and resource cleanup. */
public final class EngineStateLifetime implements AutoCloseable {
  private final String name;
  private final LongSupplier clock;
  private final List<AutoCloseable> resources = new ArrayList<>();
  private final List<Pending<?>> pending = new ArrayList<>();
  private boolean closed;

  public EngineStateLifetime(final String name) {
    this(name, System::nanoTime);
  }

  EngineStateLifetime(final String name, final LongSupplier clock) {
    this.name = Objects.requireNonNull(name);
    this.clock = Objects.requireNonNull(clock);
  }

  public <T extends AutoCloseable> T own(final T resource) {
    if(this.closed) throw new IllegalStateException(this.name + " lifetime is closed");
    this.resources.add(Objects.requireNonNull(resource));
    return resource;
  }

  public EngineStateLifetime child(final String label) {
    return this.own(new EngineStateLifetime(this.name + "/" + label, this.clock));
  }

  public <T> void await(final String label, final CompletableFuture<T> future, final Consumer<T> adopt) {
    this.await(label, future, Duration.ofSeconds(60), adopt, value -> { });
  }

  /** discard must be thread-safe: late CPU results can arrive after the engine thread has exited. */
  public <T> void await(final String label, final CompletableFuture<T> future, final Duration timeout,
                        final Consumer<T> adopt, final Consumer<T> discard) {
    if(this.closed) throw new IllegalStateException(this.name + " lifetime is closed");
    if(timeout.isNegative() || timeout.isZero()) throw new IllegalArgumentException("Timeout must be positive");
    Objects.requireNonNull(future, "future");
    final Pending<T> task = new Pending<>(this.name + "/" + label,
      this.clock.getAsLong(), timeout.toNanos(), Objects.requireNonNull(adopt), Objects.requireNonNull(discard));
    this.pending.add(task);
    future.whenComplete((value, failure) -> {
      try {
        task.completed(value, failure);
      } catch(final RuntimeException | Error cleanup) {
        System.getLogger(EngineStateLifetime.class.getName()).log(System.Logger.Level.ERROR, "Discarding late result " + this.name + "/" + label, cleanup);
      }
    });
  }

  public int pendingCount() {
    return this.pending.size();
  }

  public boolean isClosed() {
    return this.closed;
  }

  /** Called only by the owning engine thread. No completion callback mutates engine state. */
  public void poll() {
    if(this.closed) return;
    for(final Pending<?> task : List.copyOf(this.pending)) {
      if(task.poll(this.clock.getAsLong())) this.pending.remove(task);
    }
  }

  @Override
  public void close() {
    if(this.closed) return;
    this.closed = true;
    Throwable failure = null;
    for(final Pending<?> task : this.pending) {
      try {
        task.abandon();
      } catch(final RuntimeException | Error problem) {
        failure = mergeFailure(failure, problem);
      }
    }
    this.pending.clear();
    for(int i = this.resources.size() - 1; i >= 0; i--) {
      try {
        this.resources.get(i).close();
      } catch(final Exception | Error problem) {
        failure = mergeFailure(failure, problem);
      }
    }
    this.resources.clear();
    if(failure instanceof Error fatal) throw fatal;
    if(failure != null) throw new IllegalStateException("Closing " + this.name, failure);
  }

  private static Throwable mergeFailure(final Throwable previous, final Throwable problem) {
    if(previous == null) return problem;
    if(previous == problem) return previous;
    if(problem instanceof Error && !(previous instanceof Error)) {
      problem.addSuppressed(previous);
      return problem;
    }
    previous.addSuppressed(problem);
    return previous;
  }

  private static final class Pending<T> {
    private final String label;
    private final long started;
    private final long timeout;
    private Consumer<T> adopt;
    private final Consumer<T> discard;
    private boolean complete;
    private boolean abandoned;
    private boolean consumed;
    private T value;
    private Throwable failure;

    private Pending(final String label, final long started, final long timeout,
                    final Consumer<T> adopt, final Consumer<T> discard) {
      this.label = label;
      this.started = started;
      this.timeout = timeout;
      this.adopt = adopt;
      this.discard = discard;
    }

    synchronized void completed(final T value, final Throwable failure) {
      this.value = value;
      this.failure = failure;
      this.complete = true;
      if(this.abandoned && failure == null && !this.consumed) {
        this.consumed = true;
        this.discard.accept(value);
        this.value = null;
      }
    }

    synchronized boolean poll(final long now) {
      if(this.abandoned || this.consumed) return true;
      if(!this.complete) {
        if(now - this.started < this.timeout) return false;
        this.abandoned = true;
        this.adopt = null;
        throw new IllegalStateException("Timed out loading " + this.label);
      }
      this.consumed = true;
      final Consumer<T> adoption = this.adopt;
      this.adopt = null;
      if(this.failure instanceof Error fatal) {
        fatal.addSuppressed(new IllegalStateException("Loading " + this.label));
        throw fatal;
      }
      if(this.failure != null) throw new IllegalStateException("Failed loading " + this.label, this.failure);
      try {
        adoption.accept(this.value);
      } catch(final RuntimeException | Error failure) {
        try {
          this.discard.accept(this.value);
        } catch(final RuntimeException | Error cleanup) {
          failure.addSuppressed(cleanup);
        }
        if(failure instanceof Error fatal) {
          fatal.addSuppressed(new IllegalStateException("Adopting " + this.label));
          throw fatal;
        }
        throw new IllegalStateException("Failed adopting " + this.label, failure);
      } finally {
        this.value = null;
      }
      return true;
    }

    synchronized void abandon() {
      this.abandoned = true;
      this.adopt = null;
      if(this.complete && this.failure == null && !this.consumed) {
        this.consumed = true;
        this.discard.accept(this.value);
      }
    }
  }
}
