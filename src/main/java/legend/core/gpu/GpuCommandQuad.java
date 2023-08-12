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

  private VramTexture texture;
  private VramTexture[] palettes;

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
      LOGGER.warn("Negative G! %x", g);
    }

    if(b < 0) {
      LOGGER.warn("Negative B! %x", b);
    }

    return this.rgb(b << 16 | g << 8 | r);
  }

  public GpuCommandQuad monochrome(final int colour) {
    return this.rgb(colour, colour, colour);
  }

  public GpuCommandQuad monochrome(final float colour) {
    return this.monochrome((int)(colour * 0x100));
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

  public GpuCommandQuad texture(final VramTexture texture) {
    this.texture = texture;
    this.textured = true;
    return this;
  }

  public GpuCommandQuad texture(final VramTexture texture, final VramTexture[] palettes) {
    this.texture = texture;
    this.palettes = palettes;
    this.textured = true;
    return this;
  }

  @Override
  public void render(final Gpu gpu) {
    final int x1 = Math.max(this.x + gpu.getOffsetX(), gpu.drawingArea.x.get());
    final int y1 = Math.max(this.y + gpu.getOffsetY(), gpu.drawingArea.y.get());
    final int x2 = Math.min(this.x + gpu.getOffsetX() + this.w, gpu.drawingArea.x.get() + gpu.drawingArea.w.get());
    final int y2 = Math.min(this.y + gpu.getOffsetY() + this.h, gpu.drawingArea.y.get() + gpu.drawingArea.h.get());

    final int u1;
    final int v1;
    if(this.textured) {
      u1 = this.u + x1 - (this.x + gpu.getOffsetX());
      v1 = this.v + y1 - (this.y + gpu.getOffsetY());
    } else {
      u1 = 0;
      v1 = 0;
    }

    gpu.rasterizeQuad(x1, y1, x2, y2, this.colour, this.raw, this.textured, u1, v1, this.clutX, this.clutY, this.vramX, this.vramY, this.bpp, this.translucence, this.texture, this.palettes);
  }
}
