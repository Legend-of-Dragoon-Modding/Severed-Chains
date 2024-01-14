package legend.game.credits;

import legend.game.credits.Credits.CreditsType;
import org.joml.Vector3i;

public class CreditData1c {
  public enum CreditState {
    LOAD_0,
    NONE_1,
    RENDER_2,
    PASSED_3,
  }

  public int index;

  public final Vector3i colour_00 = new Vector3i();
  public int prevCreditSlot_04;
  public CreditsType type_08;
  public int y_0c;
  public int width_0e;
  public int height_10;
  public int scroll_12;
  public int brightnessAngle_14;
  /**
   * <ul>
   *   <li>0 - load</li>
   *   <li>2 - render</li>
   *   <li>3 - off screen</li>
   * </ul>
   */
  public CreditState state_16;
}
