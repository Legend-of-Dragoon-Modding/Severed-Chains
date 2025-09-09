package legend.game.submap;

import legend.core.QueuedModelStandard;
import legend.core.gpu.Bpp;
import legend.core.gte.MV;
import legend.core.opengl.MeshObj;
import legend.core.opengl.Obj;
import legend.core.opengl.QuadBuilder;

import static legend.core.GameEngine.RENDERER;

public class MapIndicator {
  private final Obj[] indicators = new MeshObj[3];
  private final MV transforms = new MV();

  private enum IndicatorType {
    PLAYER(0),
    DOOR(1),
    ALERT(2),
    ;

    public final int indicator;

    IndicatorType(final int indicator) {
      this.indicator = indicator;
    }
  }

  private void createPlayerIndicator(final int cX, final int cY) {
    this.indicators[IndicatorType.PLAYER.indicator] = new QuadBuilder("PlayerIndicator")
      .vramPos(960, 256)
      .bpp(Bpp.BITS_4)
      .clut(cX, cY)
      .uv(0, 0)
      .size(16, 16)
      .uvSize(16, 16)
      .build();
  }

  private void createDoorIndicator(final int cX, final int cY) {
    this.indicators[IndicatorType.DOOR.indicator] = (new QuadBuilder("DoorIndicator")
      .vramPos(960, 256)
      .bpp(Bpp.BITS_4)
      .clut(cX, cY)
      .uv(0, 0)
      .size(8, 16)
      .uvSize(8, 15)
      .build());
  }

  private void createAlertIndicator() {
    this.indicators[IndicatorType.ALERT.indicator] = new QuadBuilder("AlertIndicator")
      .vramPos(960, 256)
      .bpp(Bpp.BITS_4)
      .clut(976, 464)
      .uv(0, 0)
      .size(24, 24)
      .uvSize(24, 24)
      .build();
  }

  public void renderPlayerIndicator(final float x, final float y, final float z, final float r, final float g, final float b, final int cX, final int cY, final int uX, final int uY) {
    this.transforms.transfer.set(x, y, z);

    if(this.indicators[IndicatorType.PLAYER.indicator] == null) {
      this.createPlayerIndicator(cX, cY);
    }

    RENDERER.queueOrthoModel(this.indicators[IndicatorType.PLAYER.indicator], this.transforms, QueuedModelStandard.class)
      .colour(r, g, b)
      .clutOverride(cX, cY)
      .uvOffset(uX, uY);
  }

  public void renderDoorIndicator(final float x, final float y, final float z, final float r, final float g, final float b, final int cX, final int cY, final int uX, final int uY) {
    this.transforms.transfer.set(x, y, z);

    if(this.indicators[IndicatorType.DOOR.indicator] == null) {
      this.createDoorIndicator(cX, cY);
    }

    RENDERER.queueOrthoModel(this.indicators[IndicatorType.DOOR.indicator], this.transforms, QueuedModelStandard.class)
      .colour(r, g, b)
      .clutOverride(cX, cY)
      .uvOffset(uX, uY);
  }

  public void renderAlertIndicator(final float x, final float y, final float z, final int uX, final int uY) {
    this.transforms.transfer.set(x, y, z);

    if(this.indicators[IndicatorType.ALERT.indicator] == null) {
      this.createAlertIndicator();
    }

    RENDERER.queueOrthoModel(this.indicators[IndicatorType.ALERT.indicator], this.transforms, QueuedModelStandard.class)
      .uvOffset(uX, uY);
  }

  public void destroy() {
    for(int i = 0; i < this.indicators.length; i++) {
      if(this.indicators[i] != null) {
        this.indicators[i].delete();
        this.indicators[i] = null;
      }
    }
  }
}
