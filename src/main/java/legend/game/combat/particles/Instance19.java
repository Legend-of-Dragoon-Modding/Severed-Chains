package legend.game.combat.particles;

import legend.game.combat.effects.EffectManagerData6c;
import legend.game.combat.effects.EffectManagerParams;

public class Instance19 extends ParticleEffectInstance94 {
  public Instance19(final int index, final ParticleEffectData98 particle) {
    super(index, particle);
  }

  @Override
  protected void initType() {

  }

  @Override
  protected void tickType(final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {
    if(this.framesUntilRender_04 == 0) {
      this.stepR_8a = 0;
      this.stepG_8c = 0;
      this.stepB_8e = 0;
    }
  }

  @Override
  protected void beforeRender(final EffectManagerData6c<EffectManagerParams.ParticleType> manager) {

  }
}
