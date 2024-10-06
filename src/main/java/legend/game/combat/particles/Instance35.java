package legend.game.combat.particles;

import legend.core.memory.Method;
import legend.game.combat.effects.EffectManagerData6c;
import legend.game.combat.effects.EffectManagerParams;

public class Instance35 extends ParticleEffectInstance94 {
  public Instance35(final int index, final ParticleEffectData98 particle) {
    super(index, particle);
  }

  @Method(0x801000b8L)
  @Override
  protected void initType() {
    this.particleVelocity_58.x = -this.particlePosition_50.x / 32.0f;
    this.particleVelocity_58.z = -this.particlePosition_50.z / 32.0f;
  }

  @Override
  protected void tickType(final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {

  }

  @Override
  protected void beforeRender(final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {

  }
}
