package legend.game.combat.particles;

import legend.game.combat.effects.EffectManagerData6c;
import legend.game.combat.effects.EffectManagerParams;
import legend.game.combat.types.BattleObject;
import legend.game.scripting.ScriptState;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.Arrays;

public class WhyIsThereANoParticle extends ParticleEffectData98 {
  public WhyIsThereANoParticle(final ParticleManager manager, @Nullable final BattleObject parentBobj, final ParticleEffectData98Inner24 effectInner, final int type, final int count) {
    super(manager, parentBobj, effectInner, type, count);
  }

  @Override
  protected void init(final int flags) {
    this.countParticleSub_54 = (short)flags;

    //LAB_80101cb0
    for(int i = 0; i < this.countParticleInstance_50; i++) {
      final ParticleEffectInstance94 inst = this.particleArray_68[i];
      inst.subParticlePositionsArray_44 = new Vector3f[this.countParticleSub_54];
      Arrays.setAll(inst.subParticlePositionsArray_44, n -> new Vector3f().set(inst.particlePosition_50));
    }
  }

  @Override
  protected void reinitInstanceType(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectInstance94 inst) {
    //LAB_80101160
    //LAB_80101170
    for(int i = 0; i < this.countParticleSub_54; i++) {
      inst.subParticlePositionsArray_44[i].set(inst.particlePosition_50);
    }
  }

  @Override
  public void render(final ScriptState<EffectManagerData6c<EffectManagerParams.ParticleType>> state) {

  }
}
