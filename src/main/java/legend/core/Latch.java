package legend.core;

import java.util.function.Supplier;

public class Latch<T> implements Supplier<T> {
  private final Supplier<T> supplier;
  private T value;
  private boolean resolved;

  public Latch(final Supplier<T> supplier) {
    this.supplier = supplier;
  }

  public void clear() {
    this.value = null;
    this.resolved = false;
  }

  @Override
  public T get() {
    if(!this.resolved) {
      this.value = this.supplier.get();
      this.resolved = true;
    }

    return this.value;
  }
}
