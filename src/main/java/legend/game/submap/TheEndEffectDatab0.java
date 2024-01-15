package legend.game.submap;

public class TheEndEffectDatab0 {
  /** short */
  public boolean shouldRender_00;
  /** short; inverted from retail */
  public boolean shouldBrighten_02;
  /** short; inverted from retail */
  public boolean shouldAdjustBrightness_04;
  /** short */
  public boolean shouldTickClut_06;
  /** short */
  public int tick_08;

  /** .16 */
  public int brightness_0c;
  /** .16 */
  public final int[] clutStep_10 = new int[16];
  /** .16 */
  public final int[] currClut_50 = new int[16];
  public final int[] finalClut_90 = new int[16];

  public TheEndEffectDatab0() {
    this.shouldBrighten_02 = true;
    this.shouldAdjustBrightness_04 = true;
  }
}
