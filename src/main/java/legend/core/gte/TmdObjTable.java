package legend.core.gte;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedIntRef;

/** 0x1c bytes long */
public class TmdObjTable implements MemoryRef {
  private final Value ref;

  public final Pointer<UnboundedArrayRef<SVECTOR>> vert_top_00;
  public final UnsignedIntRef n_vert_04;
  public final UnsignedIntRef normal_top_08;
  public final UnsignedIntRef n_normal_0c;
  public final Pointer<UnboundedArrayRef<UnsignedIntRef>> primitives_10;
  public final UnsignedIntRef n_primitive_14;
  public final UnsignedIntRef scale_18;

  public TmdObjTable(final Value ref) {
    this.ref = ref;

    this.vert_top_00 = ref.offset(4, 0x00L).cast(Pointer.deferred(4, UnboundedArrayRef.of(8, SVECTOR::new)));
    this.n_vert_04 = ref.offset(4, 0x04L).cast(UnsignedIntRef::new);
    this.normal_top_08 = ref.offset(4, 0x08L).cast(UnsignedIntRef::new);
    this.n_normal_0c = ref.offset(4, 0x0cL).cast(UnsignedIntRef::new);
    this.primitives_10 = ref.offset(4, 0x10L).cast(Pointer.deferred(4, UnboundedArrayRef.of(4, UnsignedIntRef::new)));
    this.n_primitive_14 = ref.offset(4, 0x14L).cast(UnsignedIntRef::new);
    this.scale_18 = ref.offset(4, 0x18L).cast(UnsignedIntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
