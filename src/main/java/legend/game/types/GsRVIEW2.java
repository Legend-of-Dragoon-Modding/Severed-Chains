package legend.game.types;

import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.VECTOR;
import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;

/** 0x20 long */
public class GsRVIEW2 implements MemoryRef {
  private final Value ref;

  public final VECTOR viewpoint_00;
  public final VECTOR refpoint_0c;
  public final IntRef viewpointTwist_18;
  public final Pointer<GsCOORDINATE2> super_1c;

  public GsRVIEW2(final Value ref) {
    this.ref = ref;

    this.viewpoint_00 = this.ref.offset(4, 0x00L).cast(VECTOR::new);
    this.refpoint_0c = this.ref.offset(4, 0x0cL).cast(VECTOR::new);
    this.viewpointTwist_18 = this.ref.offset(4, 0x18L).cast(IntRef::new);
    this.super_1c = this.ref.offset(4, 0x1cL).cast(Pointer.deferred(4, GsCOORDINATE2::new));
  }

  public GsRVIEW2 set(final GsRVIEW2 other) {
    this.viewpoint_00.set(other.viewpoint_00);
    this.refpoint_0c.set(other.refpoint_0c);
    this.viewpointTwist_18.set(other.viewpointTwist_18);
    this.super_1c.setNullable(other.super_1c.derefNullable());
    return this;
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
