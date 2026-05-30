package legend.core;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.function.Supplier;

public class QueuePool<T> {
  private final List<T> queue = new ArrayList<>();
  private final Map<Class<? extends T>, QueueType<? extends T>> pools = new HashMap<>();
  boolean ignoreQueues;

  private T[] sortArray = (T[])new Object[100];
  private T[] workArray = (T[])new Object[this.sortArray.length];

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

  /** Optimized sort that doesn't allocate temporary objects */
  public void sort(final Comparator<? super T> comparator) {
    if(this.size() == 0) {
      return;
    }

    if(this.sortArray.length < this.size()) {
      this.sortArray = (T[])new Object[(int)(this.size() * 1.5f)];
      this.workArray = (T[])new Object[this.sortArray.length];
    }

    this.queue.toArray(this.sortArray);
    Sort.sort(this.sortArray, 0, this.size(), comparator, this.workArray, 0, this.workArray.length);

    final ListIterator<T> it = this.queue.listIterator();
    for(int i = 0; i < this.size(); i++) {
      it.next();
      it.set(this.sortArray[i]);
    }
  }
}
