package legend.game.combat.particles;

import legend.core.memory.Value;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;

public class ParticleInitialTransformationMetrics10 implements MemoryRef {
  public final Value ref;

  public final ByteRef initialPositionMode_00;

  public final ShortRef initialTranslationMagnitudeReductionFactor1_02;
  public final ShortRef initialTranslationMagnitudeReductionFactor2_04;
  public final ByteRef hasSpecialAccelerationHandling_06;
  public final ByteRef hasSpecialScaleStepHandling_07;
  public final ByteRef scaleStepLowerBound_08;
  public final ByteRef scaleStepUpperBound_09;

  public ParticleInitialTransformationMetrics10(final Value ref) {
    this.ref = ref;

    this.initialPositionMode_00 = ref.offset(1, 0x00).cast(ByteRef::new);
    this.initialTranslationMagnitudeReductionFactor1_02 = ref.offset(2, 0x02).cast(ShortRef::new);
    this.initialTranslationMagnitudeReductionFactor2_04 = ref.offset(2, 0x04).cast(ShortRef::new);
    this.hasSpecialAccelerationHandling_06 = ref.offset(1, 0x06).cast(ByteRef::new);
    this.hasSpecialScaleStepHandling_07 = ref.offset(1, 0x07).cast(ByteRef::new);
    this.scaleStepLowerBound_08 = ref.offset(1, 0x08).cast(ByteRef::new);
    this.scaleStepUpperBound_09 = ref.offset(1, 0x09).cast(ByteRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
