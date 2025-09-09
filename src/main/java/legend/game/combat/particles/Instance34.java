package legend.game.combat.particles;

import legend.core.memory.Method;
import legend.game.combat.effects.EffectManagerData6c;
import legend.game.combat.effects.EffectManagerParams;

import static legend.game.Scus94491BpeSegment.rcos;
import static legend.game.Scus94491BpeSegment.rsin;
import static legend.game.combat.Battle.seed_800fa754;

public class Instance34 extends ParticleEffectInstance94 {
  public Instance34(final int index, final ParticleEffectData98 particle) {
    super(index, particle);
  }

  @Method(0x800fffa0L)
  @Override
  protected void initType() {
    this._1a = this.particle.effectInner_08._10;
    this._14 = (short)(seed_800fa754.nextInt(4097));
    this._16 = (short)((seed_800fa754.nextInt(123) + 64) * this.particle.effectInner_08._18);
    this._18 = (short)(0x800 / this.particle.countParticleInstance_50 * this.index);
  }

  @Override
  protected void tickType(final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {

  }

  @Method(0x800fc348L)
  @Override
  protected void beforeRender(final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {
    this.particlePosition_50.x = rcos(this._14) * this._1a / (float)0x1000 * rsin(this._18) / 0x1000;
    this.particlePosition_50.y = this._18 * 2 - 0x800;
    this.particlePosition_50.z = rsin(this._14) * this._1a / (float)0x1000 * rsin(this._18) / 0x1000;
    this._14 += this._16;
  }
}
