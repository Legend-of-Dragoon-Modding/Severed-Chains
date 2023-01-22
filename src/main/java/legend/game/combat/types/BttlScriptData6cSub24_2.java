package legend.game.combat.types;

import legend.core.gte.SVECTOR;
import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.Pointer;
import legend.game.combat.deff.DeffPart;

public class BttlScriptData6cSub24_2 extends BttlScriptData6cSubBase2 {
  public final IntRef _0c;
  public final IntRef _10;
  public final Pointer<DeffPart.LmbType> lmb_14;
  public final IntRef _18;
  public final SVECTOR _1c;

  public BttlScriptData6cSub24_2(final Value ref) {
    super(ref);

    this._0c = ref.offset(4, 0x0cL).cast(IntRef::new);
    this._10 = ref.offset(4, 0x10L).cast(IntRef::new);
    this.lmb_14 = ref.offset(4, 0x14L).cast(Pointer.deferred(4, DeffPart.LmbType::new));
    this._18 = ref.offset(4, 0x18L).cast(IntRef::new);
    this._1c = ref.offset(2, 0x1cL).cast(SVECTOR::new);
  }
}
