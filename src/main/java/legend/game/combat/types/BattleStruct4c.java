package legend.game.combat.types;

import legend.core.gte.SVECTOR;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;

public class BattleStruct4c implements MemoryRef {
  private final Value ref;

  public final SVECTOR ambientColour_00;
  public final ShortRef _06;
  public final ShortRef _08;
  public final ShortRef _0a;
  public final ShortRef _0c;
  public final ShortRef _0e;
  public final ArrayRef<BattleStruct14> _10;

  public BattleStruct4c(final Value ref) {
    this.ref = ref;

    this.ambientColour_00 = ref.offset(2, 0x00L).cast(SVECTOR::new);
    this._06 = ref.offset(2, 0x06L).cast(ShortRef::new);
    this._08 = ref.offset(2, 0x08L).cast(ShortRef::new);
    this._0a = ref.offset(2, 0x0aL).cast(ShortRef::new);
    this._0c = ref.offset(2, 0x0cL).cast(ShortRef::new);
    this._0e = ref.offset(2, 0x0eL).cast(ShortRef::new);
    this._10 = ref.offset(4, 0x10L).cast(ArrayRef.of(BattleStruct14.class, 3, 0x14, BattleStruct14::new));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
