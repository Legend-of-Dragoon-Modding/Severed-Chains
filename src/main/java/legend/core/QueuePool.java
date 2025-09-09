package legend.core;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class QueuePool<T> {
  private final List<T> queue = new ArrayList<>();
  private final Map<Class<? extends T>, QueueType<? extends T>> pools = new HashMap<>();
  boolean ignoreQueues;

  public <U extends T> void addType(final Class<U> cls, final Supplier<U> constructor) {
    this.pools.put(cls, new QueueType<>(constructor));
  }

  public T get(final int index) {
    return this.queue.get(index);
  }

  public int size() {
    return this.queue.size();
  }

  public boolean isEmpty() {
    return this.size() == 0;
  }

  public <U extends T> U acquire(final Class<U> cls) {
    @SuppressWarnings("unchecked")
    final U entry = (U)this.pools.get(cls).acquire();

    if(!this.ignoreQueues) {
      this.queue.add(entry);
    }

    return entry;
  }

  public void reset() {
    this.queue.clear();

    for(final var entry : this.pools.values()) {
      entry.reset();
    }
  }

  public void sort(final Comparator<? super T> comparator) {
    this.queue.subList(0, this.size()).sort(comparator);
  }
}
