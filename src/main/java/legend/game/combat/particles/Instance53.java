package legend.game.combat.particles;

import legend.core.memory.Method;
import legend.game.combat.effects.EffectManagerData6c;
import legend.game.combat.effects.EffectManagerParams;

import static legend.game.Scus94491BpeSegment.rcos;
import static legend.game.Scus94491BpeSegment.rsin;
import static legend.game.combat.Battle.seed_800fa754;

public class Instance53 extends ParticleEffectInstance94 {
  public Instance53(final int index, final ParticleEffectData98 particle) {
    super(index, particle);
  }

  @Method(0x80100978L)
  @Override
  protected void initType() {
    this._14 = (short)(seed_800fa754.nextInt(4097));
    this._16 = (short)(seed_800fa754.nextInt((this.particle.effectInner_08._10 & 0xffff) + 1));
    this._18 = (short)(seed_800fa754.nextInt(4097));
    this._1a = (short)((seed_800fa754.nextInt(41) + 150) * this.particle.effectInner_08._18);
    this.particleVelocity_58.y = -64.0f;
  }

  @Override
  protected void tickType(final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {

  }

  @Method(0x800fc6bcL)
  @Override
  protected void beforeRender(final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {
    this.particlePosition_50.x = rsin(this._14) * this._16 >> 12;
    this.particlePosition_50.z = rcos(this._14) * this._16 >> 12;
    this.particlePosition_50.x += rsin(this._18) << 8 >> 12;
    this.particlePosition_50.z += rcos(this._18) << 8 >> 12;
    this._18 += this._1a;
  }
}
