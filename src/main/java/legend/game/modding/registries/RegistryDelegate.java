package legend.game.modding.registries;

import legend.core.Latch;

import java.util.function.Supplier;

public class RegistryDelegate<Type extends RegistryEntry> {
  private final Latch<Type> latch;

  RegistryDelegate(final Supplier<Type> supplier) {
    this.latch = new Latch<>(supplier);
  }

  public void clear() {
    this.latch.clear();
  }

  public Type get() {
    return this.latch.get();
  }
}
