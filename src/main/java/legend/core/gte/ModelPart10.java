package legend.core.gte;

import legend.core.opengl.MeshObj;

public class ModelPart10 {
  /** perspective, translation, rotate, display */
  public int attribute_00;
  /** local dmatrix */
  public GsCOORDINATE2 coord2_04;
  public TmdObjTable1c tmd_08;
  public MeshObj obj;

  public ModelPart10 set(final ModelPart10 other) {
    this.attribute_00 = other.attribute_00;
    this.coord2_04 = other.coord2_04;
    this.tmd_08 = other.tmd_08;
    this.obj = other.obj;
    return this;
  }

  public void delete() {
    if(this.obj != null) {
      this.obj.delete();
    }
  }
}
