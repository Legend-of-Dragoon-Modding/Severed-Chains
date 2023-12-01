package legend.game.wmap;

import org.joml.Vector3f;

public class CoolonWarpDestination20 {
  /** 12-bit fixed-point */
  public final Vector3f destPosition_00;
  public final int locationIndex_10;
  public final int defaultDestLocationIndex_14;
  public final int x_18;
  public final int y_1a;
  public final String placeName_1c;

  public CoolonWarpDestination20(final Vector3f vec, final int locationIndex, final int defaultDestLocationIndex, final int x, final int y, final String placeName) {
    this.destPosition_00 = vec;
    this.locationIndex_10 = locationIndex;
    this.defaultDestLocationIndex_14 = defaultDestLocationIndex;
    this.x_18 = x;
    this.y_1a = y;
    this.placeName_1c = placeName;
  }
}
