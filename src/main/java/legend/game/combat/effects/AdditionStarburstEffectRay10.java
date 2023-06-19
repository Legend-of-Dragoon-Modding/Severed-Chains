package legend.game.combat.effects;

import legend.core.memory.Value;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;

public class AdditionStarburstEffectRay10 implements MemoryRef {
  private final Value ref;

  public final ByteRef renderRay_00;

  public final ShortRef angle_02;
  /** Set to 0x10 and never used */
  public final ShortRef unused_04;
  public final ShortRef endpointTranslationMagnitude_06;
  public final ShortRef endpointTranslationMagnitudeVelocity_08;
  public final ShortRef angleModifier_0a;
  /** Set to 0 and never used */
  public final IntRef unused_0c;

  public AdditionStarburstEffectRay10(final Value ref) {
    this.ref = ref;

    this.renderRay_00 = ref.offset(1, 0x00).cast(ByteRef::new);
    this.angle_02 = ref.offset(2, 0x02).cast(ShortRef::new);
    this.unused_04 = ref.offset(2, 0x04).cast(ShortRef::new);
    this.endpointTranslationMagnitude_06 = ref.offset(2, 0x06).cast(ShortRef::new);
    this.endpointTranslationMagnitudeVelocity_08 = ref.offset(2, 0x08).cast(ShortRef::new);
    this.angleModifier_0a = ref.offset(2, 0x0a).cast(ShortRef::new);
    this.unused_0c = ref.offset(4, 0x0c).cast(IntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
