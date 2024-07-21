package legend.game.combat.effects;

import legend.core.MathHelper;
import legend.core.memory.Method;
import legend.game.combat.bent.BattleEntity27c;
import legend.game.scripting.ScriptState;
import org.joml.Vector3f;

import static legend.game.combat.Battle.seed_800fa754;
import static legend.game.combat.SEffe.getModelObjectTranslation;

public class MonsterDeathEffect34 implements Effect<EffectManagerParams.VoidType> {
  /** short; Indicates highest part index to turn off. Parts at index <= value are "destroyed" */
  private int destroyedPartsCutoffIndex_00;
  /** ushort */
  private int remainingFrameLimit_02;
  /** ushort */
  private final int modelObjectCount_04;
  private final BattleEntity27c parent_08;
  private final GenericSpriteEffect24 sprite_0c;

  private final MonsterDeathEffectObjectDestructor30[] objectDestructorArray_30;

  public MonsterDeathEffect34(final BattleEntity27c parent, final GenericSpriteEffect24 sprite) {
    this.modelObjectCount_04 = parent.model_148.partCount_98;
    this.remainingFrameLimit_02 = this.modelObjectCount_04 + 8;
    this.parent_08 = parent;
    this.sprite_0c = sprite;
    this.objectDestructorArray_30 = new MonsterDeathEffectObjectDestructor30[this.modelObjectCount_04];

    //LAB_800d35a0
    for(int objIndex = 0; objIndex < this.modelObjectCount_04; objIndex++) {
      this.objectDestructorArray_30[objIndex] = new MonsterDeathEffectObjectDestructor30();
      this.setModelObjectVisibility(objIndex, true);
    }
  }

  @Override
  @Method(0x800d30c0L)
  public void render(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {
    final EffectManagerData6c<EffectManagerParams.VoidType> manager = state.innerStruct_00;

    //LAB_800d30fc
    for(int objIndex = 0; objIndex < this.modelObjectCount_04; objIndex++) {
      final MonsterDeathEffectObjectDestructor30 obj = this.objectDestructorArray_30[objIndex];

      if(obj.destructionState_00 == 1) {
        this.sprite_0c.r_14 = obj.r_24 >>> 8;
        this.sprite_0c.g_15 = obj.g_26 >>> 8;
        this.sprite_0c.b_16 = obj.b_28 >>> 8;
        this.sprite_0c.angle_20 = obj.angleModifier_0c;
        this.sprite_0c.scaleX_1c = manager.params_10.scale_16.x + obj.scaleModifier_04;
        this.sprite_0c.scaleY_1e = manager.params_10.scale_16.y + obj.scaleModifier_04;
        this.sprite_0c.render(obj.translation_14);
      }
      //LAB_800d3174
    }
    //LAB_800d3190
  }

  @Override
  @Method(0x800d31b0L)
  public void tick(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {
    final EffectManagerData6c<EffectManagerParams.VoidType> manager = state.innerStruct_00;

    this.remainingFrameLimit_02--;
    if(this.remainingFrameLimit_02 == 0) {
      state.deallocateWithChildren();
    } else {
      //LAB_800d320c
      this.destroyedPartsCutoffIndex_00 += 2;

      //LAB_800d322c
      for(int objIndex = 0; objIndex < this.modelObjectCount_04; objIndex++) {
        final MonsterDeathEffectObjectDestructor30 obj = this.objectDestructorArray_30[objIndex];

        if(this.destroyedPartsCutoffIndex_00 >= objIndex + 1 && obj.destructionState_00 == -1) {
          obj.destructionState_00 = 1;
          obj.stepCount_01 = 8;
          obj.scaleModifier_04 = 0;
          obj.scaleModifierVelocity_08 = (seed_800fa754.nextInt(49) + 104) / (float)0x1000;
          obj.angleModifier_0c = seed_800fa754.nextFloat(MathHelper.TWO_PI);
          obj.angleModifierVelocity_10 = 0;
          obj.r_24 = manager.params_10.colour_1c.x << 8;
          obj.g_26 = manager.params_10.colour_1c.y << 8;
          obj.b_28 = manager.params_10.colour_1c.z << 8;
          obj.stepR_2a = obj.r_24 / obj.stepCount_01;
          obj.stepG_2c = obj.g_26 / obj.stepCount_01;
          obj.stepB_2e = obj.b_28 / obj.stepCount_01;
          final Vector3f translation = new Vector3f();
          getModelObjectTranslation(this.parent_08, translation, objIndex);
          obj.translation_14.set(translation);
          this.setModelObjectVisibility(objIndex, false);
        }

        //LAB_800d33d0
        if(obj.destructionState_00 > 0) {
          obj.stepCount_01--;

          if(obj.stepCount_01 == 0) {
            obj.destructionState_00 = 0;
          }

          //LAB_800d3400
          obj.angleModifier_0c +=  obj.angleModifierVelocity_10;
          obj.r_24 -= obj.stepR_2a;
          obj.g_26 -= obj.stepG_2c;
          obj.b_28 -= obj.stepB_2e;
          obj.scaleModifier_04 +=  obj.scaleModifierVelocity_08;
        }
        //LAB_800d3450
      }
    }
    //LAB_800d346c
  }

  @Override
  public void destroy(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {
    this.sprite_0c.delete();
  }

  @Method(0x800d0094L)
  private void setModelObjectVisibility(final int objIndex, final boolean clearBit) {
    //LAB_800d00d4
    if(clearBit) {
      this.parent_08.model_148.partInvisible_f4 &= ~(0x1L << objIndex);
    } else {
      //LAB_800d0104
      this.parent_08.model_148.partInvisible_f4 |= 0x1L << objIndex;
    }
  }
}
