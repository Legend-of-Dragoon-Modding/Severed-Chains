package legend.core.gte;

import legend.game.tmd.TmdObjTable1c;

public class ModelPart10 {
  /** perspective, translation, rotate, display */
  public int attribute_00;
  /** local dmatrix */
  public GsCOORDINATE2 coord2_04;
  public TmdObjTable1c tmd_08;

  public ModelPart10 set(final ModelPart10 other) {
    this.attribute_00 = other.attribute_00;
    this.coord2_04 = other.coord2_04;
    this.tmd_08 = other.tmd_08;
    return this;
  }

  public void delete() {
    this.tmd_08.delete();
  }
}
