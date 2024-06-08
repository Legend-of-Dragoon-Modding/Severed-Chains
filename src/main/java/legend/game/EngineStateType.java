package legend.game;

import org.legendofdragoon.modloader.registries.RegistryEntry;

import java.util.function.Supplier;

public class EngineStateType<T extends EngineState<T>> extends RegistryEntry {
  public final Class<T> class_00;
  public final Supplier<T> constructor_00;

  public EngineStateType(final Class<T> cls, final Supplier<T> constructor) {
    this.class_00 = cls;
    this.constructor_00 = constructor;
  }
}
