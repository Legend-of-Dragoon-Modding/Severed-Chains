package legend.game.combat.particles;

import legend.core.memory.Method;
import legend.game.combat.effects.EffectManagerData6c;
import legend.game.combat.effects.EffectManagerParams;

public class Instance27 extends Instance24 {
  public Instance27(final int index, final ParticleEffectData98 particle) {
    super(index, particle);
  }

  @Override
  protected void tickType(final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {

  }

  @Method(0x800fc1fcL)
  @Override
  protected void beforeRender(final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {
    setParticlePositionAlongVector(this);

    this._16 += this._18;
    if(this.particlePosition_50.y + this.managerTranslation_2c.y >= manager.params_10.y_30) {
      this.particlePosition_50.y = manager.params_10.y_30 - this.managerTranslation_2c.y;
      this.particleVelocity_58.y = -this.particleVelocity_58.y / 2.0f;
    }

    //LAB_800fc26c
  }
}
