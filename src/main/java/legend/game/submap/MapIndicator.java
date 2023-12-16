package legend.game.submap;

import legend.core.gpu.Bpp;
import legend.core.gte.MV;
import legend.core.opengl.Obj;
import legend.core.opengl.QuadBuilder;

import java.util.ArrayList;

import static legend.core.GameEngine.RENDERER;

public class MapIndicator {
  public Obj playerIndicator = null;
  public Obj doorIndicator = null;
  public Obj alertIndicator = null;
  private final MV transforms = new MV();

  private void createPlayerIndicator(final float r, final float g, final float b, final int cX, final int cY) {
    this.playerIndicator = new QuadBuilder("PlayerIndicator")
      .vramPos(960, 256)
      .bpp(Bpp.BITS_4)
      .clut(cX, cY)
      .uv(0, 0)
      .size(16, 16)
      .uvSize(16, 16)
      .build();
  }

  private void createDoorIndicator(final int index, final float r, final float g, final float b, final int cX, final int cY) {
    this.doorIndicator = (new QuadBuilder("DoorIndicator")
      .vramPos(960, 256)
      .bpp(Bpp.BITS_4)
      .clut(cX, cY)
      .uv(0, 0)
      .size(8, 16)
      .uvSize(8, 16)
      .build());
  }

  private void createAlertIndicator() {
    this.alertIndicator = new QuadBuilder("AlertIndicator")
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

    if(this.playerIndicator == null) {
      this.createPlayerIndicator(r, g, b, cX, cY);
    }

    RENDERER.queueOrthoModel(this.playerIndicator, this.transforms)
      .colour(r, g, b)
      .clutOverride(cX, cY)
      .uvOffset(uX, uY);
  }

  public void renderDoorIndicator(final int index, final float x, final float y, final float z, final float r, final float g, final float b, final int cX, final int cY, final int uX, final int uY) {
    this.transforms.transfer.set(x, y, z);

    if(this.doorIndicator == null) {
      this.createDoorIndicator(index, r, g, b, cX, cY);
    }

    RENDERER.queueOrthoModel(this.doorIndicator, this.transforms)
      .colour(r, g, b)
      .clutOverride(cX, cY)
      .uvOffset(uX, uY);
  }

  public void renderAlertIndicator(final float x, final float y, final float z, final int uX, final int uY) {
    this.transforms.transfer.set(x, y, z);

    if(this.alertIndicator == null) {
      this.createAlertIndicator();
    }

    RENDERER.queueOrthoModel(this.alertIndicator, this.transforms)
      .uvOffset(uX, uY);
  }

  public void destroy() {
    if(playerIndicator != null) {
      this.playerIndicator.delete();
      this.playerIndicator = null;
    }

    if(doorIndicator != null) {
      this.doorIndicator.delete();
      this.doorIndicator = null;
    }

    if(alertIndicator != null) {
      this.alertIndicator.delete();
      this.alertIndicator = null;
    }
  }

}
