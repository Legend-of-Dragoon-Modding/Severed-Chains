package legend.game.combat.particles;

import legend.core.memory.Method;
import legend.game.combat.effects.EffectManagerData6c;
import legend.game.combat.effects.EffectManagerParams;

import static legend.game.Scus94491BpeSegment.rcos;
import static legend.game.Scus94491BpeSegment.rsin;
import static legend.game.combat.Battle.seed_800fa754;

public class Instance23 extends ParticleEffectInstance94 {
  public Instance23(final int index, final ParticleEffectData98 particle) {
    super(index, particle);
  }

  @Method(0x800ffadcL)
  @Override
  protected void initType() {
    this._14 = (short)(seed_800fa754.nextInt(4097));
    final int v0 = -this.particle.effectInner_08._10 >> 5;
    this.r_84 = 0;
    this.g_86 = 0;
    this.b_88 = 0;
    this._16 = this.particle.effectInner_08._10;
    this._18 = (short)(v0 * this.particle.effectInner_08._18);
    this._1a = (short)(this.particle.effectInner_08._18 * 128);
  }

  @Method(0x80100e4cL)
  @Override
  protected void tickType(final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {
    if(this.framesUntilRender_04 == 0) {
      this.stepR_8a = -1.0f / this.ticksRemaining_12;
      this.stepG_8c = -1.0f / this.ticksRemaining_12;
      this.stepB_8e = -1.0f / this.ticksRemaining_12;
    }

    //LAB_80100e98
  }

  @Method(0x800fbfd0L)
  @Override
  protected void beforeRender(final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {
    this.particlePosition_50.y = rsin(this._14) * this._16 >> 12;
    this.particlePosition_50.z = rcos(this._14) * this._16 >> 12;

    this._16 += this._18;
    if(this._16 < 0) {
      this._16 = 0;
    }

    //LAB_800fc044
    this._14 += this._1a;
  }
}
