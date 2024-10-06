package legend.game.combat.particles;

import legend.core.memory.Method;
import legend.game.combat.effects.EffectManagerData6c;
import legend.game.combat.effects.EffectManagerParams;

import static legend.game.Scus94491BpeSegment.rcos;
import static legend.game.Scus94491BpeSegment.rsin;
import static legend.game.combat.Battle.seed_800fa754;

public class Instance3 extends ParticleEffectInstance94 {
  public Instance3(final int index, final ParticleEffectData98 particle) {
    super(index, particle);
  }

  @Method(0x800fea70L)
  @Override
  protected void initType() {
    final int angle = seed_800fa754.nextInt(4097);
    this.particleVelocity_58.x = rcos(angle) / (float)0x80;
    this.particleVelocity_58.z = rsin(angle) / (float)0x80;
    this.particleVelocity_58.y = 0.0f;
    this.framesUntilRender_04 = 1;
    this._14 = (short)(seed_800fa754.nextInt(6));
  }

  @Override
  protected void tickType(final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {

  }

  @Override
  protected void beforeRender(final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {

  }
}
