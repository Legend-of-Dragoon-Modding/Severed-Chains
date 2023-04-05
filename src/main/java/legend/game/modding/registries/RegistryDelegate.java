package legend.game.modding.registries;

import java.util.function.Supplier;

public class RegistryDelegate<Type extends RegistryEntry> implements Supplier<Type> {
  private final Supplier<Type> supplier;
  private Type value;
  private boolean resolved;

  RegistryDelegate(final Supplier<Type> supplier) {
    this.supplier = supplier;
  }

  public void clear() {
    this.value = null;
    this.resolved = false;
  }

  @Override
  public Type get() {
    if(!this.resolved) {
      this.value = this.supplier.get();
      this.resolved = true;
    }

    return this.value;
  }
}
