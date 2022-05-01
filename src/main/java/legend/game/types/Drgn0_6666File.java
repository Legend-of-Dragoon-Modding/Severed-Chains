package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;

public class Drgn0_6666File implements MemoryRef {
  private final Value ref;

  public final Drgn0_6666Struct _0000;
  public final Drgn0_6666Struct _c6a4;
  public final Drgn0_6666Struct _cfac;

  public Drgn0_6666File(final Value ref) {
    this.ref = ref;

    this._0000 = ref.offset(0, 0x0000L).cast(Drgn0_6666Struct::new);
    this._c6a4 = ref.offset(0, 0xc6a4L).cast(Drgn0_6666Struct::new);
    this._cfac = ref.offset(0, 0xcfacL).cast(Drgn0_6666Struct::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
