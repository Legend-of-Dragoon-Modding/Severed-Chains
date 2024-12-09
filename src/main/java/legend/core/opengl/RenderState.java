package legend.core.opengl;

import legend.core.QueuedModel;
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

  private final ScissorStack scissorStack;
  private final Rect4i tempScissorRect = new Rect4i();
  private final Rect4i activeScissorRect = new Rect4i();
  private boolean scissor;

  public RenderState(final RenderEngine engine, final ScissorStack scissorStack) {
    this.engine = engine;
    this.scissorStack = scissorStack;
  }

  public void initBatch(final RenderBatch batch) {
    this.batch = batch;
    this.widescreen = batch.allowWidescreen && CONFIG.getConfig(CoreMod.ALLOW_WIDESCREEN_CONFIG.get());
    this.w = this.engine.getRenderWidth() / batch.projectionWidth;
    this.h = this.engine.getRenderHeight() / batch.projectionHeight;

    this.backfaceCulling = false;
    glDisable(GL_CULL_FACE);

    this.scissor = false;
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

  public void scissor(final QueuedModel<?, ?> model) {
    final Rect4i worldScissor = model.worldScissor();
    final Rect4i modelScissor = model.modelScissor();

    this.tempScissorRect.set(worldScissor.x, this.engine.getRenderHeight() - (worldScissor.y + worldScissor.h), worldScissor.w, worldScissor.h);

    if(modelScissor.w != 0 || modelScissor.h != 0) {
      if(this.widescreen || this.batch.expandedSubmap) {
        this.tempScissorRect.subregion(Math.round((modelScissor.x + this.batch.widescreenOrthoOffsetX) * this.h * (this.batch.expectedWidth / this.batch.projectionWidth) / this.batch.widthSquisher), this.engine.getRenderHeight() - Math.round((modelScissor.y + modelScissor.h) * this.h), Math.round(modelScissor.w * this.h * (this.batch.expectedWidth / this.batch.projectionWidth) / this.batch.widthSquisher), Math.round(modelScissor.h * this.h));
      } else {
        this.tempScissorRect.subregion(Math.round((modelScissor.x + this.batch.widescreenOrthoOffsetX) * this.w), this.engine.getRenderHeight() - Math.round((modelScissor.y + modelScissor.h) * this.h), Math.round(modelScissor.w * this.w), Math.round(modelScissor.h * this.h));
      }
    }

    this.applyScissor();
  }

  private void applyScissor() {
    if(!this.activeScissorRect.equals(this.tempScissorRect)) {
      glScissor(this.tempScissorRect.x, this.tempScissorRect.y, this.tempScissorRect.w, this.tempScissorRect.h);
      this.activeScissorRect.set(this.tempScissorRect);
    }
  }

  public void enableScissor() {
    if(!this.scissor) {
      glEnable(GL_SCISSOR_TEST);
      this.scissor = true;
    }
  }

  public void disableScissor() {
    if(this.scissor) {
      glDisable(GL_SCISSOR_TEST);
      this.scissor = false;
    }
  }
}
