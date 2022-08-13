package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;

public abstract class BttlScriptData6cSubBase1 implements MemoryRef {
  private final Value ref;

  //TODO don't think these are actually part of the base
  public final IntRef scriptIndex_00;
  public final IntRef scriptIndex_04;

  public BttlScriptData6cSubBase1(final Value ref) {
    this.ref = ref;

    this.scriptIndex_00 = ref.offset(4, 0x00L).cast(IntRef::new);
    this.scriptIndex_04 = ref.offset(4, 0x04L).cast(IntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
