package legend.game.wmap;

import legend.core.QueuedModelStandard;
import legend.core.gpu.Bpp;
import legend.core.opengl.Obj;
import legend.core.opengl.QuadBuilder;

import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.RENDERER;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;
import static legend.game.Scus94491BpeSegment_800b.tickCount_800bb0fc;

public class CoolonQueenFuryOverlay {
  /** Wmap.coolonIconStateIndices_800ef154 */
  private static final int[] coolonIconStates = {0, 1, 2, 3, 0};
  /** Wmap.queenFuryIconStateIndices_800ef158 */
  private static final int[] queenFuryIconStates = {0, 0, 0, 0, 1, 2, 3, 4, 4, 4, 4, 3, 2, 1, 0};
  /** Wmap.squareButtonUs_800ef168 */
  private static final int[] buttonStates = {0, 0, 1, 2, 2, 1, 0};

  private final Obj[] buttonSprites = new Obj[3];
  private final Obj[] coolonSprites = new Obj[4];
  private final Obj[] queenFurySprites = new Obj[5];

  public CoolonQueenFuryOverlay() {
    this.buildButton();
    this.buildCoolonIcon();
    this.buildQueenFuryIcon();
  }

  private void buildButton() {
    for(int i = 0; i < 3; i++) {
      this.buttonSprites[i] = new QuadBuilder("CoolonQfButton")
        .bpp(Bpp.BITS_4)
        .clut(640, 508)
        .vramPos(640, 256)
        .monochrome(1.0f)
        .pos(GPU.getOffsetX() + 86.0f, GPU.getOffsetY() + 88.0f, 52.0f)
        .size(16.0f, 16.0f)
        .uv(64 + i * 16, 168)
        .build();
    }
  }

  private void buildCoolonIcon() {
    for(int i = 0; i < 4; i++) {
      this.coolonSprites[i] = new QuadBuilder("CoolonIcon")
        .bpp(Bpp.BITS_4)
        .clut(640, 506)
        .vramPos(640, 256)
        .monochrome(1.0f)
        .pos(GPU.getOffsetX() + 106.0f, GPU.getOffsetY() + 80.0f, 52.0f)
        .size(32.0f, 16.0f)
        .uv(i * 32.0f, 128.0f)
        .build();
    }
  }

  private void buildQueenFuryIcon() {
    for(int i = 0; i < 5; i++) {
      this.queenFurySprites[i] = new QuadBuilder("QueenFuryIcon")
        .bpp(Bpp.BITS_4)
        .clut(640, 507)
        .vramPos(640, 256)
        .monochrome(1.0f)
        .pos(GPU.getOffsetX() + 106.0f, GPU.getOffsetY() + 80.0f, 52.0f)
        .size(24.0f, 24.0f)
        .uv(i * 24.0f, 144.0f)
        .build();
    }
  }

  /** @param mode 0: Coolon icon, 1: Queen Fury icon */
  public void render(final int mode) {
    final int buttonState = buttonStates[(int)(tickCount_800bb0fc / 2 / (3.0f / vsyncMode_8007a3b8) % 7)];
    final Obj button = this.buttonSprites[buttonState];
    RENDERER.queueOrthoModel(button, QueuedModelStandard.class);

    final int iconState;
    final Obj icon;
    if(mode == 0) {
      iconState = coolonIconStates[(int)(tickCount_800bb0fc / 2 / (3.0f / vsyncMode_8007a3b8) % 5)];
      icon = this.coolonSprites[iconState];
    } else {
      iconState = queenFuryIconStates[(int)(tickCount_800bb0fc / 3 / (3.0f / vsyncMode_8007a3b8) % 15)];
      icon = this.queenFurySprites[iconState];
    }

    RENDERER.queueOrthoModel(icon, QueuedModelStandard.class);
  }

  public void deallocate() {
    for(final Obj button : this.buttonSprites) {
      button.delete();
    }

    for(final Obj icon : this.coolonSprites) {
      icon.delete();
    }

    for(final Obj icon : this.queenFurySprites) {
      icon.delete();
    }
  }
}
