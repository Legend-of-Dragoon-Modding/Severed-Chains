package legend.game.combat.particles;

import legend.core.memory.Method;
import legend.game.combat.effects.EffectManagerData6c;
import legend.game.combat.effects.EffectManagerParams;
import org.joml.Math;

public class Instance44 extends Instance21 {
  public Instance44(final int index, final ParticleEffectData98 particle) {
    super(index, particle);
  }

  @Method(0x80100364L)
  @Override
  protected void initType() {
    super.initType();
    this._14 = 0;
    this.particleVelocity_58.y = -Math.abs(this.particleVelocity_58.y);
    this.particleAcceleration_60.set(this.particleVelocity_58).negate().div(this.ticksRemaining_12);
  }

  @Method(0x800fc410L)
  @Override
  protected void beforeRender(final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {
    this.particleVelocity_58.y += this._14 / (float)0x100;
  }
}
