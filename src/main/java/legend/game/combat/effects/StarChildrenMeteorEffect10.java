package legend.game.combat.effects;

import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.UnboundedArrayRef;

public class StarChildrenMeteorEffect10 implements BttlScriptData6cSubBase1, MemoryRef {
  private final Value ref;

  public final IntRef count_00;
  public final SpriteMetrics08 metrics_04;
  public final Pointer<UnboundedArrayRef<StarChildrenMeteorEffectInstance10>> meteorArray_0c;

  public StarChildrenMeteorEffect10(final Value ref) {
    this.ref = ref;

    this.count_00 = ref.offset(4, 0x00L).cast(IntRef::new);
    this.metrics_04 = ref.offset(4, 0x04L).cast(SpriteMetrics08::new);
    this.meteorArray_0c = ref.offset(4, 0x0cL).cast(Pointer.deferred(4, UnboundedArrayRef.of(0x10, StarChildrenMeteorEffectInstance10::new)));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
