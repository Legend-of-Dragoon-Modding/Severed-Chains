package legend.game.combat.particles;

import legend.core.memory.Method;
import legend.game.combat.bent.BattleEntity27c;
import legend.game.combat.effects.EffectManagerData6c;
import legend.game.combat.effects.EffectManagerParams;

import static legend.game.combat.SEffe.calculateBentPartPosition;

public class Instance64 extends ParticleEffectInstance94 {
  public Instance64(final int index, final ParticleEffectData98 particle) {
    super(index, particle);
  }

  @Method(0x80100d00L)
  @Override
  protected void initType() {
    calculateBentPartPosition((BattleEntity27c)this.particle.parentBobj_04, this.particlePosition_50, this.index);
  }

  @Override
  protected void tickType(final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {

  }

  @Override
  protected void beforeRender(final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {

  }
}
