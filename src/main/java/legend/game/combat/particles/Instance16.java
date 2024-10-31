package legend.game.combat.particles;

import legend.core.MathHelper;
import legend.core.memory.Method;
import legend.game.combat.effects.EffectManagerData6c;
import legend.game.combat.effects.EffectManagerParams;
import org.joml.Math;
import org.joml.Vector3f;

import static legend.game.Scus94491BpeSegment.rcos;
import static legend.game.Scus94491BpeSegment.rsin;
import static legend.game.combat.SEffe.rotateAndTranslateEffect;

public class Instance16 extends ParticleEffectInstance94 {
  public Instance16(final int index, final ParticleEffectData98 particle) {
    super(index, particle);
  }

  @Method(0x800ff6fcL)
  @Override
  protected void initType() {
    this.angleVelocity_10 = MathHelper.TWO_PI / 32.0f;
    final int v1 = this.index >>> 1;
    this.angle_0e = MathHelper.psxDegToRad(v1 << 7);
    this._14 = (short)(v1 << 7);
    this._16 = this.particle.effectInner_08._10;
    this._18 = (short)(this.index & 0x1);
    this._1a = (short)(v1 << 3);

    final float colour = Math.max(0, (this.particle.effectInner_08.particleInnerStuff_1c >>> 8 & 0xff) - this.index * 16) / (float)0x80;

    //LAB_800ff754
    this.ticksRemaining_12 = -1;
    this.r_84 = colour;
    this.g_86 = colour;
    this.b_88 = colour;
  }

  @Method(0x80100e28L)
  @Override
  protected void tickType(final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {
    if(this.framesUntilRender_04 == 0) {
      this.stepR_8a = 0;
      this.stepG_8c = 0;
      this.stepB_8e = 0;
    }
  }

  @Method(0x800fbd68L)
  @Override
  protected void beforeRender(final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {
    final Vector3f sp0x38 = new Vector3f();
    if(this._18 == 0) {
      setParticlePositionAlongVector(this);
      this.particlePosition_50.y = 0.0f;
      this.particlePosition_50.z /= 2.0f;
      sp0x38.set(MathHelper.psxDegToRad(this._1a), 0.0f, 0.0f);
    } else {
      //LAB_800fbdb8
      this.particlePosition_50.x = 0.0f;
      this.particlePosition_50.y = rsin(this._14) * this._16 >> 11;
      this.particlePosition_50.z = rcos(this._14) * this._16 >> 12;
      sp0x38.set(0.0f, 0.0f, MathHelper.psxDegToRad(this._1a));
    }

    //LAB_800fbe10
    this._14 -= 0x80;
    this._1a -= 8;

    final Vector3f sp0x28 = new Vector3f();
    rotateAndTranslateEffect(manager, sp0x38, this.particlePosition_50, sp0x28);
    this.particlePosition_50.set(sp0x28);
  }
}
