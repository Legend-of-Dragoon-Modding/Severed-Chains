package legend.game.combat.effects;

import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.UnboundedArrayRef;

public class GradientRaysEffect24 implements BttlScriptData6cSubBase1, MemoryRef {
  private final Value ref;

  public final Pointer<UnboundedArrayRef<GradientRaysEffectInstance04>> rayArray_00;
  public final IntRef count_04;
  public final IntRef _08;
  public final IntRef _0c;
  public final IntRef _10;
  public final IntRef _14;
  public final IntRef _18;
  public final IntRef _1c;
  public final IntRef _20;

  public GradientRaysEffect24(final Value ref) {
    this.ref = ref;

    this.rayArray_00 = ref.offset(4, 0x00L).cast(Pointer.deferred(4, UnboundedArrayRef.of(4, GradientRaysEffectInstance04::new)));
    this.count_04 = ref.offset(4, 0x04L).cast(IntRef::new);
    this._08 = ref.offset(4, 0x08L).cast(IntRef::new);
    this._0c = ref.offset(4, 0x0cL).cast(IntRef::new);
    this._10 = ref.offset(4, 0x10L).cast(IntRef::new);
    this._14 = ref.offset(4, 0x14L).cast(IntRef::new);
    this._18 = ref.offset(4, 0x18L).cast(IntRef::new);
    this._1c = ref.offset(4, 0x1cL).cast(IntRef::new);
    this._20 = ref.offset(4, 0x20L).cast(IntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
