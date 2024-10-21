package legend.game.types;

import legend.game.inventory.screens.PostBattleScreen;

/** Main menu renderable */
public class Renderable58 {
  /** Does not animate */
  public static final int FLAG_NO_ANIMATION = 0x4;
  /** Automatically deleted after rendering */
  public static final int FLAG_DELETE_AFTER_RENDER = 0x8;
  /** Automatically deleted after animation finishes */
  public static final int FLAG_DELETE_AFTER_ANIMATION = 0x10;
  /** Glyph sequence counts down instead of up */
  public static final int FLAG_BACKWARDS_ANIMATION = 0x20;
  /** Do not render */
  public static final int FLAG_INVISIBLE = 0x40;

  public int flags_00;
  public int glyph_04;
  public int ticksPerFrame_08;
  public int animationLoopsCompletedCount_0c;
  public int startGlyph_10;
  public int endGlyph_14;
  public int repeatStartGlyph_18;
  public int repeatEndGlyph_1c;
  public UiType uiType_20;
//  public int[] metricsIndices_24;
  /** Only values that are used are 0 and 1. deallocateRenderables(0) would only deallocate group 0. deallocateRenderables(0xff) would deallocate all groups 0xff and under. */
  public int deallocationGroup_28;
  public int tpage_2c;
  public int clut_30;
  public float widthScale;
  public float heightScale_38;
  public float z_3c;
  public int x_40;
  public int y_44;

  public Renderable58 child_50;
  public Renderable58 parent_54;

  public int widthCut;
  public int heightCut;

  public int baseX;
  public int baseY;

  /** We moved the UI textures into the vram region that the render buffers used to use so they could stay loaded. Some UIs like {@link PostBattleScreen} use their own textures so we have to use the normal tpage that's passed in. */
  public boolean useOriginalTpage;

  public Renderable58 setVisible(final boolean visible) {
    if(visible) {
      this.flags_00 &= ~Renderable58.FLAG_INVISIBLE;
    } else {
      this.flags_00 |= Renderable58.FLAG_INVISIBLE;
    }

    return this;
  }
}
