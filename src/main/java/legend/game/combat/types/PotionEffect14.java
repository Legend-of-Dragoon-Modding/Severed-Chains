package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.QuintConsumerRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.game.types.Translucency;

public class PotionEffect14 implements BttlScriptData6cSubBase1, MemoryRef {
  private final Value ref;

  public final UnsignedByteRef _00;
  public final UnsignedByteRef _01;

  public final IntRef z_04;
  public final IntRef angleStep_08;
  public final UnsignedByteRef _0c;
  public final UnsignedByteRef _0d;
  public final UnsignedByteRef _0e;

  public final Pointer<QuintConsumerRef<EffectManagerData6c, Integer, short[], PotionEffect14, Translucency>> renderer_10;

  public PotionEffect14(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(1, 0x00L).cast(UnsignedByteRef::new);
    this._01 = ref.offset(1, 0x01L).cast(UnsignedByteRef::new);

    this.z_04 = ref.offset(4, 0x04L).cast(IntRef::new);
    this.angleStep_08 = ref.offset(4, 0x08L).cast(IntRef::new);
    this._0c = ref.offset(1, 0x0cL).cast(UnsignedByteRef::new);
    this._0d = ref.offset(1, 0x0dL).cast(UnsignedByteRef::new);
    this._0e = ref.offset(1, 0x0eL).cast(UnsignedByteRef::new);

    this.renderer_10 = ref.offset(4, 0x10L).cast(Pointer.deferred(4, QuintConsumerRef::new));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
