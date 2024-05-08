package legend.game.combat.types;

import legend.game.models.TmdAnimationFile;
import legend.game.unpacker.FileData;

/** A union type, see type property to tell which type it is */
public abstract class CombatantAsset0c {
  public int assetIndex_04;
  public int compressedAssetIndex_06;
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
  public boolean isLoaded_0b;

  /** Type 1/2 */
  public static class AnimType extends CombatantAsset0c {
    public final TmdAnimationFile anim_00;

    public AnimType(final TmdAnimationFile anim) {
      this.anim_00 = anim;
    }
  }

  /** Type 3 */
  public static class GlobalAssetType extends CombatantAsset0c {
    public final int assetIndex_00;

    public GlobalAssetType(final int assetIndex) {
      this.assetIndex_00 = assetIndex;
    }
  }

  /** Type 4/5 */
  public static class BpeType extends CombatantAsset0c {
    public final FileData bpe_00;

    public BpeType(final FileData bpe) {
      this.bpe_00 = bpe;
    }
  }

  /** Type 6 */
  public static class TimType extends CombatantAsset0c {
    public final FileData data;

    public TimType(final FileData data) {
      this.data = data;
    }
  }
}
