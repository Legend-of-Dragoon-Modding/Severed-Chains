package legend.game.types;

import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.SVECTOR;
import legend.core.memory.Value;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;

public class WMapStruct258Sub60 implements MemoryRef {
  private final Value ref;

  public final GsCOORDINATE2 coord2_00;
  public final SVECTOR rotation_50;
  public final ShortRef _58;
  public final ShortRef _5a;
  public final ShortRef _5c;
  public final ByteRef _5e;

  public WMapStruct258Sub60(final Value ref) {
    this.ref = ref;

    this.coord2_00 = ref.offset(4, 0x00L).cast(GsCOORDINATE2::new);
    this.rotation_50 = ref.offset(2, 0x50L).cast(SVECTOR::new);
    this._58 = ref.offset(2, 0x58L).cast(ShortRef::new);
    this._5a = ref.offset(2, 0x5aL).cast(ShortRef::new);
    this._5c = ref.offset(2, 0x5cL).cast(ShortRef::new);
    this._5e = ref.offset(1, 0x5eL).cast(ByteRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
