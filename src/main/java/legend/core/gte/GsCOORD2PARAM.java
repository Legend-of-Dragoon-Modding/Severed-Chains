package legend.core.gte;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;

public class GsCOORD2PARAM implements MemoryRef {
  private final Value ref;

  public final VECTOR scale;
  public final SVECTOR rotate;
  public final VECTOR trans;

  public GsCOORD2PARAM(final Value ref) {
    this.ref = ref;

    this.scale  = ref.offset(4, 0x00L).cast(VECTOR::new);
    this.rotate = ref.offset(2, 0x10L).cast(SVECTOR::new);
    this.trans  = ref.offset(4, 0x18L).cast(VECTOR::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
