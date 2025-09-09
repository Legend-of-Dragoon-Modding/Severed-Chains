package legend.game.combat.particles;

import legend.game.combat.effects.EffectManagerData6c;
import legend.game.combat.effects.EffectManagerParams;
import legend.game.combat.types.BattleObject;

import javax.annotation.Nullable;

public class JkNotActuallyALineParticle extends LineParticle {
  public JkNotActuallyALineParticle(final ParticleManager manager, @Nullable final BattleObject parentBobj, final ParticleEffectData98Inner24 effectInner, final int type, final int count) {
    super(manager, parentBobj, effectInner, type, count);
  }

  @Override
  protected void renderLineParticles(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleMetrics48 particleMetrics) {

  }
}
