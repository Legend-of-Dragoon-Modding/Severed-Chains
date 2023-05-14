package legend.core.gpu;

public class GpuCommandFillVram extends GpuCommand {
  private final int colour;

  public GpuCommandFillVram(final int colour) {
    this.colour = colour;
  }

  public GpuCommandFillVram(final int r, final int g, final int b) {
    this(b << 16 | g << 8 | r);
  }

  @Override
  public void render(final Gpu gpu) {
    gpu.clear(this.colour);
  }
}
