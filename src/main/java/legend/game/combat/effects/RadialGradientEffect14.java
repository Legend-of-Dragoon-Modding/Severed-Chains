package legend.game.combat.effects;

import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.QuintConsumerRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.game.types.Translucency;

public class RadialGradientEffect14 implements BttlScriptData6cSubBase1, MemoryRef {
  private final Value ref;

  public final UnsignedByteRef circleSubdivisionModifier_00;
  public final UnsignedByteRef scaleModifier_01;

  public final IntRef z_04;
  public final IntRef angleStep_08;
  public final UnsignedByteRef r_0c;
  public final UnsignedByteRef g_0d;
  public final UnsignedByteRef b_0e;

  public final Pointer<QuintConsumerRef<EffectManagerData6c, Integer, short[], RadialGradientEffect14, Translucency>> renderer_10;

  public RadialGradientEffect14(final Value ref) {
    this.ref = ref;

    this.circleSubdivisionModifier_00 = ref.offset(1, 0x00L).cast(UnsignedByteRef::new);
    this.scaleModifier_01 = ref.offset(1, 0x01L).cast(UnsignedByteRef::new);

    this.z_04 = ref.offset(4, 0x04L).cast(IntRef::new);
    this.angleStep_08 = ref.offset(4, 0x08L).cast(IntRef::new);
    this.r_0c = ref.offset(1, 0x0cL).cast(UnsignedByteRef::new);
    this.g_0d = ref.offset(1, 0x0dL).cast(UnsignedByteRef::new);
    this.b_0e = ref.offset(1, 0x0eL).cast(UnsignedByteRef::new);

    this.renderer_10 = ref.offset(4, 0x10L).cast(Pointer.deferred(4, QuintConsumerRef::new));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
