package legend.game.combat.types;

import legend.game.types.TmdAnimationFile;

/** A union type, see type property to tell which type it is */
public abstract class CombatantStruct1a8_c {
  public int BttlStruct08_index_04;
  public int BattleStructEf4Sub08_index_06;
  public int _08;
  public int _09;
  /**
   * <ol start="0">
   *  <li>None</li>
   *  <li>Anim file</li>
   *  <li>Anim file</li>
   *  <li>Index</li>
   *  <li>BPE version of 1</li>
   *  <li>BPE version of 2</li>
   *  <li>TIM</li>
   * </ol>
   */
  public int type_0a;
  public int _0b;

  /** Type 1/2 */
  public static class AnimType extends CombatantStruct1a8_c {
    public final TmdAnimationFile anim_00;

    public AnimType(final TmdAnimationFile anim) {
      this.anim_00 = anim;
    }
  }

  /** Type 3 */
  public static class IndexType extends CombatantStruct1a8_c {
    public final int index_00;

    public IndexType(final int index) {
      this.index_00 = index;
    }
  }

  /** Type 6 */
  public static class TimType extends CombatantStruct1a8_c {
    public final int x_00;
    public final int y_02;
    public final int h_03;

    public TimType(final int x, final int y, final int h) {
      this.x_00 = x;
      this.y_02 = y;
      this.h_03 = h;
    }
  }
}
