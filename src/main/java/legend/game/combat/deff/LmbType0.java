package legend.game.combat.deff;

import legend.core.memory.Value;
import legend.core.memory.types.ShortRef;

public class LmbType0 extends Lmb {
  public final ShortRef _0c;

  public LmbType0(final Value ref) {
    super(ref);

    this._0c = ref.offset(4, 0x0cL).cast(ShortRef::new);
  }
}
