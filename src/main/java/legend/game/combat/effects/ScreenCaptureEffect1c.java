package legend.game.combat.effects;

public class ScreenCaptureEffect1c implements Effect {
  public final ScreenCaptureEffectMetrics8 metrics_00 = new ScreenCaptureEffectMetrics8();
  public int captureW_04;
  public int captureH_08;
  /**
   * 0 is unknown, 1 is for Death Dimension, Melbu screenshot attack, and demon frog
   */
  public int rendererIndex_0c;
  /**
   * Capture width and height scaled by depth into scene and projection plane distance
   */
  public float screenspaceW_10;
  public float screenspaceH_14;
}
