package legend.core.opengl;

import legend.core.QueuedModel;
import legend.core.RenderBatch;
import legend.core.RenderEngine;
import legend.core.gpu.Rect4i;
import legend.game.EngineState;
import legend.game.modding.coremod.CoreMod;

import static legend.core.GameEngine.CONFIG;

public abstract class RenderState {
  protected final RenderEngine engine;
  protected final Rect4i tempScissorRect = new Rect4i();
  protected final Rect4i activeScissorRect = new Rect4i();
  protected boolean backfaceCulling;
  protected RenderBatch batch;
  protected boolean widescreen;
  protected float w;
  protected float h;
  protected boolean scissor;
  protected boolean depthTest;
  protected int depthComparator;

  public RenderState(final RenderEngine engine) {
    this.engine = engine;
  }

  public void initBatch(final RenderBatch batch) {
    this.batch = batch;
    this.widescreen = batch.getRenderMode() == EngineState.RenderMode.PERSPECTIVE && CoreMod.ALLOW_WIDESCREEN_CONFIG.isValid() && CONFIG.getConfig(CoreMod.ALLOW_WIDESCREEN_CONFIG.get()) || batch.getRenderMode() == EngineState.RenderMode.LEGACY && CoreMod.LEGACY_WIDESCREEN_MODE_CONFIG.isValid() && CONFIG.getConfig(CoreMod.LEGACY_WIDESCREEN_MODE_CONFIG.get()) == SubmapWidescreenMode.EXPANDED;
    this.w = (float)this.engine.getRenderWidth() / batch.nativeWidth;
    this.h = (float)this.engine.getRenderHeight() / batch.nativeHeight;

    this.setCulling(false);
    this.setScissor(false);
  }

  public void backfaceCulling(final boolean enable) {
    if(this.backfaceCulling != enable) {
      this.setCulling(enable);
    }
  }

  public void enableDepthTest(final int comparator) {
    if(!this.depthTest) {
      this.setDepthTest(true);
    }

    if(this.depthComparator != comparator) {
      this.setDepthComparator(comparator);
    }
  }

  public void disableDepthTest() {
    if(this.depthTest) {
      this.setDepthTest(false);
    }
  }

  public void scissor(final QueuedModel<?, ?> model) {
    final Rect4i worldScissor = model.worldScissor();
    final Rect4i modelScissor = model.modelScissor();

    this.tempScissorRect.set(worldScissor.x, this.engine.getRenderHeight() - (worldScissor.y + worldScissor.h), worldScissor.w, worldScissor.h);

    if(modelScissor.w != 0 || modelScissor.h != 0) {
      if(this.widescreen) {
        this.tempScissorRect.subregion(Math.round((modelScissor.x + this.batch.widescreenOrthoOffsetX) * this.h * ((float)this.batch.expectedWidth / this.batch.nativeWidth)), this.engine.getRenderHeight() - Math.round((modelScissor.y + modelScissor.h) * this.h), Math.round(modelScissor.w * this.h * ((float)this.batch.expectedWidth / this.batch.nativeWidth)), Math.round(modelScissor.h * this.h));
      } else {
        final float offset;
        final float w;

        if(CONFIG.getConfig(CoreMod.LEGACY_WIDESCREEN_MODE_CONFIG.get()) == SubmapWidescreenMode.FORCED_4_3) {
          final float ratio = (float)this.engine.getRenderWidth() / this.engine.getRenderHeight();
          final float adjustedW = this.batch.nativeHeight * ratio;
          offset = (adjustedW - this.batch.nativeWidth) / 2.0f;
          w = this.h;
        } else {
          offset = this.batch.widescreenOrthoOffsetX;
          w = this.w;
        }

        this.tempScissorRect.subregion(Math.round((modelScissor.x + offset) * w), this.engine.getRenderHeight() - Math.round((modelScissor.y + modelScissor.h) * this.h), Math.round(modelScissor.w * w), Math.round(modelScissor.h * this.h));
      }
    }

    this.applyScissor();
  }

  public void fullScreenScissor() {
    this.tempScissorRect.set(0, 0, this.engine.getRenderWidth(), this.engine.getRenderHeight());
    this.applyScissor();
  }

  protected void applyScissor() {
    if(!this.activeScissorRect.equals(this.tempScissorRect)) {
      this.setScissorRect(this.tempScissorRect);
    }
  }

  public void enableScissor() {
    if(!this.scissor) {
      this.setScissor(true);
    }
  }

  public void disableScissor() {
    if(this.scissor) {
      this.setScissor(false);
    }
  }

  protected abstract void setCulling(boolean enable);

  protected abstract void setDepthTest(boolean enable);

  protected abstract void setDepthComparator(int comparator);

  protected abstract void setScissor(boolean enable);

  protected abstract void setScissorRect(Rect4i rect);
}
