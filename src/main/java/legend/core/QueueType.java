package legend.core;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class QueueType<T> {
  private final List<T> cache = new ArrayList<>();
  private final Supplier<T> constructor;
  private int index;

  public QueueType(final Supplier<T> constructor) {
    this.constructor = constructor;
  }

  public T acquire() {
    if(this.index >= this.cache.size()) {
      final T entry = this.constructor.get();
      this.cache.add(entry);
      this.index++;
      return entry;
    }

    return this.cache.get(this.index++);
  }

  public void reset() {
    this.index = 0;
  }
}
