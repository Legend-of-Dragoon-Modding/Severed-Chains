package legend.game.combat.particles;

import legend.core.memory.Method;
import legend.game.combat.effects.EffectManagerData6c;
import legend.game.combat.effects.EffectManagerParams;

import static legend.game.Scus94491BpeSegment.rcos;
import static legend.game.Scus94491BpeSegment.rsin;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;
import static legend.game.combat.Battle.seed_800fa754;

public class Instance5 extends ParticleEffectInstance94 {
  public Instance5(final int index, final ParticleEffectData98 particle) {
    super(index, particle);
  }

  @Method(0x800ff15cL)
  @Override
  protected void initType() {
    this.particleVelocity_58.y = -seed_800fa754.nextInt(61) - 60;

    final float scaleStep;
    if(vsyncMode_8007a3b8 != 4) {
      scaleStep = (short)-(seed_800fa754.nextInt(14) + 5) / (float)0x1000;
    } else {
      //LAB_800ff248
      scaleStep = (short)-(seed_800fa754.nextInt(21) + 10) / (float)0x1000;
    }

    this.scaleHorizontalStep_0a = scaleStep;
    this.scaleVerticalStep_0c = scaleStep;

    //LAB_800ff2b4
    this._14 = (short)seed_800fa754.nextInt(4097);
    this._1a = (short)seed_800fa754.nextInt(4097);
    this._1e = (short)(seed_800fa754.nextInt(1025) - 512);

    this._1c = 100;
    this._16 = this.particle.effectInner_08._10;
    this._18 = (short)(this.particle.effectInner_08._18 * 100);
  }

  @Override
  protected void tickType(final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {

  }

  @Method(0x800fba58L)
  @Override
  protected void beforeRender(final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {
    setParticlePositionAlongVector(this);

    this.particlePosition_50.x += rcos(this._1a) * this._1c / (float)0x1000;
    this.particlePosition_50.z += rsin(this._1a) * this._1c / (float)0x1000;
    this._16 += this._18;

    if(this._18 >= 4) {
      this._18 -= 4;
    }

    //LAB_800fbaf0
    this._1a += this._1e;
  }
}
