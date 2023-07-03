package legend.game.combat.effects;

import legend.core.memory.Value;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;

public class SomeParticleStruct10 implements MemoryRef {
  public final Value ref;

  public final ByteRef _00;

  public final ShortRef initialTranslationScaleReduction_02;
  public final ShortRef _04;
  public final ByteRef hasVelocity_06;
  public final ByteRef hasScaleStep_07;
  public final ByteRef _08;
  public final ByteRef _09;

  public SomeParticleStruct10(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(1, 0x00).cast(ByteRef::new);
    this.initialTranslationScaleReduction_02 = ref.offset(2, 0x02).cast(ShortRef::new);
    this._04 = ref.offset(2, 0x04).cast(ShortRef::new);
    this.hasVelocity_06 = ref.offset(1, 0x06).cast(ByteRef::new);
    this.hasScaleStep_07 = ref.offset(1, 0x07).cast(ByteRef::new);
    this._08 = ref.offset(1, 0x08).cast(ByteRef::new);
    this._09 = ref.offset(1, 0x09).cast(ByteRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
