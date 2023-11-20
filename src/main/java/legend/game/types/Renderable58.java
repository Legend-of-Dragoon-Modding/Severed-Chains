package legend.game.types;

/** Main menu renderable? */
public class Renderable58 {
  /**
   * <ul>
   *   <li>0x4 - start and end glyph is the same - no transition</li>
   *   <li>0x40 - invisible</li>
   * </ul>
   */
  public int flags_00;
  public int glyph_04;
  public int _08;
  public int _0c;
  public int startGlyph_10;
  public int endGlyph_14;
  public int _18;
  public int _1c;
  public UiType uiType_20;
//  public int[] metricsIndices_24;
  public int _28;
  public int tpage_2c;
  public int clut_30;
  public float widthScale;
  public float heightScale_38;
  public int z_3c;
  public int x_40;
  public int y_44;
  public int _48;
  public Renderable58 child_50;
  public Renderable58 parent_54;

  public int heightCut;

  public Renderable58 setVisible(final boolean visible) {
    if(visible) {
      this.flags_00 &= ~0x40;
    } else {
      this.flags_00 |= 0x40;
    }

    return this;
  }
}
