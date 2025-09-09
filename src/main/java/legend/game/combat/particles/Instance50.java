package legend.game.combat.particles;

import legend.core.memory.Method;
import legend.game.combat.effects.EffectManagerData6c;
import legend.game.combat.effects.EffectManagerParams;

import static legend.game.Scus94491BpeSegment.rcos;
import static legend.game.Scus94491BpeSegment.rsin;
import static legend.game.combat.Battle.seed_800fa754;

public class Instance50 extends ParticleEffectInstance94 {
  public Instance50(final int index, final ParticleEffectData98 particle) {
    super(index, particle);
  }

  @Method(0x80100878L)
  @Override
  protected void initType() {
    this.particleVelocity_58.y = -64.0f;
    this._18 = this.particle.effectInner_08._10;
    this._16 = (short)(this.particle.effectInner_08._18 * 0x2000);
    this._14 = (short)(seed_800fa754.nextInt(4097));
  }

  @Override
  protected void tickType(final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {

  }

  @Method(0x800fc5a8L)
  @Override
  protected void beforeRender(final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {
    this.particlePosition_50.x = rsin(this._14) * this._18 >> 12;
    this.particlePosition_50.z = rcos(this._14) * this._18 >> 12;
    this._18 = (short)(this._18 * 7 / 8);
  }
}
