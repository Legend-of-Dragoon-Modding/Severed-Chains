package legend.game.types;

import legend.game.EngineState;

import java.util.function.Supplier;

public class OverlayStruct {
  public final Class<? extends EngineState> class_00;
  public final Supplier<? extends EngineState> constructor_00;

  public <T extends EngineState> OverlayStruct(final Class<T> cls, final Supplier<T> constructor) {
    this.class_00 = cls;
    this.constructor_00 = constructor;
  }
}
