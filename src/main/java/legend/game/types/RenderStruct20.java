package legend.game.types;

import legend.core.gte.GsCOORDINATE2;
import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;

public class RenderStruct20 implements MemoryRef {
  private final Value ref;

  public final IntRef _00;
  public final IntRef _04;
  public final IntRef _08;
  public final IntRef _0c;
  public final IntRef _10;
  public final IntRef _14;
  public final IntRef _18;
  public final Pointer<GsCOORDINATE2> _1c;

  public RenderStruct20(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(4, 0x00L).cast(IntRef::new);
    this._04 = ref.offset(4, 0x04L).cast(IntRef::new);
    this._08 = ref.offset(4, 0x08L).cast(IntRef::new);
    this._0c = ref.offset(4, 0x0cL).cast(IntRef::new);
    this._10 = ref.offset(4, 0x10L).cast(IntRef::new);
    this._14 = ref.offset(4, 0x14L).cast(IntRef::new);
    this._18 = ref.offset(4, 0x18L).cast(IntRef::new);
    this._1c = ref.offset(4, 0x1cL).cast(Pointer.deferred(4, GsCOORDINATE2::new));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
