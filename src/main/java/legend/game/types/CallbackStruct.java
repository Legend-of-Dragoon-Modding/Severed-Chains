package legend.game.types;

public class CallbackStruct {
  public final Runnable callback_00;
  public final FileEntry08 entry_04;
  public final long addressToClear_08;
  public final int clearSize;

  public CallbackStruct(final Runnable callback, final FileEntry08 entry, final long addressToClear, final int clearSize) {
    this.callback_00 = callback;
    this.entry_04 = entry;
    this.addressToClear_08 = addressToClear;
    this.clearSize = clearSize;
  }
}
