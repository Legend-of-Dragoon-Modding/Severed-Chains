package legend.game.types;

import legend.game.EngineState;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class OverlayStruct {
  public final Class<? extends EngineState> class_00;
  public final Supplier<? extends EngineState> constructor_00;
  @Nullable
  public final String file_04;
  public final long addressToClear_08;
  public final int clearSize;

  public <T extends EngineState> OverlayStruct(final Class<T> cls, final Supplier<T> constructor, @Nullable final String file, final long addressToClear, final int clearSize) {
    this.class_00 = cls;
    this.constructor_00 = constructor;
    this.file_04 = file;
    this.addressToClear_08 = addressToClear;
    this.clearSize = clearSize;
  }

  public <T extends EngineState> OverlayStruct(final Class<T> cls, final Supplier<T> constructor) {
    this(cls, constructor, null, 0, 0);
  }
}
