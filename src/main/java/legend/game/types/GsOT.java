package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedIntRef;

public class GsOT implements MemoryRef {
  private final Value ref;

  /** Bit length of ordering table (1 << n) */
  public final UnsignedIntRef length_00;
  /** Pointer to start address of GsOT_TAG table */
  public final Pointer<UnboundedArrayRef<GsOT_TAG>> org_04;
  /** OT screen coordinate system Z-axis offset */
  public final UnsignedIntRef offset_08;
  /** OT screen coordinate system Z-axis typical value */
  public final UnsignedIntRef point_0c;
  /** Pointer to current GsOT_TAG element */
  public final Pointer<GsOT_TAG> tag_10;

  public GsOT(final Value ref) {
    this.ref = ref;

    this.length_00 = ref.offset(4, 0x00L).cast(UnsignedIntRef::new);
    this.org_04 = ref.offset(4, 0x04L).cast(Pointer.deferred(4, UnboundedArrayRef.of(4, GsOT_TAG::new, () -> (int)this.length_00.get())));
    this.offset_08 = ref.offset(4, 0x08L).cast(UnsignedIntRef::new);
    this.point_0c = ref.offset(4, 0x0cL).cast(UnsignedIntRef::new);
    this.tag_10 = ref.offset(4, 0x10L).cast(Pointer.deferred(4, GsOT_TAG::new));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
