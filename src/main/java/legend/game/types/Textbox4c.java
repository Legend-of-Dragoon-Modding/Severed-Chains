package legend.game.types;

public class Textbox4c {
  public static final int ANIMATING = 0x1;
  public static final int RENDER_BACKGROUND = 0x8000_0000;

  public int state_00;
  public int _04;
  public int _06;
  /**
   * <ul>
   *   <li>0x1 - animating</li>
   *   <li>0x2 - ?</li>
   *   <li>0x4 - ?</li>
   *   <li>0x8000_0000 - render background</li>
   * </ul>
   */
  public int flags_08;
  public int z_0c;
  public int currentTicks_10;
  public float x_14;
  public float y_16;
  public int chars_18;
  public int lines_1a;
  public int width_1c;
  public int height_1e;
  /** Width multiplier to animate textbox in/out (.12) */
  public int animationWidth_20;
  /** Height multiplier to animate textbox in/out (.12) */
  public int animationHeight_22;
  /** total number of ticks for animation */
  public int animationTicks_24;
  public float _28;
  public float _2c;
  public float _30;
  public float _34;
  public float _38;
  public float _3c;
  public int _40;
  public int _44;
  public int _48;
}
