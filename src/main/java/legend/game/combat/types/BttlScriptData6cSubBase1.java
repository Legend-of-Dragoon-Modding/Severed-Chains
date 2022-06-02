package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedIntRef;

public class BttlScriptData6cSubBase1 implements MemoryRef {
  private final Value ref;

  public final UnsignedIntRef scriptIndex_00;
  public final UnsignedIntRef scriptIndex_04;

  public BttlScriptData6cSubBase1(final Value ref) {
    this.ref = ref;

    this.scriptIndex_00 = ref.offset(4, 0x00L).cast(UnsignedIntRef::new);
    this.scriptIndex_04 = ref.offset(4, 0x04L).cast(UnsignedIntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
