package legend.game.combat.particles;

import legend.core.memory.Method;
import legend.game.combat.effects.EffectManagerData6c;
import legend.game.combat.effects.EffectManagerParams;

import static legend.game.Scus94491BpeSegment.rcos;
import static legend.game.Scus94491BpeSegment.rsin;

public class Instance32 extends ParticleEffectInstance94 {
  public Instance32(final int index, final ParticleEffectData98 particle) {
    super(index, particle);
  }

  @Method(0x800fff04L)
  @Override
  protected void initType() {
    this._14 = 0;
    this._18 = 0;
    this._1e = 0;
    final float v0 = this.particle.effectInner_08._18 * 64.0f;
    this._16 = (short)v0;
    this._1a = (short)v0;
    this._1c = this.particle.effectInner_08._10;
  }

  @Override
  protected void tickType(final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {

  }

  @Method(0x800fc280L)
  @Override
  protected void beforeRender(final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {
    this.particlePosition_50.z = rcos(this._14) * 2 * this._1c / (float)0x1000;
    this.particlePosition_50.x = rsin(this._1e) * this._1c / (float)0x1000;
    this._14 += this._16;
    this._1e += this._16 * 2;
    this.particlePosition_50.y += 0x40 + ((rcos(this._18) >> 1) + 0x800) * this._1c / (float)0x8000;
    this._18 += this._1a;
  }
}
