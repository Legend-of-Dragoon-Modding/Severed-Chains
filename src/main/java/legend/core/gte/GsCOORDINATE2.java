package legend.core.gte;

public class GsCOORDINATE2 {
  public int flg;
  public final MATRIX coord = new MATRIX();
  public final MATRIX workm = new MATRIX();
  public GsCOORD2PARAM param;
  public GsCOORDINATE2 super_;
  public GsCOORDINATE2 sub;

  public GsCOORDINATE2 set(final GsCOORDINATE2 other) {
    this.flg = other.flg;
    this.coord.set(other.coord);
    this.workm.set(other.workm);
    this.param = other.param;
    this.super_ = other.super_;
    this.sub = other.sub;
    return this;
  }
}
