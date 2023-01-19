package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.UnboundedArrayRef;

public class GoldDragoonTransformEffect20 implements BttlScriptData6cSubBase1, MemoryRef {
  private final Value ref;

  public final IntRef count_00;
  public final IntRef _04;
  public final Pointer<UnboundedArrayRef<GoldDragoonTransformEffectInstance84>> parts_08;

  public GoldDragoonTransformEffect20(final Value ref) {
    this.ref = ref;

    this.count_00 = ref.offset(4, 0x00L).cast(IntRef::new);
    this._04 = ref.offset(4, 0x04L).cast(IntRef::new);
    this.parts_08 = ref.offset(4, 0x08L).cast(Pointer.deferred(4, UnboundedArrayRef.of(0x84, GoldDragoonTransformEffectInstance84::new, this.count_00::get)));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
