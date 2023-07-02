package legend.game.combat.effects;

import java.util.Arrays;

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
}
