package legend.game.wmap;

import legend.core.QueuedModelStandard;
import legend.core.gpu.Bpp;
import legend.core.opengl.MeshObj;
import legend.core.opengl.QuadBuilder;
import legend.game.types.Translucency;

import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.RENDERER;

public class ZoomOverlay {
  public static final WmapRectMetrics06[] zoomUiMetrics_800ef104 = {
    new WmapRectMetrics06(0, 8, 96, 16, 32, 16),
    new WmapRectMetrics06(0, 16, 96, 16, 32, 16),
    new WmapRectMetrics06(0, 0, 64, 0, 32, 16),
    new WmapRectMetrics06(0, 8, 64, 16, 32, 16),
    new WmapRectMetrics06(0, 16, 96, 0, 32, 16),
    new WmapRectMetrics06(36, 0, 64, 48, 16, 16),
    new WmapRectMetrics06(36, 16, 80, 48, 16, 16),
  };

  public final MeshObj overlayTranslucent;
  public final MeshObj overlayOpaque;

  public ZoomOverlay() {
    final QuadBuilder builderTranslucent = new QuadBuilder("ZoomOverlayTranslucent");
    final QuadBuilder builderOpaque = new QuadBuilder("ZoomOverlayOpaque");
    int i = 0;
    for(; i < 2; i++) {
      builderTranslucent
        .add()
        .bpp(Bpp.BITS_4)
        .clut(640, 502)
        .vramPos(640, 256)
        .pos(GPU.getOffsetX() + zoomUiMetrics_800ef104[i].x_00 + 88.0f, GPU.getOffsetY() + zoomUiMetrics_800ef104[i].y_01 - 96.0f, 79.0f)
        .size(zoomUiMetrics_800ef104[i].w_04, zoomUiMetrics_800ef104[i].h_05)
        .uv(zoomUiMetrics_800ef104[i].u_02, zoomUiMetrics_800ef104[i].v_03)
        .translucency(Translucency.HALF_B_PLUS_HALF_F)
        .monochrome(0.5f);
    }

    for(; i < 7; i++) {
      builderOpaque
        .add()
        .bpp(Bpp.BITS_4)
        .clut(640, i < 5 ? 502 : 503)
        .vramPos(640, 256)
        .pos(GPU.getOffsetX() + zoomUiMetrics_800ef104[i].x_00 + 88.0f, GPU.getOffsetY() + zoomUiMetrics_800ef104[i].y_01 - 96.0f, 80.0f)
        .size(zoomUiMetrics_800ef104[i].w_04, zoomUiMetrics_800ef104[i].h_05)
        .uv(zoomUiMetrics_800ef104[i].u_02, zoomUiMetrics_800ef104[i].v_03);

        if(i >= 5) {
          builderOpaque.monochrome(0.5f);
        } else {
          builderOpaque.monochrome(0.25f);
        }
    }

    this.overlayTranslucent = builderTranslucent.build();
    this.overlayOpaque = builderOpaque.build();
  }

  public void render(final WMapModelAndAnimData258.ZoomState zoomState) {
    final int currentZoomLevel = switch(zoomState) {
      case LOCAL_0 -> 2;
      case CONTINENT_1, TRANSITION_MODEL_OUT_2 -> 3;
      case WORLD_3, TRANSITION_MODEL_IN_4 -> 4;
    };

    for(int i = 0; i < 5; i++) {
      final QueuedModelStandard model = RENDERER.queueOrthoModel(this.overlayOpaque, QueuedModelStandard.class)
        .vertices(i * 4, 4);

      if(i + 2 == currentZoomLevel) {
        model.monochrome(4.0f);
      }
    }

    for(int i = 0; i < 2; i++) {
      RENDERER.queueOrthoModel(this.overlayTranslucent, QueuedModelStandard.class)
        .vertices(i * 4, 4);
    }
  }

  public void delete() {
    if(this.overlayTranslucent != null) {
      this.overlayTranslucent.delete();
    }

    if(this.overlayOpaque != null) {
      this.overlayOpaque.delete();
    }
  }
}
