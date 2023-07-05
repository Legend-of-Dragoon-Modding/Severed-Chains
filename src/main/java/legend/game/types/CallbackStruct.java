package legend.game.types;

public class CallbackStruct {
  public final Runnable callback_00;
  public final String file_04;
  public final long addressToClear_08;
  public final int clearSize;

  public CallbackStruct(final Runnable callback, final String file, final long addressToClear, final int clearSize) {
    this.callback_00 = callback;
    this.file_04 = file;
    this.addressToClear_08 = addressToClear;
    this.clearSize = clearSize;
  }
}
