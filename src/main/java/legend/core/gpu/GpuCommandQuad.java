package legend.core.gpu;

import legend.game.types.Translucency;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GpuCommandQuad extends GpuCommand {
  private static final Logger LOGGER = LogManager.getFormatterLogger(GpuCommandQuad.class);

  private Bpp bpp = Bpp.BITS_4;
  private Translucency translucence;
  private boolean raw;
  private boolean textured;

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

  public GpuCommandQuad bpp(final Bpp bpp) {
    this.bpp = bpp;
    return this;
  }

  public final GpuCommandQuad translucent(final Translucency trans) {
    this.translucence = trans;
    return this;
  }

  public final GpuCommandQuad noTextureBlending() {
    this.raw = true;
    return this;
  }

  public GpuCommandQuad rgb(final int colour) {
    this.colour = colour;
    return this;
  }

  public GpuCommandQuad rgb(final int r, final int g, final int b) {
    if(r < 0) {
      LOGGER.warn("Negative R! %x", r);
    }

    if(g < 0) {
      LOGGER.warn("Negative R! %x", g);
    }

    if(b < 0) {
      LOGGER.warn("Negative R! %x", b);
    }

    return this.rgb(b << 16 | g << 8 | r);
  }

  public GpuCommandQuad monochrome(final int colour) {
    return this.rgb(colour, colour, colour);
  }

  public GpuCommandQuad pos(final int x, final int y, final int w, final int h) {
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
    return this;
  }

  public GpuCommandQuad uv(final int u, final int v) {
    this.u = u;
    this.v = v;
    this.textured = true;
    return this;
  }

  public GpuCommandQuad clut(final int x, final int y) {
    this.clutX = x;
    this.clutY = y;
    this.textured = true;
    return this;
  }

  public GpuCommandQuad vramPos(final int x, final int y) {
    if(x < 0) {
      throw new IllegalArgumentException("Invalid VRAM X " + x);
    }

    if(y < 0) {
      throw new IllegalArgumentException("Invalid VRAM Y " + y);
    }

    this.vramX = x;
    this.vramY = y;
    this.textured = true;
    return this;
  }

  @Override
  public void render(final Gpu gpu) {
    final int x1 = Math.max(this.x + gpu.getOffsetX(), gpu.drawingArea.x.get());
    final int y1 = Math.max(this.y + gpu.getOffsetY(), gpu.drawingArea.y.get());
    final int x2 = Math.min(this.x + gpu.getOffsetX() + this.w, gpu.drawingArea.w.get());
    final int y2 = Math.min(this.y + gpu.getOffsetY() + this.h, gpu.drawingArea.h.get());

    final int u1;
    final int v1;
    if(this.textured) {
      u1 = this.u + x1 - (this.x + gpu.getOffsetX());
      v1 = this.v + y1 - (this.y + gpu.getOffsetY());
    } else {
      u1 = 0;
      v1 = 0;
    }

    for(int y = y1, v = v1; y < y2; y++, v++) {
      for(int x = x1, u = u1; x < x2; x++, u++) {
        // Check background mask
        if(gpu.status.drawPixels == Gpu.DRAW_PIXELS.NOT_TO_MASKED_AREAS) {
          if((gpu.getPixel(x, y) & 0xff00_0000L) != 0) {
            continue;
          }
        }

        int texel;
        if(this.textured) {
          texel = gpu.getTexel(u, v, this.clutX, this.clutY, this.vramX, this.vramY, this.bpp);
          if(texel == 0) {
            continue;
          }

          if(!this.raw) {
            texel = Gpu.applyBlending(this.colour, texel);
          }

          if(this.translucence != null && (texel & 0xff00_0000) != 0) {
            texel = gpu.handleTranslucence(x, y, texel, this.translucence);
          }
        } else {
          if(this.translucence != null) {
            texel = gpu.handleTranslucence(x, y, this.colour, this.translucence);
          } else {
            texel = this.colour;
          }
        }

        gpu.setPixel(x, y, (gpu.status.setMaskBit ? 1 : 0) << 24 | texel);
      }
    }
  }
}
