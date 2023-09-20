package legend.game.types;

import javax.annotation.Nullable;

public class OverlayStruct {
  public final Runnable callback_00;
  @Nullable
  public final String file_04;
  public final long addressToClear_08;
  public final int clearSize;

  public OverlayStruct(final Runnable callback, @Nullable final String file, final long addressToClear, final int clearSize) {
    this.callback_00 = callback;
    this.file_04 = file;
    this.addressToClear_08 = addressToClear;
    this.clearSize = clearSize;
  }

  public OverlayStruct(final Runnable callback) {
    this(callback, null, 0, 0);
  }
}
