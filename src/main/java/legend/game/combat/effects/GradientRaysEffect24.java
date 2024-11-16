package legend.game.combat.effects;

import legend.core.QueuedModelStandard;
import legend.core.gte.MV;
import legend.core.memory.Method;
import legend.core.opengl.Obj;
import legend.core.opengl.PolyBuilder;
import legend.game.scripting.ScriptState;
import legend.game.types.Translucency;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.Arrays;

import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.GTE;
import static legend.core.GameEngine.RENDERER;
import static legend.game.Scus94491BpeSegment_8003.RotTransPers4;
import static legend.game.Scus94491BpeSegment_800c.worldToScreenMatrix_800c3548;
import static legend.game.combat.SEffe.calculateEffectTransforms;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_STRIP;

public class GradientRaysEffect24 implements Effect<EffectManagerParams.VoidType> {
  public final GradientRaysEffectInstance04[] rays_00;
  public final int count_04;
  /** Y values when rays are in the center cluster of the effect */
  public int yInner_08;
  /** Z value of the two edge vertices of the rays */
  public int midVertZ_0c;
  /** Y values when rays are in the outer part of the effect */
  public int yOuter_10;
  public int stepVertColourAndYModifier_14;
  public int flags_18;
  public int type_1c;
  public float projectionPlaneDistanceDiv4_20;

  final MV transforms = new MV();

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
        ray.vertColourAndYModifier_02 += (short)this.stepVertColourAndYModifier_14;

        if((this.flags_18 & 0x2) != 0 && ray.vertColourAndYModifier_02 >= 0x80) {
          ray.vertColourAndYModifier_02 = 0x80;
        } else {
          //LAB_8010af28
          //LAB_8010af3c
          ray.vertColourAndYModifier_02 %= 0x80;
        }
      } else {
        ray.vertColourAndYModifier_02 -= (short)this.stepVertColourAndYModifier_14;

        if((this.flags_18 & 0x2) != 0 && ray.vertColourAndYModifier_02 <= 0) {
          ray.vertColourAndYModifier_02 = 0;
        } else {
          //LAB_8010aecc
          ray.vertColourAndYModifier_02 += 0x80;
          ray.vertColourAndYModifier_02 %= 0x80;
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
    final Vector3f vert0 = new Vector3f();
    final Vector3f vert1 = new Vector3f();
    final Vector3f vert2 = new Vector3f();
    final Vector3f vert3 = new Vector3f();
    final Vector2f xy0 = new Vector2f();
    final Vector2f xy1 = new Vector2f();
    final Vector2f xy2 = new Vector2f();
    final Vector2f xy3 = new Vector2f();

    final GradientRaysEffect24 effect = (GradientRaysEffect24)manager.effect_44;

    //LAB_8010a968
    if((effect.flags_18 & 0x4) == 0) {
      if(effect.yOuter_10 * 2 < gradientRay.vertColourAndYModifier_02 * effect.yInner_08) {
        vert1.y = -effect.yOuter_10;
        vert2.y = -effect.yOuter_10;
        vert3.y = -effect.yOuter_10 * 2.0f;
      } else {
        //LAB_8010a9ec
        vert1.y = gradientRay.vertColourAndYModifier_02 * -effect.yInner_08 / 2.0f;
        vert2.y = gradientRay.vertColourAndYModifier_02 * -effect.yInner_08 / 2.0f;
        vert3.y = gradientRay.vertColourAndYModifier_02 * -effect.yInner_08;
      }

      //LAB_8010aa34
      vert1.z = effect.midVertZ_0c;
      vert2.z = -effect.midVertZ_0c;
    }

    //LAB_8010aa54
    final MV rotationMatrix = new MV();
    final MV translationMatrix = new MV();
    final MV effectTransforms = new MV();
    final MV compositionMatrix = new MV();
    final MV worldMatrix = new MV();
    final MV transformMatrix = new MV();

    final Vector3f translation = new Vector3f(0.0f, gradientRay.vertColourAndYModifier_02 * effect.yInner_08, 0.0f);
    final Vector3f rotation = new Vector3f(gradientRay.angle_00, 0.0f, 0.0f);
    rotationMatrix.rotationXYZ(rotation);
    translationMatrix.transfer.set(translation);
    translationMatrix.compose(rotationMatrix, compositionMatrix);
    calculateEffectTransforms(effectTransforms, manager);

    if((manager.params_10.flags_00 & 0x400_0000) == 0) {
      rotationMatrix.rotationXYZ(manager.params_10.rot_10);
      effectTransforms.compose(rotationMatrix, transformMatrix);
      GTE.setTransforms(transformMatrix);
    } else {
      //LAB_8010ab10
      compositionMatrix.compose(effectTransforms, worldMatrix);
      worldMatrix.compose(worldToScreenMatrix_800c3548, transformMatrix);
      GTE.setTransforms(transformMatrix);
    }

    //LAB_8010ab34
    final float z = RotTransPers4(vert0, vert1, vert2, vert3, xy0, xy1, xy2, xy3);
    if(z >= effect.projectionPlaneDistanceDiv4_20) {
      final float r, g, b;
      if(effect.type_1c == 1) {
        //LAB_8010abf4
        r = ((128.0f - gradientRay.vertColourAndYModifier_02) * manager.params_10.colour_1c.x / 128.0f) / 256.0f;
        g = r / 2.0f;
        b = 0.0f;
      } else if(effect.type_1c == 2) {
        //LAB_8010ac68
        r = (this.getModifierR(gradientRay.vertColourAndYModifier_02) * manager.params_10.colour_1c.x * 8 / 128.0f) / 256.0f;
        g = (this.getModifierG(gradientRay.vertColourAndYModifier_02) * manager.params_10.colour_1c.y * 8 / 128.0f) / 256.0f;
        b = (this.getModifierB(gradientRay.vertColourAndYModifier_02) * manager.params_10.colour_1c.z * 8 / 128.0f) / 256.0f;
      } else {
        r = g = b = 0.0f;
      }

      //LAB_8010ad68
      //LAB_8010ad6c
      final PolyBuilder builder = new PolyBuilder("GradientRay", GL_TRIANGLE_STRIP)
        .translucency(Translucency.B_PLUS_F)
        .addVertex(xy0.x, xy0.y, 0)
        .monochrome(0);

      if(effect.type_1c == 1) {
        builder
          .addVertex(xy1.x, xy1.y, 0)
          .monochrome(0)
          .addVertex(xy2.x, xy2.y, 0)
          .rgb(r, g, g)
          .addVertex(xy3.x, xy3.y, 0)
          .rgb(r, g, g);
      } else if(effect.type_1c == 2) {
        builder
          .addVertex(xy1.x, xy1.y, 0)
          .rgb(r / 2, g / 2, b / 2)
          .addVertex(xy2.x, xy2.y, 0)
          .rgb(r / 2, g / 2, b / 2)
          .addVertex(xy3.x, xy3.y, 0)
          .rgb(r, g, b);
      } else {
        // I don't think there is another type in the scripts, but just to be sure.
        builder
          .addVertex(xy1.x, xy1.y, 0)
          .monochrome(0)
          .addVertex(xy2.x, xy2.y, 0)
          .monochrome(0)
          .addVertex(xy3.x, xy3.y, 0)
          .monochrome(0);
      }

      final Obj obj = builder.build();
      obj.delete();

      this.transforms.transfer.set(GPU.getOffsetX(), GPU.getOffsetY(), z * 4);
      RENDERER.queueOrthoModel(obj, this.transforms, QueuedModelStandard.class);
    }
    //LAB_8010ae18
  }

  @Method(0x8010b058L)
  private int getModifierR(final int inModifier) {
    //LAB_8010b06c
    return switch(inModifier / 0x10) {
      case 0, 1, 6 -> 0x10;
      case 2, 7 -> 0x10 - inModifier % 0x10;
      case 5 -> inModifier % 0x10;
      default -> 0;
    };
  }

  @Method(0x8010b0dcL)
  private int getModifierG(final int inModifier) {
    //LAB_8010b0f0
    return (short)switch(inModifier / 0x10) {
      case 0, 4, 5 -> 0x10;
      case 1, 6 -> 0x10 - inModifier % 0x10;
      case 3 -> inModifier % 0x10;
      default -> 0;
    };
  }

  @Method(0x8010b160L)
  private int getModifierB(final int inModifier) {
    return (short)switch(inModifier / 0x10) {
      case 0, 1, 2, 3 -> 0x10;
      case 4 -> 0x10 - inModifier % 0x10;
      default -> 0;
    };
  }
}
