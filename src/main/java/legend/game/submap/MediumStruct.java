package legend.game.submap;

import java.util.Arrays;
import java.util.function.Consumer;

public class MediumStruct {
  public final int[] arr_00 = new int[16];
  public int count_40;
  public boolean _44;
  public Consumer<MediumStruct> callback_48;

  public void clear() {
    Arrays.fill(this.arr_00, 0);
    this.count_40 = 0;
    this._44 = false;
    this.callback_48 = null;
  }
}
