package legend.game.combat.particles;

import legend.core.memory.Method;
import legend.game.combat.effects.EffectManagerData6c;
import legend.game.combat.effects.EffectManagerParams;

public class Instance24 extends ParticleEffectInstance94 {
  public Instance24(final int index, final ParticleEffectData98 particle) {
    super(index, particle);
  }

  @Method(0x800ffb80L)
  @Override
  protected void initType() {
    this.particleAcceleration_60.y = 8.0f;
    this._14 = (short)(this.index << 9);
    this._16 = this.particle.effectInner_08._10;
    this.framesUntilRender_04 = (short)(this.index >>> 2 | 0x1);
    this._18 = (short)(this.particle.effectInner_08._18 * 64.0f);
    this.particleVelocity_58.y = this.particle.effectInner_08._14 * -0x40 >> 8;
  }

  @Override
  protected void tickType(final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {
    if(this.framesUntilRender_04 == 0) {
      this.stepR_8a = 0;
      this.stepG_8c = 0;
      this.stepB_8e = 0;
    }
  }

  @Method(0x800fc068L)
  @Override
  protected void beforeRender(final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {
    setParticlePositionAlongVector(this);

    this._16 += this._18;

    if(this.particlePosition_50.y + this.managerTranslation_2c.y >= manager.params_10.y_30) {
      this.ticksRemaining_12 = 1;
    }

    //LAB_800fc0bc
  }
}
