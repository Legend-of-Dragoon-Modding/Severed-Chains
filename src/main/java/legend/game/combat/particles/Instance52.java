package legend.game.combat.particles;

import legend.core.memory.Method;
import legend.game.combat.effects.EffectManagerData6c;
import legend.game.combat.effects.EffectManagerParams;

import static legend.game.Scus94491BpeSegment.rcos;

public class Instance52 extends ParticleEffectInstance94 {
  public Instance52(final int index, final ParticleEffectData98 particle) {
    super(index, particle);
  }

  @Method(0x801008f8L)
  @Override
  protected void initType() {
    this.angle_0e = 0.0f;
    this.angleVelocity_10 = 0.0f;
    this.spriteRotation_70.zero();
    this.spriteRotationStep_78.zero();
    this._18 = (short)(this.particle.effectInner_08._18 * 256);
    this._14 = 0x800;
    this._16 = (short)(this.particle.effectInner_08._10 * 16);
    this._1a = (short)(this.particle.effectInner_08._18 * 64);
    this.framesUntilRender_04 = (short)(this.index * (this.particle.effectInner_08._14 / this.particle.countParticleInstance_50));
  }

  @Override
  protected void tickType(final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {

  }

  @Method(0x800fc61cL)
  @Override
  protected void beforeRender(final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {
    this.scaleVertical_08 = (short)((rcos(this._14) * this._16 >> 12) * manager.params_10.scale_16.y) / (float)0x1000;
    this._14 -= this._18;

    if(this._16 > 0) {
      this._16 -= this._1a;
    }

    //LAB_800fc6a8
  }
}
