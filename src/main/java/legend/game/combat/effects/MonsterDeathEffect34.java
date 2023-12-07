package legend.game.combat.effects;

import legend.core.MathHelper;
import legend.core.memory.Method;
import legend.game.scripting.ScriptState;
import org.joml.Vector3f;

import java.util.Arrays;

import static legend.game.combat.Bttl_800c.getModelObjectTranslation;
import static legend.game.combat.Bttl_800c.seed_800fa754;
import static legend.game.combat.Bttl_800d.setModelObjectVisibility;
import static legend.game.combat.Bttl_800e.renderGenericSpriteAtZOffset0;

public class MonsterDeathEffect34 implements Effect {
  /** short; Indicates highest part index to turn off. Parts at index <= value are "destroyed" */
  public int destroyedPartsCutoffIndex_00;
  /** ushort */
  public int remainingFrameLimit_02;
  /** ushort */
  public int modelObjectCount_04;
  /** ushort; Set to 0 and never used */
  public int unused_06;
  public int scriptIndex_08;
  public final GenericSpriteEffect24 sprite_0c = new GenericSpriteEffect24();

  public MonsterDeathEffectObjectDestructor30[] objectDestructorArray_30;

  public MonsterDeathEffect34(final int modelObjectCount) {
    this.modelObjectCount_04 = modelObjectCount;
    this.objectDestructorArray_30 = new MonsterDeathEffectObjectDestructor30[modelObjectCount];
    Arrays.setAll(this.objectDestructorArray_30, MonsterDeathEffectObjectDestructor30::new);
  }

  @Method(0x800d30c0L)
  public void monsterDeathEffectRenderer(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state, final EffectManagerData6c<EffectManagerParams.VoidType> manager) {
    final MonsterDeathEffectObjectDestructor30[] objArray = this.objectDestructorArray_30;

    //LAB_800d30fc
    for(int objIndex = 0; objIndex < this.modelObjectCount_04; objIndex++) {
      if(objArray[objIndex].destructionState_00 == 1) {
        this.sprite_0c.r_14 = objArray[objIndex].r_24 >>> 8;
        this.sprite_0c.g_15 = objArray[objIndex].g_26 >>> 8;
        this.sprite_0c.b_16 = objArray[objIndex].b_28 >>> 8;
        this.sprite_0c.angle_20 = objArray[objIndex].angleModifier_0c;
        this.sprite_0c.scaleX_1c = manager.params_10.scale_16.x + objArray[objIndex].scaleModifier_04;
        this.sprite_0c.scaleY_1e = manager.params_10.scale_16.y + objArray[objIndex].scaleModifier_04;
        renderGenericSpriteAtZOffset0(this.sprite_0c, objArray[objIndex].translation_14);
      }
      //LAB_800d3174
    }
    //LAB_800d3190
  }

  @Method(0x800d31b0L)
  public void monsterDeathEffectTicker(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state, final EffectManagerData6c<EffectManagerParams.VoidType> manager) {
    this.remainingFrameLimit_02--;
    if(this.remainingFrameLimit_02 == 0) {
      state.deallocateWithChildren();
    } else {
      //LAB_800d320c
      final MonsterDeathEffectObjectDestructor30[] objArray = this.objectDestructorArray_30;
      this.destroyedPartsCutoffIndex_00 += 2;

      //LAB_800d322c
      for(int objIndex = 0; objIndex < this.modelObjectCount_04; objIndex++) {
        if(this.destroyedPartsCutoffIndex_00 >= objIndex + 1 && objArray[objIndex].destructionState_00 == -1) {
          objArray[objIndex].destructionState_00 = 1;
          objArray[objIndex].stepCount_01 = 8;
          objArray[objIndex].scaleModifier_04 = 0;
          objArray[objIndex].scaleModifierVelocity_08 = (seed_800fa754.nextInt(49) + 104) / (float)0x1000;
          objArray[objIndex].angleModifier_0c = seed_800fa754.nextFloat(MathHelper.TWO_PI);
          objArray[objIndex].angleModifierVelocity_10 = 0;
          objArray[objIndex].r_24 = manager.params_10.colour_1c.x << 8;
          objArray[objIndex].g_26 = manager.params_10.colour_1c.y << 8;
          objArray[objIndex].b_28 = manager.params_10.colour_1c.z << 8;
          objArray[objIndex].stepR_2a = objArray[objIndex].r_24 / objArray[objIndex].stepCount_01;
          objArray[objIndex].stepG_2c = objArray[objIndex].g_26 / objArray[objIndex].stepCount_01;
          objArray[objIndex].stepB_2e = objArray[objIndex].b_28 / objArray[objIndex].stepCount_01;
          final Vector3f translation = new Vector3f();
          getModelObjectTranslation(this.scriptIndex_08, translation, objIndex);
          objArray[objIndex].translation_14.set(translation);
          setModelObjectVisibility(this.scriptIndex_08, objIndex, false);
        }

        //LAB_800d33d0
        if(objArray[objIndex].destructionState_00 > 0) {
          objArray[objIndex].stepCount_01--;

          if(objArray[objIndex].stepCount_01 == 0) {
            objArray[objIndex].destructionState_00 = 0;
          }

          //LAB_800d3400
          objArray[objIndex].angleModifier_0c +=  objArray[objIndex].angleModifierVelocity_10;
          objArray[objIndex].r_24 -= objArray[objIndex].stepR_2a;
          objArray[objIndex].g_26 -= objArray[objIndex].stepG_2c;
          objArray[objIndex].b_28 -= objArray[objIndex].stepB_2e;
          objArray[objIndex].scaleModifier_04 +=  objArray[objIndex].scaleModifierVelocity_08;
        }
        //LAB_800d3450
      }
    }
    //LAB_800d346c
  }
}
