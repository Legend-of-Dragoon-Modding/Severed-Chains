package legend.game.wmap;

import legend.game.types.LodString;
import org.joml.Vector3f;

public class CoolonWarpDestination20 {
  /** 12-bit fixed-point */
  public final Vector3f vec_00;
  public final int locationIndex_10;
  public final int _14;
  public final int x_18;
  public final int y_1a;
  public final LodString placeName_1c;

  public CoolonWarpDestination20(final Vector3f vec, final int locationIndex, final int _14, final int x, final int y, final LodString placeName) {
    this.vec_00 = vec;
    this.locationIndex_10 = locationIndex;
    this._14 = _14;
    this.x_18 = x;
    this.y_1a = y;
    this.placeName_1c = placeName;
  }
}
