package legend.game.combat.effects;

import legend.core.gte.MV;
import legend.core.memory.Method;
import legend.game.scripting.ScriptState;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.util.Arrays;

import static legend.game.Scus94491BpeSegment.tmdGp0Tpage_1f8003ec;
import static legend.game.Scus94491BpeSegment.zOffset_1f8003e8;
import static legend.game.combat.SEffe.FUN_800e61e4;
import static legend.game.combat.SEffe.FUN_800e62a8;
import static legend.game.combat.SEffe.calculateEffectTransforms;
import static legend.game.combat.SEffe.renderBillboardSpriteEffect;
import static legend.game.combat.SEffe.renderTmdSpriteEffect;

public class SpriteWithTrailEffect30 implements Effect<EffectManagerParams.ColourType> {
  /**
   * <ul>
   *   <li>0x4 - use colour</li>
   *   <li>0x8 - use scale</li>
   * </ul>
   */
  public int colourAndScaleFlags_00;
  public int effectFlag_04;
  public int countCopies_08;
  public int countTransformSteps_0c;
  public int colourAndScaleTransformModifier_10;
  /** This value is always modified when indexing to remain within the range of count */
  public int translationIndexBase_14;
  public Vector3f[] instanceTranslations_18;
  public Sub subEffect_1c;

  public SpriteWithTrailEffect30(final int count) {
    this.countCopies_08 = count;
    if(count != 0) {
      this.instanceTranslations_18 = new Vector3f[count];
      Arrays.setAll(this.instanceTranslations_18, i -> new Vector3f());
    } else {
      this.instanceTranslations_18 = null;
    }
  }

  @Override
  @Method(0x801196bcL)
  public void tick(final ScriptState<EffectManagerData6c<EffectManagerParams.ColourType>> state) {
    final EffectManagerData6c<EffectManagerParams.ColourType> manager = state.innerStruct_00;

    if(this.countCopies_08 != 0) {
      final MV transformMatrix = new MV();
      calculateEffectTransforms(transformMatrix, manager);
      this.instanceTranslations_18[this.translationIndexBase_14 % this.countCopies_08].set(transformMatrix.transfer);
      this.translationIndexBase_14++;
    }

    //LAB_80119778
  }

  /** Some effects use CTMD render pipeline if type == 0x300_0000 */
  @Override
  @Method(0x80118e98L)
  public void render(final ScriptState<EffectManagerData6c<EffectManagerParams.ColourType>> state) {
    final EffectManagerData6c<EffectManagerParams.ColourType> manager = state.innerStruct_00;

    if(manager.params_10.flags_00 >= 0) { // No errors
      final MV transformMatrix = new MV();
      calculateEffectTransforms(transformMatrix, manager);

      final int type = this.effectFlag_04 & 0xff00_0000;
      if(type == 0x300_0000) {
        final TmdSpriteEffect10 sprite = (TmdSpriteEffect10)this.subEffect_1c;

        //LAB_80118f38
        if((manager.params_10.flags_00 & 0x4000_0000) != 0) {
          tmdGp0Tpage_1f8003ec = manager.params_10.flags_00 >>> 23 & 0x60;
        } else {
          //LAB_80118f5c
          tmdGp0Tpage_1f8003ec = sprite.tpage_10;
        }

        //LAB_80118f68
        zOffset_1f8003e8 = manager.params_10.z_22;

        if((manager.params_10.flags_00 & 0x40) == 0) {
          FUN_800e61e4((manager.params_10.colour_1c.x << 5) / (float)0x1000, (manager.params_10.colour_1c.y << 5) / (float)0x1000, (manager.params_10.colour_1c.z << 5) / (float)0x1000);
        }

        //LAB_80118f9c
        renderTmdSpriteEffect(sprite.tmd_08, sprite.obj, manager.params_10, transformMatrix);
      } else if(type == 0x400_0000) {
        final BillboardSpriteEffect0c sprite = (BillboardSpriteEffect0c)this.subEffect_1c;
        renderBillboardSpriteEffect(sprite.metrics_04, manager.params_10, transformMatrix);
      }

      //LAB_80118fac
      if(this.countCopies_08 != 0) {
        final EffectManagerParams.ColourType effectParams = new EffectManagerParams.ColourType();
        final Vector3i colour = new Vector3i();
        final Vector3f stepScale = new Vector3f();

        //LAB_80118fc4
        effectParams.set(manager.params_10);

        final int combinedSteps = this.countCopies_08 * (this.countTransformSteps_0c + 1);
        if((this.colourAndScaleFlags_00 & 0x4) != 0) {
          final int brightness = this.colourAndScaleTransformModifier_10 - 0x1000;
          effectParams.r_28 = effectParams.colour_1c.x << 12;
          effectParams.g_2c = effectParams.colour_1c.y << 12;
          effectParams.b_30 = effectParams.colour_1c.z << 12;
          colour.x = effectParams.colour_1c.x * brightness / combinedSteps;
          colour.y = effectParams.colour_1c.y * brightness / combinedSteps;
          colour.z = effectParams.colour_1c.z * brightness / combinedSteps;
        }

        //LAB_801190a8
        if((this.colourAndScaleFlags_00 & 0x8) != 0) {
          final float scaleModifier = (this.colourAndScaleTransformModifier_10 - 0x1000) / (float)0x1000;
          stepScale.x = effectParams.scale_16.x * scaleModifier / combinedSteps;
          stepScale.y = effectParams.scale_16.y * scaleModifier / combinedSteps;
          stepScale.z = effectParams.scale_16.z * scaleModifier / combinedSteps;
        }

        //LAB_80119130
        //LAB_8011914c
        for(int i = 1; i < this.countCopies_08 && i < this.translationIndexBase_14; i++) {
          final Vector3f instTranslation = this.instanceTranslations_18[(this.translationIndexBase_14 - i - 1) % this.countCopies_08];

          final int steps = this.countTransformSteps_0c + 1;
          final float stepX = (instTranslation.x - transformMatrix.transfer.x) / steps;
          final float stepY = (instTranslation.y - transformMatrix.transfer.y) / steps;
          final float stepZ = (instTranslation.z - transformMatrix.transfer.z) / steps;

          float x = transformMatrix.transfer.x;
          float y = transformMatrix.transfer.y;
          float z = transformMatrix.transfer.z;

          //LAB_80119204
          for(int j = this.countTransformSteps_0c; j >= 0; j--) {
            if((this.colourAndScaleFlags_00 & 0x4) != 0) {
              effectParams.r_28 += colour.x;
              effectParams.g_2c += colour.y;
              effectParams.b_30 += colour.z;
              //LAB_80119254
              //LAB_80119270
              //LAB_8011928c
              effectParams.colour_1c.x = effectParams.r_28 >> 12;
              effectParams.colour_1c.y = effectParams.g_2c >> 12;
              effectParams.colour_1c.z = effectParams.b_30 >> 12;
            }

            //LAB_80119294
            if((this.colourAndScaleFlags_00 & 0x8) != 0) {
              //LAB_801192e4
              //LAB_80119300
              //LAB_8011931c
              effectParams.scale_16.add(stepScale);
            }

            //LAB_80119324
            x += stepX;
            y += stepY;
            z += stepZ;

            //LAB_80119348
            //LAB_80119360
            //LAB_80119378
            transformMatrix.transfer.set(x, y, z);
            transformMatrix.scaleLocal(effectParams.scale_16);

            if(type == 0x300_0000) {
              //LAB_801193f0
              final TmdSpriteEffect10 subEffect = (TmdSpriteEffect10)this.subEffect_1c;
              renderTmdSpriteEffect(subEffect.tmd_08, subEffect.obj, effectParams, transformMatrix);
            } else if(type == 0x400_0000) {
              final BillboardSpriteEffect0c subEffect = (BillboardSpriteEffect0c)this.subEffect_1c;
              renderBillboardSpriteEffect(subEffect.metrics_04, effectParams, transformMatrix);
            }
            //LAB_80119400
            //LAB_80119404
          }
          //LAB_8011940c
        }

        //LAB_80119420
        if(type == 0x300_0000 && (manager.params_10.flags_00 & 0x40) == 0) {
          FUN_800e62a8();
        }
      }
    }

    //LAB_80119454
  }

  @Override
  public void destroy(final ScriptState<EffectManagerData6c<EffectManagerParams.ColourType>> state) {
    if(this.subEffect_1c instanceof final TmdSpriteEffect10 effect) {
      if(effect.obj != null) {
        effect.obj.delete();
        effect.obj = null;
      }
    }
  }

  public static class Sub {
    public int flags_00;
  }
}
