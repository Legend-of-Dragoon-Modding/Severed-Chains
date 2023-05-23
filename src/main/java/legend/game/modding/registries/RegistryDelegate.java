package legend.game.modding.registries;

import legend.core.Latch;
import legend.game.modding.ModNotLoadedException;

import java.util.function.Supplier;

import static legend.core.GameEngine.MODS;
import static legend.core.GameEngine.REGISTRIES;

public class RegistryDelegate<Type extends RegistryEntry> {
  private final RegistryId id;
  private final Class<Registry<Type>> cls;
  private final Latch<Type> latch;

  RegistryDelegate(final RegistryId id, final Class<Registry<Type>> cls, final Supplier<Type> supplier) {
    this.id = id;
    this.cls = cls;
    this.latch = new Latch<>(supplier);
  }

  public boolean isValid() {
    return REGISTRIES.config.hasEntry(this.id);
  }

  public void clear() {
    this.latch.clear();
  }

  public Type get() {
    if(!this.isValid()) {
      throw new ModNotLoadedException("Mod " + this.id.modId() + " is not loaded");
    }

    return this.latch.get();
  }

  @Override
  public boolean equals(final Object obj) {
    if(obj instanceof final RegistryDelegate<?> other) {
      return this.cls.equals(other.cls) && this.id.equals(other.id);
    }

    return false;
  }
}
