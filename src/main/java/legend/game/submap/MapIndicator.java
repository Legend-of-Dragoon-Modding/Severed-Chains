package legend.game.submap;

import legend.core.gpu.Bpp;
import legend.core.gte.MV;
import legend.core.opengl.MeshObj;
import legend.core.opengl.QuadBuilder;
import static legend.core.GameEngine.RENDERER;

public class MapIndicator {
  private final MeshObj[] indicators = new MeshObj[3];
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

  public void MapIndicator() {
    this.indicators[IndicatorType.PLAYER.indicator] = new QuadBuilder("PlayerIndicator")
      .vramPos(960, 256)
      .bpp(Bpp.BITS_4)
      .uv(0, 0)
      .size(16, 16)
      .uvSize(16, 16)
      .build();

    this.indicators[IndicatorType.DOOR.indicator] = new QuadBuilder("DoorIndicator")
      .vramPos(960, 256)
      .bpp(Bpp.BITS_4)
      .uv(0, 0)
      .size(8, 16)
      .uvSize(8, 16)
      .build();

    this.indicators[IndicatorType.ALERT.indicator] = new QuadBuilder("AlertIndicator")
      .vramPos(960, 256)
      .bpp(Bpp.BITS_4)
      .clut(976, 464)
      .uv(0, 0)
      .size(24, 24)
      .uvSize(24, 24)
      .build();
  }

  private void createPlayerIndicator(final float r, final float g, final float b, final int cX, final int cY) {
    this.indicators[IndicatorType.PLAYER.indicator] = new QuadBuilder("PlayerIndicator")
      .vramPos(960, 256)
      .bpp(Bpp.BITS_4)
      .clut(cX, cY)
      .uv(0, 0)
      .size(16, 16)
      .uvSize(16, 16)
      .build();
  }

  private void createDoorIndicator(final int index, final float r, final float g, final float b, final int cX, final int cY) {
    this.indicators[IndicatorType.DOOR.indicator] = (new QuadBuilder("DoorIndicator")
      .vramPos(960, 256)
      .bpp(Bpp.BITS_4)
      .clut(cX, cY)
      .uv(0, 0)
      .size(8, 16)
      .uvSize(8, 16)
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
      this.createPlayerIndicator(r, g, b, cX, cY);
    }

    RENDERER.queueOrthoModel(this.indicators[IndicatorType.PLAYER.indicator], this.transforms)
      .colour(r, g, b)
      .clutOverride(cX, cY)
      .uvOffset(uX, uY);
  }

  public void renderDoorIndicator(final int index, final float x, final float y, final float z, final float r, final float g, final float b, final int cX, final int cY, final int uX, final int uY) {
    this.transforms.transfer.set(x, y, z);

    if(this.indicators[IndicatorType.DOOR.indicator] == null) {
      this.createDoorIndicator(index, r, g, b, cX, cY);
    }

    RENDERER.queueOrthoModel(this.indicators[IndicatorType.DOOR.indicator], this.transforms)
      .colour(r, g, b)
      .clutOverride(cX, cY)
      .uvOffset(uX, uY);
  }

  public void renderAlertIndicator(final float x, final float y, final float z, final int uX, final int uY) {
    this.transforms.transfer.set(x, y, z);

    if(this.indicators[IndicatorType.ALERT.indicator] == null) {
      this.createAlertIndicator();
    }

    RENDERER.queueOrthoModel(this.indicators[IndicatorType.ALERT.indicator], this.transforms)
      .uvOffset(uX, uY);
  }

  public void destroy() {
    for(int i = 0; i < indicators.length; i++) {
      if(this.indicators[i] != null) {
        this.indicators[i].delete();
        this.indicators[i] = null;
      }
    }
  }
}