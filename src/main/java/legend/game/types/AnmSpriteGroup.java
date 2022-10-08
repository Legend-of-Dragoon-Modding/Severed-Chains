package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnboundedArrayRef;

public class AnmSpriteGroup implements MemoryRef {
  private final Value ref;

  public final IntRef n_sprite_00;
  public final UnboundedArrayRef<AnmSpriteMetrics14> metrics_04;

  public AnmSpriteGroup(final Value ref) {
    this.ref = ref;

    this.n_sprite_00 = ref.offset(4, 0x00L).cast(IntRef::new);
    this.metrics_04 = ref.offset(4, 0x04L).cast(UnboundedArrayRef.of(0x14, AnmSpriteMetrics14::new));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
