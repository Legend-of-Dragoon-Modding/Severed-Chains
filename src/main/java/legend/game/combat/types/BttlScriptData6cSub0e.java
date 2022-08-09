package legend.game.combat.types;

import legend.core.gte.SVECTOR;
import legend.core.memory.Value;
import legend.core.memory.types.ShortRef;

public class BttlScriptData6cSub0e extends BttlScriptData6cSubBase1 {
  public final SVECTOR svec_00;
  public final SVECTOR svec_06;
  public final ShortRef scale_0c;

  public BttlScriptData6cSub0e(final Value ref) {
    super(ref);

    this.svec_00 = ref.offset(2, 0x00L).cast(SVECTOR::new);
    this.svec_06 = ref.offset(2, 0x06L).cast(SVECTOR::new);
    this.scale_0c = ref.offset(2, 0x0cL).cast(ShortRef::new);
  }
}
