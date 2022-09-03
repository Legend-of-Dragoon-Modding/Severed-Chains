package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;

public class BattleDisplayStats144Sub10 implements MemoryRef {
  private final Value ref;

  public final ShortRef _00;
  public final ShortRef x_02;
  public final ShortRef y_04;
  public final ShortRef u_06;
  public final ShortRef v_08;
  public final ShortRef w_0a;
  public final ShortRef h_0c;
  public final ShortRef _0e;

  public BattleDisplayStats144Sub10(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(2, 0x00L).cast(ShortRef::new);
    this.x_02 = ref.offset(2, 0x02L).cast(ShortRef::new);
    this.y_04 = ref.offset(2, 0x04L).cast(ShortRef::new);
    this.u_06 = ref.offset(2, 0x06L).cast(ShortRef::new);
    this.v_08 = ref.offset(2, 0x08L).cast(ShortRef::new);
    this.w_0a = ref.offset(2, 0x0aL).cast(ShortRef::new);
    this.h_0c = ref.offset(2, 0x0cL).cast(ShortRef::new);
    this._0e = ref.offset(2, 0x0eL).cast(ShortRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
