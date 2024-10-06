package legend.game.combat.particles;

import legend.core.memory.Method;
import legend.game.combat.effects.EffectManagerData6c;
import legend.game.combat.effects.EffectManagerParams;

import static legend.game.Scus94491BpeSegment.rcos;
import static legend.game.Scus94491BpeSegment.rsin;
import static legend.game.combat.Battle.seed_800fa754;

public class Instance42 extends ParticleEffectInstance94 {
  public Instance42(final int index, final ParticleEffectData98 particle) {
    super(index, particle);
  }

  @Method(0x8010025cL)
  @Override
  protected void initType() {
    this.particleVelocity_58.y = 64.0f;
    final int angle = seed_800fa754.nextInt(4097);
    if(this.particle.effectInner_08.behaviourType_20 == 0x2a) {
      final int velocityMagnitude = (this.particle.effectInner_08._10 & 0xffff) >>> 5;
      this.particleVelocity_58.x = rcos(angle) * velocityMagnitude >> 12;
      this.particleVelocity_58.y = rsin(angle) * velocityMagnitude >> 12;
    } else {
      //LAB_80100328
      this.particleVelocity_58.x = rcos(angle) >>> 7;
      this.particleVelocity_58.y = rsin(angle) >>> 7;
    }
  }

  @Override
  protected void tickType(final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {

  }

  @Override
  protected void beforeRender(final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {

  }
}
