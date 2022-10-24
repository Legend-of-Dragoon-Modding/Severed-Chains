package legend.game.types;

import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.SVECTOR;
import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;

public class Coord2AndThenSomeStruct_60 implements MemoryRef {
  private final Value ref;

  public final GsCOORDINATE2 coord2_00;
  public final IntRef _50;
  public final SVECTOR svec_54;

  public Coord2AndThenSomeStruct_60(final Value ref) {
    this.ref = ref;

    this.coord2_00 = ref.offset(4, 0x00L).cast(GsCOORDINATE2::new);
    this._50 = ref.offset(4, 0x50L).cast(IntRef::new);
    this.svec_54 = ref.offset(2, 0x54L).cast(SVECTOR::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
