package legend.core.gpu;

import legend.game.types.Translucency;

public class GpuCommandUntexturedQuad extends GpuCommand {
  private Translucency translucency;

  private int colour;

  private int x;
  private int y;
  private int w;
  private int h;

  public final GpuCommandUntexturedQuad translucent(final Translucency translucency) {
    this.translucency = translucency;
    return this;
  }

  public GpuCommandUntexturedQuad rgb(final int r, final int g, final int b) {
    this.colour = b << 16 | g << 8 | r;
    return this;
  }

  public GpuCommandUntexturedQuad monochrome(final int colour) {
    return this.rgb(colour, colour, colour);
  }

  public GpuCommandUntexturedQuad pos(final int x, final int y, final int w, final int h) {
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
    return this;
  }

  @Override
  public void render(final Gpu gpu) {
    final int x1 = Math.max(this.x + gpu.getOffsetX(), gpu.drawingArea.x.get());
    final int y1 = Math.max(this.y + gpu.getOffsetY(), gpu.drawingArea.y.get());
    final int x2 = Math.min(this.x + gpu.getOffsetX() + this.w, gpu.drawingArea.w.get());
    final int y2 = Math.min(this.y + gpu.getOffsetY() + this.h, gpu.drawingArea.h.get());

    for(int y = y1; y < y2; y++) {
      for(int x = x1; x < x2; x++) {
        // Check background mask
        if(gpu.status.drawPixels == Gpu.DRAW_PIXELS.NOT_TO_MASKED_AREAS) {
          if((gpu.getPixel(x, y) & 0xff00_0000L) != 0) {
            continue;
          }
        }

        final int texel;
        if(this.translucency != null) {
          texel = gpu.handleTranslucence(x, y, this.colour, this.translucency);
        } else {
          texel = this.colour;
        }

        gpu.setPixel(x, y, (gpu.status.setMaskBit ? 1 : 0) << 24 | texel);
      }
    }
  }
}
