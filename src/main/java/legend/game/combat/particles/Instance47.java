package legend.game.combat.particles;

import legend.core.memory.Method;

import static legend.game.Scus94491BpeSegment.rcos;
import static legend.game.Scus94491BpeSegment.rsin;
import static legend.game.combat.Battle.seed_800fa754;

public class Instance47 extends Instance21 {
  public Instance47(final int index, final ParticleEffectData98 particle) {
    super(index, particle);
  }

  @Method(0x801005b8L)
  @Override
  protected void initType() {
    super.initType();
    final int s2 = this.particle.effectInner_08._10;
    final int angle = seed_800fa754.nextInt(4097);
    this.particlePosition_50.y = rsin(angle) * s2 >> 12;
    this.particlePosition_50.z = rcos(angle) * s2 >> 12;
    this.particleVelocity_58.x = (seed_800fa754.nextInt(65) + 54) * this.particle.effectInner_08._18;
    this.particleAcceleration_60.x = -this.particleVelocity_58.x / this.ticksRemaining_12;
    this.particleAcceleration_60.y = 16.0f;
    final int a1_0 = -((rsin(angle) * s2 >> 12) + s2) / 2;
    this.particleVelocity_58.y = (seed_800fa754.nextInt(-a1_0 + 1) + a1_0) * this.particle.effectInner_08._18;
  }
}
