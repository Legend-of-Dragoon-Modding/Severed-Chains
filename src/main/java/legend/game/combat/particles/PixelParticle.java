package legend.game.combat.particles;

import legend.core.gte.MV;
import legend.game.combat.effects.EffectManagerData6c;
import legend.game.combat.effects.EffectManagerParams;
import legend.game.scripting.ScriptState;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.RENDERER;
import static legend.game.combat.SEffe.FUN_800cfc20;

public class PixelParticle extends ParticleEffectData98 {
  private final MV mv = new MV();

  public PixelParticle(final ParticleManager manager, final int parentScriptIndex, final ParticleEffectData98Inner24 effectInner, final int type, final int count) {
    super(manager, parentScriptIndex, effectInner, type, count);
  }

  @Override
  protected void init(final int flags) {

  }

  @Override
  protected void reinitInstanceType(final EffectManagerData6c<EffectManagerParams.ParticleType> manager, final ParticleEffectInstance94 inst) {

  }

  @Override
  public void render(final ScriptState<EffectManagerData6c<EffectManagerParams.ParticleType>> state) {
    final EffectManagerData6c<EffectManagerParams.ParticleType> manager = state.innerStruct_00;
    final ParticleEffectData98 effect = (ParticleEffectData98)manager.effect_44;
    effect.countFramesRendered_52++;

    //LAB_800fde38
    for(int i = 0; i < effect.countParticleInstance_50; i++) {
      final ParticleEffectInstance94 inst = effect.particleArray_68[i];

      if(inst.tick(manager)) {
        effect.particleInstancePrerenderCallback_84.accept(manager, effect, inst);

        final Vector3f colour = new Vector3f();
        inst.tickAttributes(manager, colour);

        final Vector2f ref = new Vector2f();

        float z = FUN_800cfc20(inst.managerRotation_68, inst.managerTranslation_2c, inst.particlePosition_50, ref) / 4.0f;
        final float zCombined = z + manager.params_10.z_22;
        if(zCombined >= 0xa0) {
          if(zCombined >= 0xffe) {
            z = 0xffe - manager.params_10.z_22;
          }

          //LAB_800fdf44
          this.mv.transfer.set(ref.x + GPU.getOffsetX(), ref.y + GPU.getOffsetY(), z + manager.params_10.z_22);
          RENDERER.queueOrthoModel(RENDERER.opaqueQuad, this.mv)
            .colour(colour);
        }
      }
    }

    //LAB_800fdfe8
    if(effect.scaleOrUseEffectAcceleration_6c) {
      effect.effectAcceleration_70.mul(effect.scaleParticleAcceleration_80);
    }

    //LAB_800fe044
  }
}
