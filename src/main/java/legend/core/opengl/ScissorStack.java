package legend.core.opengl;

import legend.core.RenderBatch;
import legend.core.RenderEngine;
import legend.core.gpu.Rect4i;
import legend.game.EngineState;
import legend.game.modding.coremod.CoreMod;

import java.util.Arrays;

import static legend.core.GameEngine.CONFIG;

public class ScissorStack {
  private final RenderEngine engine;
  private final RenderBatch mainBatch;
  private final Rect4i[] stack = new Rect4i[100];
  private int index;

  public ScissorStack(final RenderEngine engine, final RenderBatch mainBatch) {
    this.engine = engine;
    this.mainBatch = mainBatch;
    Arrays.setAll(this.stack, i -> new Rect4i());
    this.reset();
  }

  /** Reset the scissor stack and set the scissor region to the entire screen */
  public void reset() {
    this.index = 0;
    this.stack[0].set(0, 0, this.engine.getRenderWidth(), this.engine.getRenderHeight());
  }

  /** Get the current scissor region */
  public Rect4i top() {
    return this.stack[this.index];
  }

  /** Push a new scissor region. This region will be set to the previous region. */
  public ScissorStack push() {
    this.index++;
    this.stack[this.index].set(this.stack[this.index - 1]);
    return this;
  }

  /** Restore the previous scissor region */
  public ScissorStack pop() {
    this.index--;
    return this;
  }

  /**
   * Set the scissor region to a region inside the current region using absolute
   * coordinates. Any part that falls outside the current region will be clipped.
   */
  public ScissorStack set(final int x, final int y, final int w, final int h) {
    this.top().subregion(x, y, w, h);
    return this;
  }

  /**
   * Set the scissor region to a region inside the current region using absolute
   * coordinates. Any part that falls outside the current region will be clipped.
   */
  public ScissorStack set(final Rect4i rect) {
    return this.set(rect.x, rect.y, rect.w, rect.h);
  }

  /** Equivalent to {@link #set} but rescales engine coords to projection coords */
  public ScissorStack setRescale(final int x, final int y, final int w, final int h) {
    final boolean widescreen = this.mainBatch.renderMode == EngineState.RenderMode.PERSPECTIVE && CONFIG.getConfig(CoreMod.ALLOW_WIDESCREEN_CONFIG.get()) || this.mainBatch.renderMode == EngineState.RenderMode.LEGACY && CONFIG.getConfig(CoreMod.LEGACY_WIDESCREEN_MODE_CONFIG.get()) == SubmapWidescreenMode.EXPANDED;
    final float widthMultiplier = (float)this.engine.getRenderWidth() / this.mainBatch.nativeWidth;
    final float heightMultiplier = (float)this.engine.getRenderHeight() / this.mainBatch.nativeHeight;

    if(widescreen) {
      return this.set(Math.round((x + this.mainBatch.widescreenOrthoOffsetX) * heightMultiplier * ((float)this.mainBatch.expectedWidth / this.mainBatch.nativeWidth)), this.engine.getRenderHeight() - Math.round((y + h) * heightMultiplier), Math.round(w * heightMultiplier * ((float)this.mainBatch.expectedWidth / this.mainBatch.nativeWidth)), Math.round(h * heightMultiplier));
    }

    return this.set(Math.round((x + this.mainBatch.widescreenOrthoOffsetX) * widthMultiplier), this.engine.getRenderHeight() - Math.round((y + h) * heightMultiplier), Math.round(w * widthMultiplier), Math.round(h * heightMultiplier));
  }

  /** Equivalent to {@link #set} but rescales engine coords to projection coords */
  public ScissorStack setRescale(final Rect4i rect) {
    return this.setRescale(rect.x, rect.y, rect.w, rect.h);
  }
}
