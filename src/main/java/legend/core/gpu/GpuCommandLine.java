package legend.core.gpu;

import legend.game.types.Translucency;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Math;

public class GpuCommandLine extends GpuCommand {
  private static final Logger LOGGER = LogManager.getFormatterLogger(GpuCommandLine.class);

  private Translucency translucence;

  private final int[] x = new int[2];
  private final int[] y = new int[2];
  private final int[] colour = new int[2];

  public final GpuCommandLine translucent(final Translucency trans) {
    this.translucence = trans;
    return this;
  }

  public GpuCommandLine rgb(final int vertex, final int colour) {
    this.colour[vertex] = colour;
    return this;
  }

  public GpuCommandLine rgb(final int vertex, final int r, final int g, final int b) {
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

  public GpuCommandLine monochrome(final int vertex, final int colour) {
    return this.rgb(vertex, colour, colour, colour);
  }

  public GpuCommandLine rgb(final int colour) {
    for(int i = 0; i < 2; i++){
      this.colour[i] = colour;
    }

    return this;
  }

  public GpuCommandLine rgb(final int r, final int g, final int b) {
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

  public GpuCommandLine monochrome(final int colour) {
    return this.rgb(colour, colour, colour);
  }

  public GpuCommandLine pos(final int vertex, final float x, final float y) {
    return this.pos(vertex, Math.round(x), Math.round(y));
  }

  public GpuCommandLine pos(final int vertex, final int x, final int y) {
    this.x[vertex] = x;
    this.y[vertex] = y;
    return this;
  }

  @Override
  public void render(final Gpu gpu) {
    gpu.rasterizeLine(this.x[0], this.y[0], this.x[1], this.y[1], this.colour[0], this.colour[1], this.translucence);
  }
}
