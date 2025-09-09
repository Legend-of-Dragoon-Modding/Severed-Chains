package legend.game.combat.particles;

import legend.core.memory.Method;
import org.joml.Math;

public class Instance59 extends Instance21 {
  public Instance59(final int index, final ParticleEffectData98 particle) {
    super(index, particle);
  }

  @Method(0x80100cacL)
  @Override
  protected void initType() {
    super.initType();
    this.particleVelocity_58.y = -Math.abs(this.particleVelocity_58.y);
    this.particleVelocity_58.x = 0.0f;
    this._14 = 0;
  }
}
