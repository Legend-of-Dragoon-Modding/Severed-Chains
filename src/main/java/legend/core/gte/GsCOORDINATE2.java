package legend.core.gte;

public class GsCOORDINATE2 {
  public int flg;
  public final MV coord = new MV();
  public final MV workm = new MV();
  public Transforms transforms = new Transforms();
  public GsCOORDINATE2 super_;
  public GsCOORDINATE2 sub;

  public GsCOORDINATE2 set(final GsCOORDINATE2 other) {
    this.flg = other.flg;
    this.coord.set(other.coord);
    this.workm.set(other.workm);
    this.transforms = other.transforms;
    this.super_ = other.super_;
    this.sub = other.sub;
    return this;
  }
}
