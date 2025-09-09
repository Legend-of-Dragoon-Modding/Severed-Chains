package legend.game.combat.particles;

import legend.core.memory.Method;
import legend.game.combat.effects.EffectManagerData6c;
import legend.game.combat.effects.EffectManagerParams;

import static legend.game.Scus94491BpeSegment.rcos;
import static legend.game.combat.Battle.seed_800fa754;

public class Instance60 extends ParticleEffectInstance94 {
  public Instance60(final int index, final ParticleEffectData98 particle) {
    super(index, particle);
  }

  @Method(0x80100cecL)
  @Override
  protected void initType() {
    this._14 = 0;
    this.particleAcceleration_60.y = this.particle.effectInner_08._18 * 2.0f;
  }

  @Override
  protected void tickType(final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {

  }

  @Method(0x800fc7c8L)
  @Override
  protected void beforeRender(final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {
    if(this.particlePosition_50.y + this.managerTranslation_2c.y >= manager.params_10.y_30) {
      this.particlePosition_50.y = manager.params_10.y_30 - this.managerTranslation_2c.y;
      this.particleVelocity_58.y = -this.particleVelocity_58.y / 2.0f;

      if(this._14 == 0) {
        final int angle = seed_800fa754.nextInt(0x1001);
        this.particleVelocity_58.x = (rcos(angle) >>> 8) * this.particle.effectInner_08._18;
        this.particleVelocity_58.z = (rcos(angle) >>> 8) * this.particle.effectInner_08._18;
      }

      //LAB_800fc8d8
      this._14 = 1;
    }

    //LAB_800fc8e0
  }
}
