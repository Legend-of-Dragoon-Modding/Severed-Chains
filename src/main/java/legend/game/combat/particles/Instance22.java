package legend.game.combat.particles;

import legend.core.memory.Method;
import legend.game.combat.effects.EffectManagerData6c;
import legend.game.combat.effects.EffectManagerParams;

public class Instance22 extends Instance10 {
  public Instance22(final int index, final ParticleEffectData98 particle) {
    super(index, particle);
  }

  @Method(0x800ffa80L)
  @Override
  protected void initType() {
    super.initType();
    final short s2 = this.particle.effectInner_08._10;
    this._16 = s2;
    this._1c = s2;
    this._18 = (short)(this.particle.effectInner_08._18 * 0xe0);
  }

  @Override
  protected void tickType(final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {

  }

  @Method(0x800fbf50L)
  @Override
  protected void beforeRender(final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {
    setParticlePositionAlongVector(this);

    this._16 = (short)(this._16 * this._18 >> 8);

    final float v1 = this._1c / 4.0f;
    if(this._16 < v1) {
      this._16 = (short)v1;
    }

    //LAB_800fbfac
    this._14 += this._1a;
  }
}
