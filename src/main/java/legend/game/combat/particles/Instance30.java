package legend.game.combat.particles;

import legend.core.memory.Method;
import legend.game.combat.effects.EffectManagerData6c;
import legend.game.combat.effects.EffectManagerParams;
import org.joml.Vector3f;

import static legend.game.combat.Battle.seed_800fa754;

public class Instance30 extends ParticleEffectInstance94 {
  public Instance30(final int index, final ParticleEffectData98 particle) {
    super(index, particle);
  }

  @Method(0x800ffefcL)
  @Override
  protected void initType() {

  }

  @Method(0x80100ea0L)
  @Override
  protected void tickType(final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {
    final int randBound = this.particle.effectInner_08._10 & 0xffff;

    final Vector3f diffTranslation = new Vector3f().set(this.particle.parentBobj_04.getPosition()).sub(manager.getPosition());

    this.particleVelocity_58.x = diffTranslation.x / 8.0f + (seed_800fa754.nextInt(randBound * 2) - randBound >> 4);
    this.particleVelocity_58.y = diffTranslation.y / 8.0f + (seed_800fa754.nextInt(randBound * 2) - randBound >> 4);
    this.particleVelocity_58.z = diffTranslation.z / 8.0f + (seed_800fa754.nextInt(randBound * 2) - randBound >> 4);
    this.particleVelocity_58.x *= this.particle.effectInner_08._18;
    this.particleVelocity_58.y *= this.particle.effectInner_08._18;
    this.particleVelocity_58.z *= this.particle.effectInner_08._18;

    if(this.particleVelocity_58.x == 0) {
      this.particleVelocity_58.x = 1.0f;
    }

    //LAB_80101068
    this.ticksRemaining_12 = (short)(diffTranslation.x / this.particleVelocity_58.x);
  }

  @Override
  protected void beforeRender(final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {

  }
}
