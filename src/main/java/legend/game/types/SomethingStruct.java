package legend.game.types;

import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.GsDOBJ2;
import legend.core.gte.SVECTOR;
import legend.core.gte.TmdObjTable;
import legend.core.gte.TmdWithId;
import legend.core.memory.types.UnboundedArrayRef;

public class SomethingStruct {
  public UnboundedArrayRef<TmdObjTable> objTableArrPtr_00;
  public UnboundedArrayRef<SVECTOR> verts_04;
  public long normals_08;
  public int count_0c;
  public long primitives_10;
  /** 0xc bytes each */
  public UnboundedArrayRef<SomethingStructSub0c_1> ptr_14;
  public UnboundedArrayRef<SomethingStructSub0c_2> ptr_18;
  public TmdWithId tmdPtr_1c;
  public GsDOBJ2 dobj2Ptr_20;
  public GsCOORDINATE2 coord2Ptr_24;
}
