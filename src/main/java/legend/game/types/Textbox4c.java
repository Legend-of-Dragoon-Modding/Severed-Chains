package legend.game.types;

import legend.core.gte.MV;
import org.joml.Vector3f;

import java.util.Arrays;

import static legend.core.GameEngine.CONFIG;
import static legend.lodmod.LodConfig.UI_COLOUR;

public class Textbox4c {
  public static final int ANIMATING = 0x1;
  public static final int NO_ANIMATE_OUT = 0x4;
  public static final int RENDER_BACKGROUND = 0x8000_0000;

  public TextboxState state_00;
  /**
   * <ul>
   *   <li>0 - no background</li>
   *   <li>1 - normal</li>
   *   <li>2 - animate in/out</li>
   * </ul>
   */
  public BackgroundType backgroundType_04 = BackgroundType.NO_BACKGROUND;
  public boolean renderBorder_06;
  /**
   * <ul>
   *   <li>0x1 - animating</li>
   *   <li>0x2 - ?</li>
   *   <li>0x4 - do not animate out</li>
   *   <li>0x8000_0000 - render background</li>
   * </ul>
   */
  public int flags_08;
  public float z_0c;
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
  public float currentX_28;
  public float currentY_2c;
  public float stepX_30;
  public float stepY_34;
  public float _38;
  public float _3c;

  public final MV backgroundTransforms = new MV();
  public final MV[] borderTransforms = new MV[8];
  public float oldX;
  public float oldY;
  public float oldW;
  public float oldH;
  public int oldScaleW;
  public int oldScaleH;
  public boolean updateBorder;

  public final Vector3f colour = new Vector3f(CONFIG.getConfig(UI_COLOUR.get()));

  public Textbox4c() {
    Arrays.setAll(this.borderTransforms, i -> new MV());
  }

  public void clear() {
    this.state_00 = TextboxState._1;
    this.renderBorder_06 = false;
    this.flags_08 = 0;
    this.z_0c = 14;
    this.currentTicks_10 = 0;
    this.width_1c = 0;
    this.height_1e = 0;
    this.animationWidth_20 = 0x1000;
    this.animationHeight_22 = 0x1000;
    this.animationTicks_24 = 0;
    this.currentX_28 = 0;
    this.currentY_2c = 0;
    this.stepX_30 = 0;
    this.stepY_34 = 0;
    this._38 = 0;
    this._3c = 0;
    this.colour.set(CONFIG.getConfig(UI_COLOUR.get()));
  }
}
