package legend.game.combat.particles;

import legend.core.memory.Method;
import legend.game.combat.effects.EffectManagerData6c;
import legend.game.combat.effects.EffectManagerParams;

import static legend.game.combat.Battle.seed_800fa754;

public class Instance28 extends Instance24 {
  public Instance28(final int index, final ParticleEffectData98 particle) {
    super(index, particle);
  }

  @Method(0x800ffe80L)
  @Override
  protected void initType() {
    this._14 = (short)(seed_800fa754.nextInt(4097));
    this._16 = this.particle.effectInner_08._10;
    this._18 = (short)(this.particle.effectInner_08._18 * 32.0f);
    this.particleAcceleration_60.y = this.particle.effectInner_08._18 * 2.0f;
  }

  @Override
  protected void tickType(final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {

  }
}
