package legend.game.combat.particles;

import legend.core.memory.Method;

public class Instance9 extends Instance8 {
  public Instance9(final int index, final ParticleEffectData98 particle) {
    super(index, particle);
  }

  @Method(0x800ff590L)
  @Override
  protected void initType() {
    super.initType();
    this.ticksUntilMovementModeChanges_22 = 20;
    this.verticalPositionScale_20 = 10;
  }
}
