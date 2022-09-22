package legend.core.gte;

import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.TmdObjTable;
import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.UnsignedIntRef;

/** 0x10 bytes long */
public class GsDOBJ2 implements MemoryRef {
  private final Value ref;

  /** perspective, translation, rotate, display */
  public final UnsignedIntRef attribute_00;
  /** local dmatrix */
  public final Pointer<legend.core.gte.GsCOORDINATE2> coord2_04;
  public final Pointer<legend.core.gte.TmdObjTable> tmd_08;
  public final UnsignedIntRef id_0c;

  public GsDOBJ2(final Value ref) {
    this.ref = ref;

    this.attribute_00 = ref.offset(4, 0x0L).cast(UnsignedIntRef::new);
    this.coord2_04 = ref.offset(4, 0x4L).cast(Pointer.deferred(4, GsCOORDINATE2::new));
    this.tmd_08 = ref.offset(4, 0x8L).cast(Pointer.deferred(4, TmdObjTable::new));
    this.id_0c = ref.offset(4, 0xcL).cast(UnsignedIntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
