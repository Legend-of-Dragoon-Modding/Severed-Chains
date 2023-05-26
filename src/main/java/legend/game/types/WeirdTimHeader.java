package legend.game.types;

import legend.core.gpu.RECT;

public class WeirdTimHeader {
  public int flags;
  public final RECT clutRect = new RECT();
  public long clutAddress;
  public final RECT imageRect = new RECT();
  public long imageAddress;
}
