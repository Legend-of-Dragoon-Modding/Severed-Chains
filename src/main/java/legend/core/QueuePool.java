package legend.core;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;

class QueuePool<T> {
  private final List<T> queue = new ArrayList<>();
  private final Supplier<T> constructor;
  private int index;

  public QueuePool(final Supplier<T> constructor) {
    this.constructor = constructor;
  }

  public T get(final int index) {
    return this.queue.get(index);
  }

  public int size() {
    return this.index;
  }

  public boolean isEmpty() {
    return this.size() == 0;
  }

  public T acquire() {
    if(this.index >= this.queue.size()) {
      final T entry = this.constructor.get();
      this.queue.add(entry);
      this.index++;
      return entry;
    }

    return this.queue.get(this.index++);
  }

  public void reset() {
    this.index = 0;
  }

  public void sort(final Comparator<? super T> comparator) {
    this.queue.subList(0, this.size()).sort(comparator);
  }
}
