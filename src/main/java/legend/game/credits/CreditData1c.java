package legend.game.credits;

import legend.game.credits.Credits.CreditsType;
import org.joml.Vector3f;

public class CreditData1c {
  public enum CreditState {
    LOAD_0,
    NONE_1,
    RENDER_2,
    PASSED_3,
  }

  public int index;

  public final Vector3f colour_00 = new Vector3f();
  public int prevCreditSlot_04;
  public CreditsType type_08;
  public float y_0c;
  public float width_0e;
  public float height_10;
  public float scroll_12;
  public float brightnessAngle_14;
  /**
   * <ul>
   *   <li>0 - load</li>
   *   <li>2 - render</li>
   *   <li>3 - off screen</li>
   * </ul>
   */
  public CreditState state_16;
}
