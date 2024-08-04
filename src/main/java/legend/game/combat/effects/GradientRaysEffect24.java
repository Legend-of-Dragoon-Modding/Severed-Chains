package legend.game.combat.effects;

import legend.core.gpu.GpuCommandPoly;
import legend.core.gte.MV;
import legend.core.memory.Method;
import legend.game.scripting.ScriptState;
import legend.game.types.Translucency;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.Arrays;

import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.GTE;
import static legend.game.Scus94491BpeSegment_8003.RotTransPers4;
import static legend.game.Scus94491BpeSegment_800c.worldToScreenMatrix_800c3548;
import static legend.game.combat.SEffe.calculateEffectTransforms;

public class GradientRaysEffect24 implements Effect<EffectManagerParams.VoidType> {
  public final GradientRaysEffectInstance04[] rays_00;
  public final int count_04;
  public int _08;
  public int _0c;
  public int _10;
  public int _14;
  public int flags_18;
  public int type_1c;
  public float projectionPlaneDistanceDiv4_20;

  public GradientRaysEffect24(final int count) {
    this.rays_00 = new GradientRaysEffectInstance04[count];
    this.count_04 = count;

    Arrays.setAll(this.rays_00, i -> new GradientRaysEffectInstance04());
  }

  @Override
  @Method(0x8010ae40L)
  public void tick(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {
    //LAB_8010ae80
    for(int i = 0; i < this.count_04; i++) {
      final GradientRaysEffectInstance04 ray = this.rays_00[i];

      if((this.flags_18 & 0x1) == 0) {
        //LAB_8010aee8
        ray._02 += (short)this._14;

        if((this.flags_18 & 0x2) != 0 && ray._02 >= 0x80) {
          ray._02 = 0x80;
        } else {
          //LAB_8010af28
          //LAB_8010af3c
          ray._02 %= 0x80;
        }
      } else {
        ray._02 -= (short)this._14;

        if((this.flags_18 & 0x2) != 0 && ray._02 <= 0) {
          ray._02 = 0;
        } else {
          //LAB_8010aecc
          ray._02 += 0x80;
          ray._02 %= 0x80;
        }
      }

      //LAB_8010af4c
    }

    //LAB_8010af64
  }

  @Override
  public void render(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {
    final EffectManagerData6c<EffectManagerParams.VoidType> manager = state.innerStruct_00;

    if(manager.params_10.flags_00 >= 0) {
      final GradientRaysEffect24 rayEffect = (GradientRaysEffect24)manager.effect_44;

      //LAB_8010afcc
      for(int i = 0; i < rayEffect.count_04; i++) {
        this.renderGradientRay(manager, rayEffect.rays_00[i]);
      }
    }

    //LAB_8010aff0
  }

  @Override
  public void destroy(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {

  }

  /** Used in Rose transform */
  @Method(0x8010a860L)
  private void renderGradientRay(final EffectManagerData6c<EffectManagerParams.VoidType> manager, final GradientRaysEffectInstance04 gradientRay) {
    final Vector3f sp0x38 = new Vector3f();
    final Vector3f sp0x40 = new Vector3f();
    final Vector3f sp0x48 = new Vector3f();
    final Vector3f sp0x50 = new Vector3f();
    final Vector2f xy0 = new Vector2f();
    final Vector2f xy1 = new Vector2f();
    final Vector2f xy2 = new Vector2f();
    final Vector2f xy3 = new Vector2f();

    final MV sp0x80 = new MV();
    final MV sp0xa0 = new MV();
    final MV sp0xc0 = new MV();

    final GradientRaysEffect24 effect = (GradientRaysEffect24)manager.effect_44;

    //LAB_8010a968
    if((effect.flags_18 & 0x4) == 0) {
      if(effect._10 * 2 < gradientRay._02 * effect._08) {
        sp0x40.y = -effect._10;
        sp0x48.y = -effect._10;
        sp0x50.y = -effect._10 * 2.0f;
      } else {
        //LAB_8010a9ec
        sp0x40.y = gradientRay._02 * -effect._08 / 2.0f;
        sp0x48.y = gradientRay._02 * -effect._08 / 2.0f;
        sp0x50.y = gradientRay._02 * -effect._08;
      }

      //LAB_8010aa34
      sp0x40.z = effect._0c;
      sp0x48.z = -effect._0c;
    }

    //LAB_8010aa54
    final Vector3f translation = new Vector3f(0.0f, gradientRay._02 * effect._08, 0.0f);
    final Vector3f rotation = new Vector3f(gradientRay.angle_00, 0.0f, 0.0f);
    sp0xa0.rotationXYZ(rotation);
    sp0x80.transfer.set(translation);
    sp0x80.compose(sp0xa0, sp0xc0);
    calculateEffectTransforms(sp0x80, manager);

    if((manager.params_10.flags_00 & 0x400_0000) == 0) {
      sp0x80.compose(worldToScreenMatrix_800c3548, sp0xa0);
      sp0xa0.rotationXYZ(manager.params_10.rot_10);
      sp0xc0.compose(sp0xa0, sp0xc0);
      GTE.setTransforms(sp0xc0);
    } else {
      //LAB_8010ab10
      sp0xc0.compose(sp0x80, sp0xa0);
      sp0xa0.compose(worldToScreenMatrix_800c3548, sp0x80);
      GTE.setTransforms(sp0x80);
    }

    //LAB_8010ab34
    final float z = RotTransPers4(sp0x38, sp0x40, sp0x48, sp0x50, xy0, xy1, xy2, xy3);
    if(z >= effect.projectionPlaneDistanceDiv4_20) {
      final GpuCommandPoly cmd = new GpuCommandPoly(4)
        .translucent(Translucency.B_PLUS_F);

      if(effect.type_1c == 1) {
        //LAB_8010abf4
        final int v0 = (0x80 - gradientRay._02) * manager.params_10.colour_1c.x / 0x80;
        final int v1 = (short)v0 / 2;

        cmd
          .monochrome(0, 0)
          .monochrome(1, 0)
          .rgb(2, v0, v1, v1)
          .rgb(3, v0, v1, v1);
      } else if(effect.type_1c == 2) {
        //LAB_8010ac68
        final short s3 = (short)(this.FUN_8010b058(gradientRay._02) * manager.params_10.colour_1c.x * 8 / 0x80);
        final short s2 = (short)(this.FUN_8010b0dc(gradientRay._02) * manager.params_10.colour_1c.y * 8 / 0x80);
        final short a2 = (short)(this.FUN_8010b160(gradientRay._02) * manager.params_10.colour_1c.z * 8 / 0x80);

        cmd
          .monochrome(0, 0)
          .rgb(1, s3 / 2, s2 / 2, a2 / 2)
          .rgb(2, s3 / 2, s2 / 2, a2 / 2)
          .rgb(3, s3, s2, a2);
      }

      //LAB_8010ad68
      //LAB_8010ad6c
      cmd
        .pos(0, xy0.x, xy0.y)
        .pos(1, xy1.x, xy1.y)
        .pos(2, xy2.x, xy2.y)
        .pos(3, xy3.x, xy3.y);

      GPU.queueCommand(z / 4.0f, cmd);
    }

    //LAB_8010ae18
  }

  @Method(0x8010b058L)
  private short FUN_8010b058(final short a0) {
    //LAB_8010b06c
    return (short)switch(a0 / 0x10) {
      case 0, 1, 6 -> 0x10;
      case 2, 7 -> 0x10 - a0 % 0x10;
      case 5 -> a0 % 0x10;
      default -> 0;
    };
  }

  @Method(0x8010b0dcL)
  private short FUN_8010b0dc(final short a0) {
    //LAB_8010b0f0
    return (short)switch(a0 / 0x10) {
      case 0, 4, 5 -> 0x10;
      case 1, 6 -> 0x10 - a0 % 0x10;
      case 3 -> a0 % 0x10;
      default -> 0;
    };
  }

  @Method(0x8010b160L)
  private short FUN_8010b160(final short a0) {
    return (short)switch(a0 / 0x10) {
      case 0, 1, 2, 3 -> 0x10L;
      case 4 -> 0x10 - a0 % 0x10;
      default -> 0;
    };
  }
}
