package legend.game.combat.effects;

import java.util.Arrays;

public class MonsterDeathEffect34 implements BttlScriptData6cSubBase1 {
  /** short */
  public int _00;
  /** ushort */
  public int _02;
  /** ushort */
  public int animCount_04;
  /** ushort; Set to 0 and never used */
  public int unused_06;
  public int scriptIndex_08;
  public final GenericSpriteEffect24 sprite_0c = new GenericSpriteEffect24();

  public GenericSpriteEffectAnimState30[] animStateArray_30;

  public MonsterDeathEffect34(final int animStateCount) {
    this.animCount_04 = animStateCount;
    this.animStateArray_30 = new GenericSpriteEffectAnimState30[animStateCount];
    Arrays.setAll(this.animStateArray_30, GenericSpriteEffectAnimState30::new);
  }
}
