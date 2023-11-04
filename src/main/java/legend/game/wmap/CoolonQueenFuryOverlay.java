package legend.game.wmap;

import legend.core.gpu.Bpp;
import legend.core.opengl.MeshObj;
import legend.core.opengl.QuadBuilder;

import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.RENDERER;

public class CoolonQueenFuryOverlay {
  private MeshObj button;
  private int previousButtonState;
  private MeshObj icon;
  private int previousIconState;

  public CoolonQueenFuryOverlay() {
    this.previousButtonState = -1;
    this.previousIconState = -1;
  }

  public int getPreviousButtonState() {
    return this.previousButtonState;
  }

  public void setPreviousButtonState(final int state) {
    this.previousButtonState = state;
  }

  public int getPreviousIconState() {
    return this.previousIconState;
  }

  public void setPreviousIconState(final int state) {
    this.previousIconState = state;
  }

  public CoolonQueenFuryOverlay buildButton(final float offsetU) {
    if(this.button != null) {
      this.button.delete();
      this.button = null;
    }

    this.button = new QuadBuilder("CoolonQfButton")
      .bpp(Bpp.BITS_4)
      .clut(640, 508)
      .vramPos(640, 256)
      .monochrome(0.5f)
      .pos(GPU.getOffsetX() + 86.0f, GPU.getOffsetY() + 88.0f, 52.0f)
      .size(16.0f, 16.0f)
      .uv(64.0f + offsetU, 168)
      .build();

    return this;
  }

  public CoolonQueenFuryOverlay buildIcon(final int mode, final float u, final float v, final float w, final float h) {
    if(this.icon != null) {
      this.icon.delete();
      this.icon = null;
    }

    final int clutY = 506 + mode;
    this.icon = new QuadBuilder("CoolonQfIcon")
      .bpp(Bpp.BITS_4)
      .clut(640, clutY)
      .vramPos(640, 256)
      .monochrome(0.5f)
      .pos(GPU.getOffsetX() + 106.0f, GPU.getOffsetY() + 80.0f, 52.0f)
      .size(w, h)
      .uv(u, v)
      .build();

    return this;
  }

  public void render() {
    if(this.button != null) {
      RENDERER.queueOrthoOverlayModel(this.button);
    }

    if(this.icon != null) {
      RENDERER.queueOrthoOverlayModel(this.icon);
    }
  }

  public void deallocate() {
    if(this.button != null) {
      this.button.delete();
      this.button = null;
    }

    if(this.icon != null) {
      this.icon.delete();
      this.icon = null;
    }
  }
}
