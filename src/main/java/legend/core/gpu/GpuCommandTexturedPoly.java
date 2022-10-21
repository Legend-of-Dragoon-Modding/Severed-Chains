package legend.core.gpu;

import legend.game.types.Translucency;

import java.util.Arrays;

public class GpuCommandTexturedPoly extends GpuCommand {
  private final int vertexCount;

  private Bpp bpp = Bpp.BITS_4;
  private Translucency translucence;
  private boolean shaded;
  private boolean raw;

  private final int[] x;
  private final int[] y;
  private final int[] u;
  private final int[] v;
  private final int[] colour;

  private int clutX;
  private int clutY;
  private int vramX;
  private int vramY;

  public GpuCommandTexturedPoly(final int vertexCount) {
    this.vertexCount = vertexCount;
    this.x = new int[vertexCount];
    this.y = new int[vertexCount];
    this.u = new int[vertexCount];
    this.v = new int[vertexCount];
    this.colour = new int[vertexCount];
  }

  public GpuCommandTexturedPoly bpp(final Bpp bpp) {
    this.bpp = bpp;
    return this;
  }

  public final GpuCommandTexturedPoly translucent(final Translucency trans) {
    this.translucence = trans;
    return this;
  }

  public final GpuCommandTexturedPoly shaded() {
    this.shaded = true;
    return this;
  }

  public final GpuCommandTexturedPoly noTextureBlending() {
    this.raw = true;
    return this;
  }

  public GpuCommandTexturedPoly rgb(final int r, final int g, final int b) {
    return this.colour(b << 16 | g << 8 | r);
  }

  public GpuCommandTexturedPoly monochrome(final int colour) {
    return this.rgb(colour, colour, colour);
  }

  public GpuCommandTexturedPoly colour(final int colour) {
    Arrays.fill(this.colour, colour);
    return this;
  }

  public GpuCommandTexturedPoly rgb(final int vertex, final int r, final int g, final int b) {
    return this.colour(vertex, b << 16 | g << 8 | r);
  }

  public GpuCommandTexturedPoly monochrome(final int vertex, final int colour) {
    return this.rgb(vertex, colour, colour, colour);
  }

  public GpuCommandTexturedPoly colour(final int vertex, final int colour) {
    this.colour[vertex] = colour;
    return this;
  }

  public GpuCommandTexturedPoly pos(final int vertex, final int x, final int y) {
    this.x[vertex] = x;
    this.y[vertex] = y;
    return this;
  }

  public GpuCommandTexturedPoly uv(final int vertex, final int u, final int v) {
    this.u[vertex] = u;
    this.v[vertex] = v;
    return this;
  }

  public GpuCommandTexturedPoly clut(final int x, final int y) {
    this.clutX = x;
    this.clutY = y;
    return this;
  }

  public GpuCommandTexturedPoly vramPos(final int x, final int y) {
    this.vramX = x;
    this.vramY = y;
    return this;
  }

  @Override
  public void render(final Gpu gpu) {
    for(int i = 0; i < this.vertexCount; i++) {
      this.x[i] += gpu.getOffsetX();
      this.y[i] += gpu.getOffsetY();
    }

    gpu.rasterizeTriangle(this.x[0], this.y[0], this.x[1], this.y[1], this.x[2], this.y[2], this.u[0], this.v[0], this.u[1], this.v[1], this.u[2], this.v[2], this.colour[0], this.colour[1], this.colour[2], this.clutX, this.clutY, this.vramX, this.vramY, this.bpp, true, this.shaded, this.translucence != null, this.raw, this.translucence);

    if(this.vertexCount == 4) {
      gpu.rasterizeTriangle(this.x[1], this.y[1], this.x[2], this.y[2], this.x[3], this.y[3], this.u[1], this.v[1], this.u[2], this.v[2], this.u[3], this.v[3], this.colour[1], this.colour[2], this.colour[3], this.clutX, this.clutY, this.vramX, this.vramY, this.bpp, true, this.shaded, this.translucence != null, this.raw, this.translucence);
    }
  }
}
