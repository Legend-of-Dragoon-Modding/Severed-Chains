package legend.core.gpu;

import legend.game.types.Translucency;

public class GpuCommandTexturedQuad extends GpuCommand {
  private Bpp bpp = Bpp.BITS_4;
  private Translucency translucence;
  private boolean raw;

  private int colour;

  private int x;
  private int y;
  private int w;
  private int h;
  private int u;
  private int v;

  private int clutX;
  private int clutY;
  private int vramX;
  private int vramY;

  public GpuCommandTexturedQuad bpp(final Bpp bpp) {
    this.bpp = bpp;
    return this;
  }

  public final GpuCommandTexturedQuad translucent(final Translucency trans) {
    this.translucence = trans;
    return this;
  }

  public final GpuCommandTexturedQuad noTextureBlending() {
    this.raw = true;
    return this;
  }

  public GpuCommandTexturedQuad rgb(final int r, final int g, final int b) {
    this.colour = b << 16 | g << 8 | r;
    return this;
  }

  public GpuCommandTexturedQuad monochrome(final int colour) {
    return this.rgb(colour, colour, colour);
  }

  public GpuCommandTexturedQuad pos(final int x, final int y, final int w, final int h) {
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
    return this;
  }

  public GpuCommandTexturedQuad uv(final int u, final int v) {
    this.u = u;
    this.v = v;
    return this;
  }

  public GpuCommandTexturedQuad clut(final int x, final int y) {
    this.clutX = x;
    this.clutY = y;
    return this;
  }

  public GpuCommandTexturedQuad vramPos(final int x, final int y) {
    this.vramX = x;
    this.vramY = y;
    return this;
  }

  @Override
  public void render(final Gpu gpu) {
    final int x1 = Math.max(this.x + gpu.getOffsetX(), gpu.drawingArea.x.get());
    final int y1 = Math.max(this.y + gpu.getOffsetY(), gpu.drawingArea.y.get());
    final int x2 = Math.min(this.x + gpu.getOffsetX() + this.w, gpu.drawingArea.w.get());
    final int y2 = Math.min(this.y + gpu.getOffsetY() + this.h, gpu.drawingArea.h.get());

    final int offsetX = x1 - (this.x + gpu.getOffsetX());
    final int offsetY = y1 - (this.y + gpu.getOffsetY());

    final int u1 = this.u + offsetX;
    final int v1 = this.v + offsetY;

    for(int y = y1, v = v1; y < y2; y++, v++) {
      for(int x = x1, u = u1; x < x2; x++, u++) {
        // Check background mask
        if(gpu.status.drawPixels == Gpu.DRAW_PIXELS.NOT_TO_MASKED_AREAS) {
          if((gpu.getPixel(x, y) & 0xff00_0000L) != 0) {
            continue;
          }
        }

        int texel = gpu.getTexel(gpu.maskTexelAxis(u, gpu.preMaskX, gpu.postMaskX), gpu.maskTexelAxis(v, gpu.preMaskY, gpu.postMaskY), this.clutX, this.clutY, this.vramX, this.vramY, this.bpp);
        if(texel == 0) {
          continue;
        }

        if(!this.raw) {
          texel = gpu.applyBlending(this.colour, texel);
        }

        if(this.translucence != null && (texel & 0xff00_0000) != 0) {
          texel = gpu.handleTranslucence(x, y, texel, this.translucence);
        }

        gpu.setPixel(x, y, (gpu.status.setMaskBit ? 1 : 0) << 24 | texel);
      }
    }
  }
}
