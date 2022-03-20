package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedIntRef;

public class WMapRender10 implements MemoryRef {
  private final Value ref;

  //TODO these might be bvecs (rgb?)
  public final UnsignedIntRef _00;
  public final UnsignedIntRef _04;
  public final UnsignedIntRef _08;
  public final UnsignedIntRef _0c;

  public WMapRender10(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(4, 0x00L).cast(UnsignedIntRef::new);
    this._04 = ref.offset(4, 0x04L).cast(UnsignedIntRef::new);
    this._08 = ref.offset(4, 0x08L).cast(UnsignedIntRef::new);
    this._0c = ref.offset(4, 0x0cL).cast(UnsignedIntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
