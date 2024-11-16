package legend.core.opengl;

import legend.core.RenderBatch;
import legend.core.RenderEngine;
import legend.core.gpu.Rect4i;
import legend.game.modding.coremod.CoreMod;

import static legend.core.GameEngine.CONFIG;
import static org.lwjgl.opengl.GL11C.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11C.GL_SCISSOR_TEST;
import static org.lwjgl.opengl.GL11C.glDisable;
import static org.lwjgl.opengl.GL11C.glEnable;
import static org.lwjgl.opengl.GL11C.glScissor;

public class RenderState {
  private boolean backfaceCulling;

  private final RenderEngine engine;
  private RenderBatch batch;
  private boolean widescreen;
  private float w;
  private float h;

  public RenderState(final RenderEngine engine) {
    this.engine = engine;
  }

  public void initBatch(final RenderBatch batch) {
    this.batch = batch;
    this.widescreen = batch.allowWidescreen && CONFIG.getConfig(CoreMod.ALLOW_WIDESCREEN_CONFIG.get());
    this.w = this.engine.getRenderWidth() / batch.projectionWidth;
    this.h = this.engine.getRenderHeight() / batch.projectionHeight;

    this.backfaceCulling = false;
    glDisable(GL_CULL_FACE);
    glDisable(GL_SCISSOR_TEST);
  }

  public void backfaceCulling(final boolean enable) {
    if(this.backfaceCulling != enable) {
      this.backfaceCulling = enable;

      if(enable) {
        glEnable(GL_CULL_FACE);
      } else {
        glDisable(GL_CULL_FACE);
      }
    }
  }

  public void scissor(final Rect4i rect) {
    glEnable(GL_SCISSOR_TEST);

    if(this.widescreen || this.batch.expandedSubmap) {
      glScissor(Math.round((rect.x + this.batch.widescreenOrthoOffsetX) * this.h * (this.batch.expectedWidth / this.batch.projectionWidth) / this.batch.widthSquisher), this.engine.getRenderHeight() - Math.round((rect.y + rect.h) * this.h), Math.round(rect.w * this.h * (this.batch.expectedWidth / this.batch.projectionWidth) / this.batch.widthSquisher), Math.round(rect.h * this.h));
    } else {
      glScissor(Math.round((rect.x + this.batch.widescreenOrthoOffsetX) * this.w), this.engine.getRenderHeight() - Math.round((rect.y + rect.h) * this.h), Math.round(rect.w * this.w), Math.round(rect.h * this.h));
    }
  }

  public void disableScissor() {
    glDisable(GL_SCISSOR_TEST);
  }
}
