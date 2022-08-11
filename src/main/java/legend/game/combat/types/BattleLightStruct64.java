package legend.game.combat.types;

import legend.core.gte.VECTOR;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;

public class BattleLightStruct64 implements MemoryRef {
  private final Value ref;

  public final VECTOR colour_00;
  public final VECTOR colour_0c;
  public final VECTOR _18;
  public final IntRef _24;

  public final ShortRef _2c;
  public final ShortRef _2e;
  public final ArrayRef<VECTOR> _30;
  public final IntRef _60;

  public BattleLightStruct64(final Value ref) {
    this.ref = ref;

    this.colour_00 = ref.offset(4, 0x00L).cast(VECTOR::new);
    this.colour_0c = ref.offset(4, 0x0cL).cast(VECTOR::new);
    this._18 = ref.offset(4, 0x18L).cast(VECTOR::new);
    this._24 = ref.offset(4, 0x24L).cast(IntRef::new);

    this._2c = ref.offset(2, 0x2cL).cast(ShortRef::new);
    this._2e = ref.offset(2, 0x2eL).cast(ShortRef::new);
    this._30 = ref.offset(4, 0x30L).cast(ArrayRef.of(VECTOR.class, 4, 0xc, VECTOR::new));
    this._60 = ref.offset(4, 0x60L).cast(IntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
