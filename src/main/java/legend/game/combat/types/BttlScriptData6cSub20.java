package legend.game.combat.types;

import legend.core.gte.DVECTOR;
import legend.core.memory.Value;

public class BttlScriptData6cSub20 extends BttlScriptData6cSub1c {
  public final DVECTOR _1c;

  public BttlScriptData6cSub20(final Value ref) {
    super(ref);

    this._1c = ref.offset(2, 0x1cL).cast(DVECTOR::new);
  }
}
