package legend.game.combat.types;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class Ptr<T> {
  private final Supplier<T> get;
  private final Consumer<T> set;

  public Ptr(final Supplier<T> get, final Consumer<T> set) {
    this.get = get;
    this.set = set;
  }

  public T get() {
    return this.get.get();
  }

  public void set(final T val) {
    this.set.accept(val);
  }
}
