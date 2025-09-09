package legend.game.combat.particles;

import legend.core.memory.Method;

public class Instance54 extends Instance21 {
  public Instance54(final int index, final ParticleEffectData98 particle) {
    super(index, particle);
  }

  @Method(0x80100af4L)
  @Override
  protected void initType() {
    super.initType();
    this.particleVelocity_58.x = this.particleVelocity_58.x * this.particle.effectInner_08._18;
    this.particleVelocity_58.y = this.particleVelocity_58.y * this.particle.effectInner_08._18;
    this.particleVelocity_58.z = this.particleVelocity_58.z * this.particle.effectInner_08._18;
    this.particleAcceleration_60.set(this.particleVelocity_58).negate().div(this.ticksRemaining_12);
  }
}
