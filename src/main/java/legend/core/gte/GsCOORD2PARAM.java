package legend.core.gte;

public class GsCOORD2PARAM {
  public final VECTOR scale = new VECTOR();
  public final SVECTOR rotate = new SVECTOR();
  public final VECTOR trans = new VECTOR();

  public GsCOORD2PARAM set(final GsCOORD2PARAM other) {
    this.scale.set(other.scale);
    this.rotate.set(other.rotate);
    this.trans.set(other.trans);
    return this;
  }
}
