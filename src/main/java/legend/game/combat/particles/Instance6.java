package legend.game.combat.particles;

import legend.core.MathHelper;
import legend.core.memory.Method;
import legend.game.combat.effects.EffectManagerData6c;
import legend.game.combat.effects.EffectManagerParams;

import static legend.game.Scus94491BpeSegment.rcos;
import static legend.game.Scus94491BpeSegment.rsin;

public class Instance6 extends Instance5 {
  public Instance6(final int index, final ParticleEffectData98 particle) {
    super(index, particle);
  }

  @Method(0x800ff15cL)
  @Override
  protected void initType() {
    super.initType();
    this.verticalPositionScale_20 = (short)0;
    this.ticksUntilMovementModeChanges_22 = (short)(0x8000 / this.ticksRemaining_12);
    //TODO should this still be << 8?
    this.angleAcceleration_24 = MathHelper.psxDegToRad((this.particle.effectInner_08.particleInnerStuff_1c >>> 8 & 0xff) << 8);
  }

  @Method(0x800fba58L)
  @Override
  protected void beforeRender(final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {
    setParticlePositionAlongVector(this);

    this.particlePosition_50.x += rcos(this._1a) * this._1c / (float)0x1000;
    this.particlePosition_50.y += rsin(this._1a) * this._1c / (float)0x1000;

    if(this._16 >= this._18 * 4) {
      this._16 -= this._18;

      if(this._18 >= 4) {
        this._18 -= 4;
      }
    }

    //LAB_800fbbbc
    this._1a += this._1e;
  }
}
