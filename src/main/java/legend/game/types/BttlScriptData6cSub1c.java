package legend.game.types;

import legend.core.gpu.RECT;
import legend.core.memory.Value;
import legend.core.memory.types.IntRef;

public class BttlScriptData6cSub1c extends BttlScriptData6cSubBase {
  public final RECT _0c;
  public final IntRef _14;
  public final IntRef _18;

  public BttlScriptData6cSub1c(final Value ref) {
    super(ref);

    this._0c = ref.offset(2, 0x0cL).cast(RECT::new);
    this._14 = ref.offset(4, 0x14L).cast(IntRef::new);
    this._18 = ref.offset(4, 0x18L).cast(IntRef::new);
  }
}
