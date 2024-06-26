package legend.game.combat.effects;

import legend.core.memory.Method;
import legend.game.scripting.ScriptState;
import org.joml.Vector3f;

import java.util.Arrays;

import static legend.game.combat.Battle.seed_800fa754;

public class MoonlightStarsEffect18 implements Effect<EffectManagerParams.VoidType> {
  /** ushort */
  public int count_00;

  public final SpriteMetrics08 metrics_04 = new SpriteMetrics08();
  public MoonlightStarsEffectInstance3c[] starArray_0c;

  public MoonlightStarsEffect18(final int count) {
    this.count_00 = count;
    this.starArray_0c = new MoonlightStarsEffectInstance3c[count];
    Arrays.setAll(this.starArray_0c, MoonlightStarsEffectInstance3c::new);
  }

  @Override
  @Method(0x8010ff10L)
  public void tick(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {
    //LAB_8010ff34
    for(int i = 0; i < this.count_00; i++) {
      final MoonlightStarsEffectInstance3c star = this.starArray_0c[i];

      // Seems like stars stop rendering when the current frame exceeds a randomized threshold.
      // The threshold is then re-randomized each tick until the current frame falls below the
      // threshold again, and then the star is rendered again.
      star.currentFrame_00++;
      if(star.currentFrame_00 > star.toggleOffFrameThreshold_38) {
        star.renderStars_03 = false;
        star.currentFrame_00 = 0;
        star.toggleOffFrameThreshold_38 = (short)seed_800fa754.nextInt(star.maxToggleFrameThreshold_36 + 1);
      } else {
        //LAB_8010ffb0
        star.renderStars_03 = true;
      }
    }
  }

  @Override
  @Method(0x8010ec08L)
  public void render(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {
    final EffectManagerData6c<EffectManagerParams.VoidType> manager = state.innerStruct_00;
    final GenericSpriteEffect24 spriteEffect = new GenericSpriteEffect24(manager.params_10.flags_00, -this.metrics_04.w_04 / 2, -this.metrics_04.h_05 / 2, this.metrics_04.w_04, this.metrics_04.h_05, (this.metrics_04.v_02 & 0x100) >>> 4 | (this.metrics_04.u_00 & 0x3ff) >>> 6, this.metrics_04.clut_06 << 4 & 0x3ff, this.metrics_04.clut_06 >>> 6 & 0x1ff, (this.metrics_04.u_00 & 0x3f) << 2, this.metrics_04.v_02);

    final Vector3f translation = new Vector3f();

    //LAB_8010ed00
    for(int i = 0; i < this.count_00; i++) {
      final MoonlightStarsEffectInstance3c star = this.starArray_0c[i];

      // If a star is set not to render, do not render subsequent stars either.
      if(!star.renderStars_03) {
        break;
      }

      spriteEffect.r_14 = manager.params_10.colour_1c.x;
      spriteEffect.g_15 = manager.params_10.colour_1c.y;
      spriteEffect.b_16 = manager.params_10.colour_1c.z;
      spriteEffect.scaleX_1c = manager.params_10.scale_16.x;
      spriteEffect.scaleY_1e = manager.params_10.scale_16.y;
      spriteEffect.angle_20 = manager.params_10.rot_10.x;
      translation.set(manager.params_10.trans_04).add(star.translation_04);
      spriteEffect.render(translation);
      spriteEffect.delete();
    }

    //LAB_8010edac
  }

  @Override
  public void destroy(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {

  }
}
