package legend.game.combat.particles;

import legend.core.memory.Method;
import legend.game.combat.effects.EffectManagerData6c;
import legend.game.combat.effects.EffectManagerParams;

import static legend.game.Scus94491BpeSegment.rcos;
import static legend.game.Scus94491BpeSegment.rsin;
import static legend.game.combat.Battle.seed_800fa754;

public class Instance1 extends ParticleEffectInstance94 {
  public Instance1(final int index, final ParticleEffectData98 particle) {
    super(index, particle);
  }

  @Method(0x800fec3cL)
  @Override
  protected void initType() {
    final int angle = seed_800fa754.nextInt(4097);
    this.particleVelocity_58.x = rcos(angle) >> 6;
    this.particleVelocity_58.z = rsin(angle) >> 6;
    this.particleVelocity_58.y = 0.0f;

    final float colourStep = (seed_800fa754.nextInt(101) - 50) / (float)0x80;
    this.r_84 += colourStep;
    this.g_86 += colourStep;
    this.b_88 += colourStep;
  }

  @Override
  protected void tickType(final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {

  }

  @Override
  protected void beforeRender(final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {

  }
}
