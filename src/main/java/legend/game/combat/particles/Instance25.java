package legend.game.combat.particles;

import legend.core.memory.Method;
import legend.game.combat.effects.EffectManagerData6c;
import legend.game.combat.effects.EffectManagerParams;
import org.joml.Vector3f;

import static legend.game.combat.Battle.seed_800fa754;

public class Instance25 extends ParticleEffectInstance94 {
  public Instance25(final int index, final ParticleEffectData98 particle) {
    super(index, particle);
  }

  @Method(0x800ffbd8L)
  @Override
  protected void initType() {
    this._14 = 0;
    this._18 = (short)(seed_800fa754.nextInt(21) - 10);
    this._1a = (short)(seed_800fa754.nextInt(21) - 10);
    this._1c = (short)(seed_800fa754.nextInt(81) - 40);
    this._1e = this.ticksRemaining_12;
    this.particleVelocity_58.x = (seed_800fa754.nextInt(41) + 44) * this.particle.effectInner_08._18;
    this.particleVelocity_58.y = (seed_800fa754.nextInt(81) - 40) * this.particle.effectInner_08._18;
    this.particleVelocity_58.z = (seed_800fa754.nextInt(41) + 44) * this.particle.effectInner_08._18;
    this.ticksRemaining_12 += 20;
  }

  @Override
  protected void tickType(final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {

  }

  @Method(0x800fc0d0L)
  @Override
  protected void beforeRender(final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {
    if(this.particlePosition_50.y + this.managerTranslation_2c.y >= -400 && this._14 == 0) {
      this._14 = 1;
      this.particleAcceleration_60.y = -8.0f;
      //LAB_800fc11c
    } else if(this.particle.parentBobj_04 != null && this._14 == 0) {
      final Vector3f parentPos = this.particle.parentBobj_04.getPosition();
      this.particleVelocity_58.x = (parentPos.x - (this.originalParticlePosition_3c.x + this.managerTranslation_2c.x)) / this._1e;
      this.particleVelocity_58.y = (parentPos.y - (this.originalParticlePosition_3c.y + this.managerTranslation_2c.y)) / this._1e;
      this.particleVelocity_58.z = (parentPos.z - (this.originalParticlePosition_3c.z + this.managerTranslation_2c.z)) / this._1e;
      this.particleVelocity_58.x += this._18;
      this.particleVelocity_58.y += this._1a;
      this.particleVelocity_58.z += this._1c;
    }

    //LAB_800fc1ec
  }
}
