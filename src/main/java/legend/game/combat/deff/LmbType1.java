package legend.game.combat.deff;

import legend.core.memory.Value;
import legend.core.memory.types.RelativePointer;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnboundedArrayRef;

public class LmbType1 extends Lmb {
  public final ShortRef _0a;

  public final RelativePointer<UnboundedArrayRef<LmbTransforms14>> _10;

  public LmbType1(final Value ref) {
    super(ref);

    this._0a = ref.offset(2, 0x0aL).cast(ShortRef::new);

    this._10 = ref.offset(4, 0x10L).cast(RelativePointer.deferred(4, ref.getAddress(), UnboundedArrayRef.of(0x14, LmbTransforms14::new, this.count_04::get)));
  }
}
