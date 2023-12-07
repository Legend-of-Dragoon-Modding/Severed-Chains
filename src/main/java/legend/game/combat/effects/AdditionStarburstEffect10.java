package legend.game.combat.effects;

import legend.core.MathHelper;
import legend.core.gpu.GpuCommandPoly;
import legend.core.memory.Method;
import legend.game.scripting.ScriptState;
import legend.game.types.Translucency;
import org.joml.Math;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.Arrays;
import java.util.function.BiConsumer;

import static legend.core.GameEngine.GPU;
import static legend.game.combat.Bttl_800c.completedAdditionStarburstAngleModifiers_800c6dac;
import static legend.game.combat.Bttl_800c.completedAdditionStarburstTranslationMagnitudes_800c6d94;
import static legend.game.combat.Bttl_800c.scriptGetScriptedObjectPos;
import static legend.game.combat.Bttl_800c.transformWorldspaceToScreenspace;

public class AdditionStarburstEffect10 implements Effect {
  public int parentIndex_00;
  /** ushort */
  public int rayCount_04;

  /** Set to 0 and never used */
  public int unused_08;
  public AdditionStarburstEffectRay10[] rayArray_0c;

  /**
   * <ol start="0">
   *   <li>{@link #renderAdditionHitStarburst}</li>
   *   <li>{@link #renderAdditionCompletedStarburst}</li>
   *   <li>{@link #renderAdditionCompletedStarburst}</li>
   * </ol>
   */
  public final BiConsumer<ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>>, EffectManagerData6c<EffectManagerParams.VoidType>>[] additionStarburstRenderers_800c6dc4 = new BiConsumer[3];
  {
    this.additionStarburstRenderers_800c6dc4[0] = this::renderAdditionHitStarburst;
    this.additionStarburstRenderers_800c6dc4[1] = this::renderAdditionCompletedStarburst;
    this.additionStarburstRenderers_800c6dc4[2] = this::renderAdditionCompletedStarburst;
  }

  public AdditionStarburstEffect10(final int rayCount) {
    this.rayCount_04 = rayCount;
    this.rayArray_0c = new AdditionStarburstEffectRay10[rayCount];
    Arrays.setAll(this.rayArray_0c, AdditionStarburstEffectRay10::new);
  }

  /** If a secondary script is specified, modifies the translations of the starburst rays by the secondary script's translation. */
  @Method(0x800d1194L)
  private void modifyAdditionStarburstTranslation(final EffectManagerData6c<EffectManagerParams.VoidType> manager, final AdditionStarburstEffect10 starburstEffect, final Vector2f outTranslation) {
    if(starburstEffect.parentIndex_00 == -1) {
      outTranslation.zero();
    } else {
      //LAB_800d11c4
      final Vector3f scriptTranslation = new Vector3f();
      scriptGetScriptedObjectPos(starburstEffect.parentIndex_00, scriptTranslation);
      scriptTranslation.add(manager.params_10.trans_04);
      transformWorldspaceToScreenspace(scriptTranslation, outTranslation);
    }

    //LAB_800d120c
  }

  @Method(0x800d1220L)
  public void renderAdditionHitStarburst(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state, final EffectManagerData6c<EffectManagerParams.VoidType> manager) {
    final float[] baseAngle = {MathHelper.psxDegToRad(-16), MathHelper.psxDegToRad(16)};
    final AdditionStarburstEffect10 starburstEffect = (AdditionStarburstEffect10)manager.effect_44;

    //LAB_800d128c
    for(int rayNum = 0; rayNum < starburstEffect.rayCount_04; rayNum++) {
      final AdditionStarburstEffectRay10 ray = starburstEffect.rayArray_0c[rayNum];

      if(ray.renderRay_00) {
        //LAB_800d12a4
        for(int i = 0; i < 2; i++) {
          float angleModifier = baseAngle[i] + ray.angleModifier_0a;
          int translationScale = 30 + ray.endpointTranslationMagnitude_06;

          float angle = ray.angle_02 + angleModifier;
          float sin = MathHelper.sin(angle);
          float cos = MathHelper.cosFromSin(sin, angle);
          float x2 = cos * translationScale;
          float y2 = sin * translationScale;

          float sin2 = MathHelper.sin(ray.angle_02);
          float cos2 = MathHelper.cosFromSin(sin2, ray.angle_02);
          float x3 = cos2 * translationScale;
          float y3 = sin2 * translationScale;

          angleModifier = baseAngle[i] + ray.angleModifier_0a;
          translationScale = 210 + ray.endpointTranslationMagnitude_06;

          angle = ray.angle_02 + angleModifier;
          sin = MathHelper.sin(angle);
          cos = MathHelper.cosFromSin(sin, angle);
          final float x0 = cos * translationScale;
          final float y0 = sin * translationScale;

          sin2 = MathHelper.sin(ray.angle_02);
          cos2 = MathHelper.cosFromSin(sin2, ray.angle_02);
          final float x1 = cos2 * translationScale;
          final float y1 = sin2 * translationScale;

          final Vector2f translation = new Vector2f();
          this.modifyAdditionStarburstTranslation(manager, starburstEffect, translation);
          x2 += translation.x;
          y2 += translation.y;
          x3 += translation.x;
          y3 += translation.y;

          GPU.queueCommand(30, new GpuCommandPoly(4)
            .translucent(Translucency.B_PLUS_F)
            .monochrome(0, 0)
            .rgb(1, manager.params_10.colour_1c)
            .monochrome(2, 0)
            .rgb(3, 0)
            .pos(0, x0, y0)
            .pos(1, x1, y1)
            .pos(2, x2, y2)
            .pos(3, x3, y3)
          );
        }
      }
      //LAB_800d1538
    }
    //LAB_800d1558
  }

  @Method(0x800d15d8L)
  public void renderAdditionCompletedStarburst(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state, final EffectManagerData6c<EffectManagerParams.VoidType> manager) {
    final AdditionStarburstEffect10 starburstEffect = (AdditionStarburstEffect10)manager.effect_44;

    final float[] xArray = new float[3];
    final float[] yArray = new float[3];

    //LAB_800d16fc
    for(int rayNum = 0; rayNum < starburstEffect.rayCount_04; rayNum++) {
      final AdditionStarburstEffectRay10 ray = starburstEffect.rayArray_0c[rayNum];

      if(ray.renderRay_00) {
        ray.endpointTranslationMagnitude_06 += ray.endpointTranslationMagnitudeVelocity_08;

        //LAB_800d1728
        for(int i = 0; i < 4; i++) {
          final Vector2f translation = new Vector2f();
          this.modifyAdditionStarburstTranslation(manager, starburstEffect, translation);

          //LAB_800d174c
          for(int j = 0; j < 3; j++) {
            final int translationScale = Math.max(0, completedAdditionStarburstTranslationMagnitudes_800c6d94[i].get(j) - ray.endpointTranslationMagnitude_06);

            //LAB_800d1784
            final float angle = ray.angle_02 + completedAdditionStarburstAngleModifiers_800c6dac[i].get(j);
            final float sin = MathHelper.sin(angle);
            final float cos = MathHelper.cosFromSin(sin, angle);
            xArray[j] = cos * translationScale + translation.x;
            yArray[j] = sin * translationScale + translation.y;
          }

          GPU.queueCommand(30, new GpuCommandPoly(3)
            .translucent(Translucency.B_PLUS_F)
            .monochrome(0, 0)
            .monochrome(1, 0)
            .rgb(2, manager.params_10.colour_1c)
            .pos(0, xArray[0], yArray[0])
            .pos(1, xArray[1], yArray[1])
            .pos(2, xArray[2], yArray[2])
          );
        }
      }
      //LAB_800d190c
    }
    //LAB_800d1940
  }
}
