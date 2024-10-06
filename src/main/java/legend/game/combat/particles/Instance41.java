package legend.game.combat.particles;

import legend.core.memory.Method;
import legend.game.combat.effects.EffectManagerData6c;
import legend.game.combat.effects.EffectManagerParams;

import static legend.game.Scus94491BpeSegment.rcos;
import static legend.game.Scus94491BpeSegment.rsin;
import static legend.game.combat.Battle.seed_800fa754;

public class Instance41 extends ParticleEffectInstance94 {
  public Instance41(final int index, final ParticleEffectData98 particle) {
    super(index, particle);
  }

  @Method(0x80100150L)
  @Override
  protected void initType() {
    this._1a = (short)(seed_800fa754.nextInt(4097));
    this._14 = (short)this.particlePosition_50.x;
    this._16 = (short)this.particlePosition_50.y;
    this._18 = (short)this.particlePosition_50.z;
    this._1e = (short)(this.particle.effectInner_08._10 / 4);
    this.particleVelocity_58.y = this.particle.effectInner_08._18 * -64.0f;
    this._1c = (short)((seed_800fa754.nextInt(513) - 256) * this.particle.effectInner_08._18);
  }

  @Override
  protected void tickType(final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {

  }

  @Method(0x800fc42cL)
  @Override
  protected void beforeRender(final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {
    this.particlePosition_50.x = this._14 + rcos(this._1a) * this._1e / (float)0x1000;
    this.particlePosition_50.z = this._18 + rsin(this._1a) * this._1e / (float)0x1000;
    this._1e += 16;
    this._1a += this._1c;
  }
}
