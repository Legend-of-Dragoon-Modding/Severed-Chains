package legend.core.gpu;

import legend.game.types.Translucency;

import java.util.Arrays;

public class GpuCommandPoly extends GpuCommand {
  private final int vertexCount;

  private Bpp bpp = Bpp.BITS_4;
  private Translucency translucence;
  private boolean shaded;
  private boolean raw;
  private boolean textured;

  private final int[] x;
  private final int[] y;
  private final int[] u;
  private final int[] v;
  private final int[] colour;

  private int clutX;
  private int clutY;
  private int vramX;
  private int vramY;

  public GpuCommandPoly(final int vertexCount) {
    this.vertexCount = vertexCount;
    this.x = new int[vertexCount];
    this.y = new int[vertexCount];
    this.u = new int[vertexCount];
    this.v = new int[vertexCount];
    this.colour = new int[vertexCount];
  }

  public GpuCommandPoly(final GpuCommandPoly toCopy) {
    this.vertexCount = toCopy.vertexCount;

    this.bpp = toCopy.bpp;
    this.translucence = toCopy.translucence;
    this.shaded = toCopy.shaded;
    this.raw = toCopy.raw;
    this.textured = toCopy.textured;

    this.x = Arrays.copyOf(toCopy.x, this.vertexCount);
    this.y = Arrays.copyOf(toCopy.y, this.vertexCount);
    this.u = Arrays.copyOf(toCopy.u, this.vertexCount);
    this.v = Arrays.copyOf(toCopy.v, this.vertexCount);
    this.colour = Arrays.copyOf(toCopy.colour, this.vertexCount);

    this.clutX = toCopy.clutX;
    this.clutY = toCopy.clutY;
    this.vramX = toCopy.vramX;
    this.vramY = toCopy.vramY;
  }

  public GpuCommandPoly bpp(final Bpp bpp) {
    this.bpp = bpp;
    return this;
  }

  public final GpuCommandPoly translucent(final Translucency trans) {
    this.translucence = trans;
    return this;
  }

  public final GpuCommandPoly noTextureBlending() {
    this.raw = true;
    return this;
  }

  public GpuCommandPoly rgb(final int colour) {
    Arrays.fill(this.colour, colour);
    return this;
  }

  public GpuCommandPoly rgb(final int r, final int g, final int b) {
    return this.rgb(b << 16 | g << 8 | r);
  }

  public GpuCommandPoly monochrome(final int colour) {
    return this.rgb(colour, colour, colour);
  }

  public GpuCommandPoly rgb(final int vertex, final int colour) {
    this.shaded = true;
    this.colour[vertex] = colour;
    return this;
  }

  public GpuCommandPoly rgb(final int vertex, final int r, final int g, final int b) {
    return this.rgb(vertex, b << 16 | g << 8 | r);
  }

  public GpuCommandPoly monochrome(final int vertex, final int colour) {
    return this.rgb(vertex, colour, colour, colour);
  }

  public GpuCommandPoly pos(final int vertex, final int x, final int y) {
    if(vertex == 2 && Math.abs(x - this.x[0]) >= 3000 || vertex == 3 && Math.abs(y - this.y[1]) >= 1500) {
      System.err.println("BAD COORD %d, %d".formatted(x, y));
      new Throwable().printStackTrace();
    }

    this.x[vertex] = x;
    this.y[vertex] = y;
    return this;
  }

  public int getX(final int vertex) {
    return this.x[vertex];
  }

  public int getY(final int vertex) {
    return this.y[vertex];
  }

  public GpuCommandPoly uv(final int vertex, final int u, final int v) {
    this.u[vertex] = u;
    this.v[vertex] = v;
    this.textured = true;
    return this;
  }

  public GpuCommandPoly clut(final int x, final int y) {
    if(y > 511) {
      throw new IllegalArgumentException("Invalid y");
    }

    this.clutX = x;
    this.clutY = y;
    this.textured = true;
    return this;
  }

  public GpuCommandPoly vramPos(final int x, final int y) {
    this.vramX = x;
    this.vramY = y;
    this.textured = true;
    return this;
  }

  @Override
  public void render(final Gpu gpu) {
    for(int i = 0; i < this.vertexCount; i++) {
      this.x[i] += gpu.getOffsetX();
      this.y[i] += gpu.getOffsetY();
    }

    gpu.rasterizeTriangle(this.x[0], this.y[0], this.x[1], this.y[1], this.x[2], this.y[2], this.u[0], this.v[0], this.u[1], this.v[1], this.u[2], this.v[2], this.colour[0], this.colour[1], this.colour[2], this.clutX, this.clutY, this.vramX, this.vramY, this.bpp, this.textured, this.shaded, this.translucence != null, this.raw, this.translucence);

    if(this.vertexCount == 4) {
      gpu.rasterizeTriangle(this.x[1], this.y[1], this.x[2], this.y[2], this.x[3], this.y[3], this.u[1], this.v[1], this.u[2], this.v[2], this.u[3], this.v[3], this.colour[1], this.colour[2], this.colour[3], this.clutX, this.clutY, this.vramX, this.vramY, this.bpp, this.textured, this.shaded, this.translucence != null, this.raw, this.translucence);
    }
  }
}
