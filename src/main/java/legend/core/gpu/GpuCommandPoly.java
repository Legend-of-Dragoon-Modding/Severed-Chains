package legend.core.gpu;

import legend.game.types.Translucency;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Math;
import org.joml.Vector3i;

import java.util.Arrays;

public class GpuCommandPoly extends GpuCommand {
  private static final Logger LOGGER = LogManager.getFormatterLogger(GpuCommandPoly.class);

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

  private VramTexture texture;
  private VramTexture[] palettes;

  public GpuCommandPoly(final int vertexCount) {
    this.vertexCount = vertexCount;
    this.x = new int[vertexCount];
    this.y = new int[vertexCount];
    this.u = new int[vertexCount];
    this.v = new int[vertexCount];
    this.colour = new int[vertexCount];
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

  public GpuCommandPoly rgb(final Vector3i colour) {
    return this.rgb(colour.x, colour.y, colour.z);
  }

  public GpuCommandPoly rgb(final int vertex, final Vector3i colour) {
    return this.rgb(vertex, colour.x, colour.y, colour.z);
  }

  public GpuCommandPoly rgb(final int colour) {
    Arrays.fill(this.colour, colour);
    return this;
  }

  public GpuCommandPoly rgb(final int r, final int g, final int b) {
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

  public GpuCommandPoly monochrome(final int colour) {
    return this.rgb(colour, colour, colour);
  }

  public GpuCommandPoly monochrome(final float colour) {
    return this.monochrome((int)(colour * 0x100));
  }

  public GpuCommandPoly rgb(final int vertex, final int colour) {
    this.shaded = true;
    this.colour[vertex] = colour;
    return this;
  }

  public GpuCommandPoly rgb(final int vertex, final int r, final int g, final int b) {
    if(r < 0) {
      LOGGER.warn("Negative R! %x", r);
    }

    if(g < 0) {
      LOGGER.warn("Negative G! %x", g);
    }

    if(b < 0) {
      LOGGER.warn("Negative B! %x", b);
    }

    return this.rgb(vertex, b << 16 | g << 8 | r);
  }

  public GpuCommandPoly monochrome(final int vertex, final int colour) {
    return this.rgb(vertex, colour, colour, colour);
  }

  public GpuCommandPoly pos(final int vertex, final float x, final float y) {
    return this.pos(vertex, Math.round(x), Math.round(y));
  }

  public GpuCommandPoly pos(final int vertex, final int x, final int y) {
    if(vertex == 2 && Math.abs(x - this.x[0]) >= 3000 || vertex == 3 && Math.abs(y - this.y[1]) >= 1500) {
//      System.err.println("BAD COORD %d, %d".formatted(x, y));
//      new Throwable().printStackTrace();
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
      throw new IllegalArgumentException("Invalid y " + y);
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

  public GpuCommandPoly texture(final VramTexture texture) {
    this.texture = texture;
    return this;
  }

  public GpuCommandPoly texture(final VramTexture texture, final VramTexture[] palettes) {
    this.texture = texture;
    this.palettes = palettes;
    return this;
  }

  @Override
  public void render(final Gpu gpu) {
    for(int i = 0; i < this.vertexCount; i++) {
      this.x[i] += gpu.getOffsetX();
      this.y[i] += gpu.getOffsetY();
    }

    gpu.rasterizeTriangle(this.x[0], this.y[0], this.x[1], this.y[1], this.x[2], this.y[2], this.u[0], this.v[0], this.u[1], this.v[1], this.u[2], this.v[2], this.colour[0], this.colour[1], this.colour[2], this.clutX, this.clutY, this.vramX, this.vramY, this.bpp, this.textured, this.shaded, this.translucence != null, this.raw, this.translucence, this.texture, this.palettes);

    if(this.vertexCount == 4) {
      gpu.rasterizeTriangle(this.x[1], this.y[1], this.x[2], this.y[2], this.x[3], this.y[3], this.u[1], this.v[1], this.u[2], this.v[2], this.u[3], this.v[3], this.colour[1], this.colour[2], this.colour[3], this.clutX, this.clutY, this.vramX, this.vramY, this.bpp, this.textured, this.shaded, this.translucence != null, this.raw, this.translucence, this.texture, this.palettes);
    }
  }
}
