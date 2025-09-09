package legend.game.combat.particles;

import legend.core.memory.Method;
import legend.game.combat.effects.EffectManagerData6c;
import legend.game.combat.effects.EffectManagerParams;

import static legend.game.combat.Battle.seed_800fa754;

public class Instance4 extends Instance3 {
  public Instance4(final int index, final ParticleEffectData98 particle) {
    super(index, particle);
  }

  @Method(0x800fefe4L)
  @Override
  protected void initType() {
    super.initType();
    this._14 = (short)(seed_800fa754.nextInt(4097));
    this._16 = this.particle.effectInner_08._10;
    this._18 = (short)(seed_800fa754.nextInt(91) + 10);
    this._1a = 120;
    this.particleVelocity_58.x = 0.0f;
    this.particleVelocity_58.y = -seed_800fa754.nextInt(11) - 5;
    this.particleVelocity_58.z = 0.0f;
  }

  @Method(0x800fb9ecL)
  @Override
  protected void beforeRender(final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {
    setParticlePositionAlongVector(this);

    this._14 += this._18;
    this._16 += this._1a;
    this.particleVelocity_58.y -= 2.0f;

    if(this._1a >= 8.0f) {
      this._1a -= 8;
    }
  }
}
