package legend.game.combat.particles;

import legend.core.memory.Method;

import static legend.game.Scus94491BpeSegment.rcos;
import static legend.game.Scus94491BpeSegment.rsin;
import static legend.game.combat.Battle.seed_800fa754;

public class Instance46 extends Instance21 {
  public Instance46(final int index, final ParticleEffectData98 particle) {
    super(index, particle);
  }

  @Method(0x801003e8L)
  @Override
  protected void initType() {
    super.initType();
    final int s4 = this.particle.effectInner_08._10; //TODO read with lw here but as a short everywhere else? Is this a bug?
    final int angle1 = seed_800fa754.nextInt(4097);
    final int angle2 = seed_800fa754.nextInt(2049);
    this.ticksRemaining_12 = (short)(this.particle.effectInner_08.particleInnerStuff_1c >>> 16 & 0xff);
    this.particlePosition_50.x = (rcos(angle1) * rsin(angle2) >> 12) * s4 >> 12;
    this.particlePosition_50.y = rcos(angle2) * s4 >> 12;
    this.particlePosition_50.z = (rsin(angle1) * rsin(angle2) >> 12) * s4 >> 12;
    this.particleVelocity_58.x = rcos(angle1) * rsin(angle2) >> 18;
    this.particleVelocity_58.y = rcos(angle2) >> 6;
    this.particleVelocity_58.z = rsin(angle1) * rsin(angle2) >> 18;
  }
}
