package legend.game.combat.particles;

import legend.core.memory.Method;
import legend.game.combat.effects.EffectManagerData6c;
import legend.game.combat.effects.EffectManagerParams;

import static legend.game.combat.Battle.seed_800fa754;

public class Instance10 extends ParticleEffectInstance94 {
  public Instance10(final int index, final ParticleEffectData98 particle) {
    super(index, particle);
  }

  @Method(0x800ff5c4L)
  @Override
  protected void initType() {
    // This method uses an uninitialized variable - the way the ASM was generated, it uses t2 which is set
    // in the calling method to the behaviourType. Since this is the 10th behaviour, t2 will always be 10.
    final int t2 = 10;

    this.particleVelocity_58.y = -(seed_800fa754.nextInt(61) + 60) * this.particle.effectInner_08._18;
    this._14 = (short)seed_800fa754.nextInt(4097);
    this._16 = this.particle.effectInner_08._10;
    this._18 = (short)100;
    this._1a = t2 * 4;
    this.particleAcceleration_60.y = -this.particleVelocity_58.y / this.ticksRemaining_12;
  }

  @Override
  protected void tickType(final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {

  }

  @Method(0x800fbd04L)
  @Override
  protected void beforeRender(final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {
    setParticlePositionAlongVector(this);

    this._16 += this._18;
    if(this._18 >= 3) {
      this._18 -= 3;
    }

    //LAB_800fbd44
    this._14 += this._1a;
  }
}
