package legend.core.memory;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class Ref<T> {
  @Nullable
  private T ref;
  private boolean set;

  public Ref() { }

  public Ref(final T ref) {
    this.set(ref);
  }

  public T get() {
    if(!this.isPresent()) {
      throw new NullPointerException("Ref must be set before accessing");
    }

    return this.ref;
  }

  public T orElse(final Supplier<T> supplier) {
    if(!this.isPresent()) {
      return supplier.get();
    }

    return this.get();
  }

  public void set(final T ref) {
    this.ref = ref;
    this.set = true;
  }

  public void clear() {
    this.ref = null;
    this.set = false;
  }

  public boolean isPresent() {
    return this.set;
  }

  @Override
  public String toString() {
    return "Ref{" + (this.isPresent() ? this.get() : "empty") + '}';
  }
}
