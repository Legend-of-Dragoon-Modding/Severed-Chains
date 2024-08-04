package legend.game.combat.effects;

import legend.core.MathHelper;
import legend.core.memory.Method;
import legend.game.scripting.ScriptState;
import org.joml.Vector3f;

import java.util.Arrays;
import java.util.function.BiConsumer;

public class WsDragoonTransformationFeathersEffect14 implements Effect<EffectManagerParams.VoidType> {
  /** ushort */
  public final int count_00;
  /** ushort */
  public int unused_02;

  public short u_06;
  public short v_08;
  public short width_0a;
  public short height_0c;
  public short clut_0e;
  public WsDragoonTransformationFeatherInstance70[] featherArray_10;

  public WsDragoonTransformationFeathersEffect14(final int count) {
    this.count_00 = count;
    this.featherArray_10 = new WsDragoonTransformationFeatherInstance70[count];
    Arrays.setAll(this.featherArray_10, WsDragoonTransformationFeatherInstance70::new);
  }

  @Override
  @Method(0x8010f978L)
  public void tick(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {
    final EffectManagerData6c<EffectManagerParams.VoidType> manager = state.innerStruct_00;

    //LAB_8010f9c0
    for(int i = 0; i < this.count_00; i++) {
      final WsDragoonTransformationFeatherInstance70 feather = this.featherArray_10[i];
      this.WsDragoonTransformationFeatherCallbacks_80119ff4[feather.callbackIndex_02].accept(manager, feather);
    }

    //LAB_8010f9fc
  }

  @Override
  @Method(0x8010d5b4L)
  public void render(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {
    final EffectManagerData6c<EffectManagerParams.VoidType> manager = state.innerStruct_00;
    final GenericSpriteEffect24 spriteEffect = new GenericSpriteEffect24(manager.params_10.flags_00, -this.width_0a / 2, -this.height_0c / 2, this.width_0a, this.height_0c, (this.v_08 & 0x100) >>> 4 | (this.u_06 & 0x3ff) >>> 6, this.clut_0e << 4 & 0x3ff, this.clut_0e >>> 6 & 0x1ff, (this.u_06 & 0x3f) * 4, this.v_08);

    final Vector3f translation = new Vector3f();

    //LAB_8010d6c8
    for(int i = 0; i < this.count_00; i++) {
      final WsDragoonTransformationFeatherInstance70 feather = this.featherArray_10[i];

      if(feather.renderFeather_00) {
        if(feather.r_38 == 0x7f && feather.g_39 == 0x7f && feather.b_3a == 0x7f) {
          spriteEffect.r_14 = manager.params_10.colour_1c.x;
          spriteEffect.g_15 = manager.params_10.colour_1c.y;
          spriteEffect.b_16 = manager.params_10.colour_1c.z;
        } else {
          //LAB_8010d718
          spriteEffect.r_14 = feather.r_38;
          spriteEffect.g_15 = feather.g_39;
          spriteEffect.b_16 = feather.b_3a;
        }

        //LAB_8010d73c
        spriteEffect.scaleX_1c = manager.params_10.scale_16.x;
        spriteEffect.scaleY_1e = manager.params_10.scale_16.y;
        spriteEffect.angle_20 = feather.spriteAngle_6e;
        translation.set(feather.translation_08).add(manager.params_10.trans_04);
        spriteEffect.render(translation);
        spriteEffect.delete();
      }
    }
  }

  @Override
  public void destroy(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {

  }

  @Method(0x8010fa4cL)
  private void initializeWsDragoonTransformationEffect(final EffectManagerData6c<EffectManagerParams.VoidType> manager, final WsDragoonTransformationFeatherInstance70 feather) {
    feather.currentFrame_04++;

    if(feather.currentFrame_04 >= feather.countCallback0Frames_64) {
      feather.renderFeather_00 = true;
      feather.currentFrame_04 = 0;
      feather.callbackIndex_02++;
    }
  }

  @Method(0x8010fa88L)
  private void expandWsDragoonTransformationEffect(final EffectManagerData6c<EffectManagerParams.VoidType> manager, final WsDragoonTransformationFeatherInstance70 feather) {
    feather.angle_58 += feather.angleStep_60;
    feather.angle_58 %= MathHelper.TWO_PI;

    feather.velocityTranslationMagnitudeXz_40 += feather.accelerationTranslationMagnitudeXz_44;
    feather.translationMagnitudeXz_3c += feather.velocityTranslationMagnitudeXz_40;

    final int x = (int)((MathHelper.cos(feather.angle_58 + feather.angleNoiseXz_5c) - MathHelper.sin(feather.angle_58 + feather.angleNoiseXz_5c)) * (feather.translationMagnitudeXz_3c >> 8));
    final int y = (int)(feather.yOrigin_54 + feather.translationMagnitudeY_50 * MathHelper.sin(feather.angle_58));
    final int z = (int)((MathHelper.cos(feather.angle_58 + feather.angleNoiseXz_5c) + MathHelper.sin(feather.angle_58 + feather.angleNoiseXz_5c)) * (feather.translationMagnitudeXz_3c >> 8));
    feather.translation_08.set(x, y, z);

    feather.currentFrame_04++;
    if(feather.currentFrame_04 >= feather.countCallback1and3Frames_4c) {
      feather.currentFrame_04 = 0;
      feather.callbackIndex_02++;
    }
    //LAB_8010fbc0
  }

  @Method(0x8010fbd4L)
  private void spinWsDragoonTransformationEffect(final EffectManagerData6c<EffectManagerParams.VoidType> manager, final WsDragoonTransformationFeatherInstance70 feather) {
    feather.angle_58 += feather.angleStep_60;
    feather.angle_58 %= MathHelper.TWO_PI;

    final int x = (int)((MathHelper.cos(feather.angle_58 + feather.angleNoiseXz_5c) - MathHelper.sin(feather.angle_58 + feather.angleNoiseXz_5c)) * (feather.translationMagnitudeXz_3c >> 8));
    final int y = (int)(feather.yOrigin_54 + feather.translationMagnitudeY_50 * MathHelper.sin(feather.angle_58));
    final int z = (int)((MathHelper.cos(feather.angle_58 + feather.angleNoiseXz_5c) + MathHelper.sin(feather.angle_58 + feather.angleNoiseXz_5c)) * (feather.translationMagnitudeXz_3c >> 8));
    feather.translation_08.set(x, y, z);

    feather.currentFrame_04++;
    if(feather.currentFrame_04 >= 0xf) {
      feather.currentFrame_04 = 0;
      feather.callbackIndex_02++;
      feather.velocityTranslationMagnitudeXz_40 = -feather.translationMagnitudeXz_3c / feather.countCallback1and3Frames_4c;
      feather.velocityTranslationMagnitudeY_1c = -feather.translationMagnitudeY_50 / feather.countCallback1and3Frames_4c;
    }
    //LAB_8010fd20
  }

  @Method(0x8010fd34L)
  private void contractWsDragoonTransformationEffect(final EffectManagerData6c<EffectManagerParams.VoidType> manager, final WsDragoonTransformationFeatherInstance70 feather) {
    feather.angle_58 += feather.angleStep_60;
    feather.angle_58 %= MathHelper.TWO_PI;
    feather.translationMagnitudeXz_3c += feather.velocityTranslationMagnitudeXz_40;

    final int x = (int)((MathHelper.cos(feather.angle_58 + feather.angleNoiseXz_5c) - MathHelper.sin(feather.angle_58 + feather.angleNoiseXz_5c)) * (feather.translationMagnitudeXz_3c >> 8));
    final int y = (int)(feather.yOrigin_54 + feather.translationMagnitudeY_50 * MathHelper.sin(feather.angle_58));
    final int z = (int)((MathHelper.cos(feather.angle_58 + feather.angleNoiseXz_5c) + MathHelper.sin(feather.angle_58 + feather.angleNoiseXz_5c)) * (feather.translationMagnitudeXz_3c >> 8));
    feather.translation_08.set(x, y, z);
    feather.translationMagnitudeY_50 += feather.velocityTranslationMagnitudeY_1c;

    feather.currentFrame_04++;
    if(feather.currentFrame_04 >= feather.countCallback1and3Frames_4c) {
      feather.renderFeather_00 = false;
      feather.currentFrame_04 = 0;
      feather.callbackIndex_02++;
    }
    //LAB_8010fe70
  }

  @Method(0x8010fe84L)
  private void WsDragoonTransformationCallback4(final EffectManagerData6c<EffectManagerParams.VoidType> manager, final WsDragoonTransformationFeatherInstance70 a2) {
    // no-op
  }

  /**
   * <ol start="0">
   *   <li>{@link this#initializeWsDragoonTransformationEffect}</li>
   *   <li>{@link this#expandWsDragoonTransformationEffect}</li>
   *   <li>{@link this#spinWsDragoonTransformationEffect}</li>
   *   <li>{@link this#contractWsDragoonTransformationEffect}</li>
   *   <li>{@link this#WsDragoonTransformationCallback4}</li>
   * </ol>
   */
  private final BiConsumer<EffectManagerData6c<EffectManagerParams.VoidType>, WsDragoonTransformationFeatherInstance70>[] WsDragoonTransformationFeatherCallbacks_80119ff4 = new BiConsumer[5];
  {
    this.WsDragoonTransformationFeatherCallbacks_80119ff4[0] = this::initializeWsDragoonTransformationEffect;
    this.WsDragoonTransformationFeatherCallbacks_80119ff4[1] = this::expandWsDragoonTransformationEffect;
    this.WsDragoonTransformationFeatherCallbacks_80119ff4[2] = this::spinWsDragoonTransformationEffect;
    this.WsDragoonTransformationFeatherCallbacks_80119ff4[3] = this::contractWsDragoonTransformationEffect;
    this.WsDragoonTransformationFeatherCallbacks_80119ff4[4] = this::WsDragoonTransformationCallback4; // no-op
  }
}
