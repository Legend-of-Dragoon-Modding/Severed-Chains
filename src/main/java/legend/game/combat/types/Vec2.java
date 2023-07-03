package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;

public class Vec2 implements MemoryRef {
  private final Value ref;

  public final IntRef x_00;
  public final IntRef y_04;

  public Vec2(final Value ref) {
    this.ref = ref;

    this.x_00 = ref.offset(4, 0x00L).cast(IntRef::new);
    this.y_04 = ref.offset(4, 0x04L).cast(IntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
