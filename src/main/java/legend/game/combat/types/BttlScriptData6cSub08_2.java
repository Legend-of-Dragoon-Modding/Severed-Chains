package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.UnsignedIntRef;

public class BttlScriptData6cSub08_2 extends BttlScriptData6cSubBase1 {
  public final UnsignedIntRef _00;
  public final UnsignedIntRef _04;

  public BttlScriptData6cSub08_2(final Value ref) {
    super(ref);

    this._00 = ref.offset(4, 0x00L).cast(UnsignedIntRef::new);
    this._04 = ref.offset(4, 0x04L).cast(UnsignedIntRef::new);
  }
}
