package legend.game.combat.particles;

import legend.core.MathHelper;
import legend.core.memory.Method;
import legend.game.combat.effects.EffectManagerData6c;
import legend.game.combat.effects.EffectManagerParams;
import org.joml.Vector3f;

import static legend.game.combat.Battle.seed_800fa754;
import static legend.game.combat.SEffe.rotateAndTranslateEffect;

public class Instance20 extends ParticleEffectInstance94 {
  public Instance20(final int index, final ParticleEffectData98 particle) {
    super(index, particle);
  }

  @Method(0x800ff788L)
  @Override
  protected void initType() {
    this.ticksRemaining_12 = -1;
    this._14 = (short)seed_800fa754.nextInt(4097);
    this._16 = this.particle.effectInner_08._10;
    this._18 = (short)seed_800fa754.nextInt(4097);
    this._1a = (short)seed_800fa754.nextInt(4097);
  }

  @Override
  protected void tickType(final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {
    if(this.framesUntilRender_04 == 0) {
      this.stepR_8a = 0;
      this.stepG_8c = 0;
      this.stepB_8e = 0;
    }
  }

  @Method(0x800fbe94L)
  @Override
  protected void beforeRender(final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {
    setParticlePositionAlongVector(this);
    this.particlePosition_50.y = 0.0f;
    this.particlePosition_50.z /= 2.0f;
    final Vector3f sp0x38 = new Vector3f(MathHelper.psxDegToRad(this._18), 0.0f, MathHelper.psxDegToRad(this._1a));
    this._14 += 0x80;

    final Vector3f sp0x28 = new Vector3f();
    rotateAndTranslateEffect(manager, sp0x38, this.particlePosition_50, sp0x28);
    this.particlePosition_50.set(sp0x28);
  }
}
