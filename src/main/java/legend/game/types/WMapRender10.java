package legend.game.types;

import legend.core.gte.COLOUR;
import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;

public class WMapRender10 implements MemoryRef {
  private final Value ref;

  public final COLOUR _00;
  public final COLOUR _04;
  public final COLOUR _08;
  public final COLOUR _0c;

  public WMapRender10(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(4, 0x00L).cast(COLOUR::new);
    this._04 = ref.offset(4, 0x04L).cast(COLOUR::new);
    this._08 = ref.offset(4, 0x08L).cast(COLOUR::new);
    this._0c = ref.offset(4, 0x0cL).cast(COLOUR::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
