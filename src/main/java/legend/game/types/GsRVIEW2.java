package legend.game.types;

import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.VECTOR;

/** 0x20 long */
public class GsRVIEW2 {
  public final VECTOR viewpoint_00 = new VECTOR();
  public final VECTOR refpoint_0c = new VECTOR();
  public int viewpointTwist_18;
  public GsCOORDINATE2 super_1c;

  public GsRVIEW2 set(final GsRVIEW2 other) {
    this.viewpoint_00.set(other.viewpoint_00);
    this.refpoint_0c.set(other.refpoint_0c);
    this.viewpointTwist_18 = other.viewpointTwist_18;
    this.super_1c = other.super_1c;
    return this;
  }
}
