package legend.game.combat.particles;

import legend.core.memory.Method;
import legend.game.combat.effects.EffectManagerData6c;
import legend.game.combat.effects.EffectManagerParams;

import static legend.game.Scus94491BpeSegment.rcos;
import static legend.game.Scus94491BpeSegment.rsin;
import static legend.game.combat.Battle.seed_800fa754;

public class Instance21 extends ParticleEffectInstance94 {
  public Instance21(final int index, final ParticleEffectData98 particle) {
    super(index, particle);
  }

  @Method(0x800ff890L)
  @Override
  protected void initType() {
    final int angle1 = seed_800fa754.nextInt(4097);
    final int angle2 = seed_800fa754.nextInt(2049);

    this.particleVelocity_58.x = rcos(angle1) * rsin(angle2) / 0x40 >> 12;
    this.particleVelocity_58.y = rcos(angle2) / 0x40;
    this.particleVelocity_58.z = rsin(angle1) * rsin(angle2) / 0x40 >> 12;

    this.ticksRemaining_12 += (short)(seed_800fa754.nextInt(21) - 10);

    if(this.ticksRemaining_12 <= 0) {
      this.ticksRemaining_12 = 1;
    }

    //LAB_800ffa60
  }

  @Override
  protected void tickType(final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {

  }

  @Override
  protected void beforeRender(final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {

  }
}
