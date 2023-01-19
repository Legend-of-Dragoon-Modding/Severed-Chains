package legend.core.gte;

/** 0x10 bytes long */
public class GsDOBJ2 {
  /** perspective, translation, rotate, display */
  public int attribute_00;
  /** local dmatrix */
  public GsCOORDINATE2 coord2_04;
  public TmdObjTable tmd_08;
  public int id_0c;

  public GsDOBJ2 set(final GsDOBJ2 other) {
    this.attribute_00 = other.attribute_00;
    this.coord2_04 = other.coord2_04;
    this.tmd_08 = other.tmd_08;
    this.id_0c = other.id_0c;
    return this;
  }
}
