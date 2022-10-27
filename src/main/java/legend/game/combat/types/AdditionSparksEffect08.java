package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;

public class AdditionSparksEffect08 extends BttlScriptData6cSubBase1 {
  public final UnsignedByteRef count_00;

  public final UnsignedIntRef _04;

  public AdditionSparksEffect08(final Value ref) {
    super(ref);

    this.count_00 = ref.offset(1, 0x00L).cast(UnsignedByteRef::new);

    this._04 = ref.offset(4, 0x04L).cast(UnsignedIntRef::new);
  }
}
