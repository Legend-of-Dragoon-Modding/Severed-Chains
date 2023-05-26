package legend.game.combat.types;

import legend.game.types.TmdAnimationFile;
import legend.game.unpacker.FileData;

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
  public int isLoaded_0b;

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

  /** Type 4/5 */
  public static class BpeType extends CombatantStruct1a8_c {
    public final FileData bpe_00;

    public BpeType(final FileData bpe) {
      this.bpe_00 = bpe;
    }
  }

  /** Type 6 */
  public static class TimType extends CombatantStruct1a8_c {
    public final FileData data;

    public TimType(final FileData data) {
      this.data = data;
    }
  }
}
