package legend.game.submap;

import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.ModelPart10;
import legend.core.gte.TmdObjTable1c;
import legend.core.gte.TmdWithId;
import org.joml.Vector3f;

public class SomethingStruct {
  public TmdObjTable1c[] objTableArrPtr_00;
  public Vector3f[] verts_04;
  public Vector3f[] normals_08;
  public int count_0c;
  public TmdObjTable1c.Primitive[] primitives_10;
  public SomethingStructSub0c_1[] ptr_14;
  public SomethingStructSub0c_2[] ptr_18;
  public TmdWithId tmdPtr_1c;
  public ModelPart10 dobj2Ptr_20;
  public GsCOORDINATE2 coord2Ptr_24;

  public TmdObjTable1c.Primitive getPrimitiveForOffset(final int offset) {
    int primitivesIndex;
    for(primitivesIndex = 0; primitivesIndex < this.primitives_10.length - 1; primitivesIndex++) {
      if(this.primitives_10[primitivesIndex + 1].offset() > offset) {
        break;
      }
    }

    return this.primitives_10[primitivesIndex];
  }
}
