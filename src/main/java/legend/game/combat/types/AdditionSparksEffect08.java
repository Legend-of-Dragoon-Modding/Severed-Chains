package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;

public class AdditionSparksEffect08 implements BttlScriptData6cSubBase1, MemoryRef {
  private final Value ref;

  public final UnsignedByteRef count_00;

  public final UnsignedIntRef _04;

  public AdditionSparksEffect08(final Value ref) {
    this.ref = ref;

    this.count_00 = ref.offset(1, 0x00L).cast(UnsignedByteRef::new);

    this._04 = ref.offset(4, 0x04L).cast(UnsignedIntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
