package legend.game.combat.particles;

import legend.core.memory.Method;
import legend.game.combat.effects.EffectManagerData6c;
import legend.game.combat.effects.EffectManagerParams;
import org.joml.Vector3f;

import static legend.game.Scus94491BpeSegment.rcos;
import static legend.game.Scus94491BpeSegment.rsin;
import static legend.game.combat.Battle.seed_800fa754;

public class Instance2 extends ParticleEffectInstance94 {
  public Instance2(final int index, final ParticleEffectData98 particle) {
    super(index, particle);
  }

  @Method(0x800fecccL)
  @Override
  protected void initType() {
    final int angle = seed_800fa754.nextInt(4097);
    this.particleVelocity_58.x = rcos(angle) >> 10;
    this.particleVelocity_58.z = rsin(angle) >> 10;
    this.particleVelocity_58.y = -(seed_800fa754.nextInt(33) + 13);

    this._14 = (short)(seed_800fa754.nextInt(3) + 1);
    this._16 = (short)(seed_800fa754.nextInt(3));

    this.framesUntilRender_04 = (short)(this.framesUntilRender_04 / 4 * 4 + 1);
  }

  @Method(0x80100d60L)
  @Override
  protected void tickType(final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {
    if(this.framesUntilRender_04 == 0 && this.particle.parentBobj_04 != null) {
      final Vector3f diffTranslation = new Vector3f().set(manager.getPosition()).sub(this.particle.parentBobj_04.getPosition());
      this.particlePosition_50.sub(diffTranslation);
    }
  }

  @Method(0x800fb9c8L)
  @Override
  protected void beforeRender(final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {
    this.particleVelocity_58.x -= this._16;
    this.particleVelocity_58.y += this._14;
  }
}
