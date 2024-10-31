package legend.game.combat.particles;

import legend.core.memory.Method;
import legend.game.combat.effects.EffectManagerData6c;
import legend.game.combat.effects.EffectManagerParams;

public class Instance58 extends ParticleEffectInstance94 {
  public Instance58(final int index, final ParticleEffectData98 particle) {
    super(index, particle);
  }

  @Method(0x80100c18L)
  @Override
  protected void initType() {
    this._14 = (short)(-this.particlePosition_50.x / 2.0f * this.particle.effectInner_08._18);
    this._16 = (short)(-this.particlePosition_50.y / 2.0f * this.particle.effectInner_08._18);
    this._18 = (short)(-this.particlePosition_50.z / 2.0f * this.particle.effectInner_08._18);
    this._1a = 0;
    this._1c = 0;
    this._1e = 0;
  }

  @Override
  protected void tickType(final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {

  }

  @Method(0x800fc768L)
  @Override
  protected void beforeRender(final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {
    this._1a += this._14;
    this._1c += this._16;
    this._1e += this._18;
    this.particleVelocity_58.x = this._1a / (float)0x100;
    this.particleVelocity_58.y = this._1c / (float)0x100;
    this.particleVelocity_58.z = this._1e / (float)0x100;
  }
}
