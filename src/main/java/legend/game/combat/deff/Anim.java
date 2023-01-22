package legend.game.combat.deff;

import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;

public abstract class Anim implements MemoryRef {
  protected final Value ref;

  public final IntRef magic_00;

  public Anim(final Value ref) {
    this.ref = ref;

    this.magic_00 = ref.offset(4, 0x00L).cast(IntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
