package legend.game.combat.effects;

import legend.core.MathHelper;
import legend.core.RenderEngine;
import legend.core.gpu.Bpp;
import legend.core.gte.MV;
import legend.core.memory.Method;
import legend.core.opengl.Obj;
import legend.core.opengl.QuadBuilder;
import legend.game.scripting.ScriptState;
import legend.game.types.Translucency;

import java.util.Arrays;

import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.RENDERER;
import static legend.game.Scus94491BpeSegment_8002.rand;

public class StarChildrenMeteorEffect10 implements Effect {
  public final int count_00;
  public final SpriteMetrics08 metrics_04 = new SpriteMetrics08();
  public StarChildrenMeteorEffectInstance10[] meteorArray_0c;

  private Obj obj;
  private final MV transforms = new MV();

  public StarChildrenMeteorEffect10(final int count) {
    this.count_00 = count;
    this.meteorArray_0c = new StarChildrenMeteorEffectInstance10[count];
    Arrays.setAll(this.meteorArray_0c, StarChildrenMeteorEffectInstance10::new);
  }

  @Method(0x8010e2fcL)
  public static void renderStarChildrenMeteorEffect(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state, final EffectManagerData6c<EffectManagerParams.VoidType> manager) {
    final StarChildrenMeteorEffect10 effect = (StarChildrenMeteorEffect10)manager.effect_44;
    final int flags = manager.params_10.flags_00;
    final int tpage = (effect.metrics_04.v_02 & 0x100) >>> 4 | (effect.metrics_04.u_00 & 0x3ff) >>> 6;
    final int vramX = (tpage & 0b1111) * 64;
    final int vramY = (tpage & 0b10000) != 0 ? 256 : 0;
    final int leftU = (effect.metrics_04.u_00 & 0x3f) * 4;
    final int bottomV = effect.metrics_04.v_02;
    final int clutX = effect.metrics_04.clut_06 << 4 & 0x3ff;
    final int clutY = effect.metrics_04.clut_06 >>> 6 & 0x1ff;
    final int r = manager.params_10.colour_1c.x;
    final int g = manager.params_10.colour_1c.y;
    final int b = manager.params_10.colour_1c.z;

    if(effect.obj == null) {
      effect.obj = new QuadBuilder("Star Children meteor")
        .bpp(Bpp.BITS_4)
        .vramPos(vramX, vramY)
        .clut(clutX, clutY)
        .size(effect.metrics_04.w_04, effect.metrics_04.h_05)
        .uv(leftU, bottomV)
        .build();
    }

    //LAB_8010e414
    for(int i = 0; i < effect.count_00; i++) {
      final StarChildrenMeteorEffectInstance10 meteor = effect.meteorArray_0c[i];

      final int w = (int)(meteor.scaleW_0c * effect.metrics_04.w_04);
      final int h = (int)(meteor.scaleH_0e * effect.metrics_04.h_05);
      final int x = meteor.centerOffsetX_02 - w / 2;
      final int y = meteor.centerOffsetY_04 - h / 2;

      effect.transforms.scaling(meteor.scaleW_0c, meteor.scaleH_0e, 1.0f);
      effect.transforms.transfer.set(GPU.getOffsetX() + x, GPU.getOffsetY() + y, 120.0f);
      final RenderEngine.QueuedModel<?> model = RENDERER.queueOrthoModel(effect.obj, effect.transforms)
        .colour(r / 255.0f, g / 255.0f, b / 255.0f);

      if((flags >>> 30 & 1) != 0) {
        model.translucency(Translucency.of(flags >>> 28 & 0x3));
      }
    }
  }

  @Method(0x8010e6b0L)
  public static void tickStarChildrenMeteorEffect(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state, final EffectManagerData6c<EffectManagerParams.VoidType> manager) {
    final StarChildrenMeteorEffect10 meteorEffect = (StarChildrenMeteorEffect10)manager.effect_44;

    //LAB_8010e6ec
    final StarChildrenMeteorEffectInstance10[] meteorArray = meteorEffect.meteorArray_0c;
    for(int i = 0; i < meteorEffect.count_00; i++) {
      final StarChildrenMeteorEffectInstance10 meteor = meteorArray[i];
      meteor.centerOffsetX_02 += (short)(MathHelper.sin(manager.params_10.rot_10.x) * 32 * manager.params_10.scale_16.x * meteor.scale_0a);
      meteor.centerOffsetY_04 += (short)(MathHelper.cos(manager.params_10.rot_10.x) * 32 * manager.params_10.scale_16.x * meteor.scale_0a);

      if(meteor.scale_0a * 120 + 50 < meteor.centerOffsetY_04) {
        meteor.centerOffsetY_04 = -120;
        meteor.centerOffsetX_02 = rand() % 321 - 160;
      }

      //LAB_8010e828
      final int centerOffsetX = meteor.centerOffsetX_02;
      if(centerOffsetX > 160) {
        meteor.centerOffsetX_02 = -160;
        //LAB_8010e848
      } else if(centerOffsetX < -160) {
        //LAB_8010e854
        meteor.centerOffsetX_02 = 160;
      }
      //LAB_8010e860
    }
    //LAB_8010e87c
  }

  public static void destructor(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state, final EffectManagerData6c<EffectManagerParams.VoidType> manager) {
    final StarChildrenMeteorEffect10 effect = (StarChildrenMeteorEffect10)manager.effect_44;

    if(effect.obj != null) {
      effect.obj.delete();
      effect.obj = null;
    }
  }
}
