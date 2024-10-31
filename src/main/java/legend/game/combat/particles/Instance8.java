package legend.game.combat.particles;

import legend.core.MathHelper;
import legend.core.memory.Method;
import legend.game.combat.effects.EffectManagerData6c;
import legend.game.combat.effects.EffectManagerParams;

import static legend.game.Scus94491BpeSegment.rsin;
import static legend.game.combat.Battle.seed_800fa754;

public class Instance8 extends ParticleEffectInstance94 {
  public Instance8(final int index, final ParticleEffectData98 particle) {
    super(index, particle);
  }

  @Method(0x800ff430L)
  @Override
  protected void initType() {
    this._16 = this.particle.effectInner_08._10;
    this._18 = (short)0;
    this._1a = 0;
    this._1c = 50;
    this.verticalPositionScale_20 = (short)100;
    this.ticksUntilMovementModeChanges_22 = (short)0;
    this.angleAcceleration_24 = this.angleVelocity_10 / this.ticksRemaining_12;

    this.particleVelocity_58.y = seed_800fa754.nextInt(31) + 10;
    this._1e = (short)(seed_800fa754.nextInt(41) + 40);
    this._14 = (short)seed_800fa754.nextInt(4097);
  }

  @Override
  protected void tickType(final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {

  }

  @Method(0x800fbbe0L)
  @Override
  protected void beforeRender(final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {
    setParticlePositionAlongVector(this);

    this._16 += this._1c;
    this.particleVelocity_58.y += 20.0f;
    this.angleVelocity_10 -= this.angleAcceleration_24;

    if(this._18 == 1) {
      if(this.ticksUntilMovementModeChanges_22 != 0) {
        //LAB_800fbca4
        this.particlePosition_50.y = -this.managerTranslation_2c.y;
        this.ticksUntilMovementModeChanges_22--;
      } else {
        this.particlePosition_50.y = (rsin(this._1a) * this.verticalPositionScale_20 >> 12) - this.managerTranslation_2c.y;
        this._1a += 0x7f;
        this.scaleHorizontalStep_0a = MathHelper.psxDegToRad(this._1e);
        this.scaleVerticalStep_0c = MathHelper.psxDegToRad(this._1e);
        this._1c += 5;
        this.verticalPositionScale_20 += 20;
      }
      //LAB_800fbcc0
    } else if(this.managerTranslation_2c.y + this.particlePosition_50.y >= -1000) {
      this._1c = 0;
      this._18 = 1;
      this.scaleHorizontalStep_0a = MathHelper.psxDegToRad(this._1e);
      this.scaleVerticalStep_0c = MathHelper.psxDegToRad(this._1e);
    }

    //LAB_800fbcf4
  }
}
