package legend.game.combat.particles;

import legend.core.memory.Method;
import legend.game.combat.effects.EffectManagerData6c;
import legend.game.combat.effects.EffectManagerParams;
import org.joml.Math;

public class Instance39 extends Instance21 {
  public Instance39(final int index, final ParticleEffectData98 particle) {
    super(index, particle);
  }

  @Method(0x801000f8L)
  @Override
  protected void initType() {
    super.initType();
    this.particleVelocity_58.y = -Math.abs(this.particleVelocity_58.y);
    this._14 = (short)(this.particle.effectInner_08._18 * 0x300);
  }

  @Method(0x800fc410L)
  @Override
  protected void beforeRender(final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {
    this.particleVelocity_58.y += this._14 / (float)0x100;
  }
}
