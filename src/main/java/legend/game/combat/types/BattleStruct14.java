package legend.game.combat.types;

import legend.core.gte.COLOUR;
import legend.core.gte.SVECTOR;
import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;

public class BattleStruct14 implements MemoryRef {
  private final Value ref;

  public final SVECTOR lightDirection_00;
  public final ShortRef _06;
  public final ShortRef _08;
  public final COLOUR lightColour_0a;
  public final COLOUR _0d;
  public final ShortRef _10;
  public final ShortRef _12;

  public BattleStruct14(final Value ref) {
    this.ref = ref;

    this.lightDirection_00 = ref.offset(2, 0x00L).cast(SVECTOR::new);
    this._06 = ref.offset(2, 0x06L).cast(ShortRef::new);
    this._08 = ref.offset(2, 0x08L).cast(ShortRef::new);
    this.lightColour_0a = ref.offset(1, 0x0aL).cast(COLOUR::new);
    this._0d = ref.offset(1, 0x0dL).cast(COLOUR::new);
    this._10 = ref.offset(2, 0x10L).cast(ShortRef::new);
    this._12 = ref.offset(2, 0x12L).cast(ShortRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
