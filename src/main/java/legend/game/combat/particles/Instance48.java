package legend.game.combat.particles;

import legend.core.memory.Method;
import legend.game.combat.effects.EffectManagerData6c;
import legend.game.combat.effects.EffectManagerParams;

public class Instance48 extends ParticleEffectInstance94 {
  public Instance48(final int index, final ParticleEffectData98 particle) {
    super(index, particle);
  }

  @Method(0x801007b4L)
  @Override
  protected void initType() {
    this.particleVelocity_58.set(this.particlePosition_50).negate().div(this.ticksRemaining_12);
  }

  @Override
  protected void tickType(final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {

  }

  @Override
  protected void beforeRender(final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {

  }
}
