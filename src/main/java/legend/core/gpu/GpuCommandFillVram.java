package legend.core.gpu;

public class GpuCommandFillVram extends GpuCommand {
  private final int x;
  private final int y;
  private final int width;
  private final int height;
  private final int colour;

  public GpuCommandFillVram(final int x, final int y, final int width, final int height, final int colour) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.colour = colour;
  }

  public GpuCommandFillVram(final int x, final int y, final int width, final int height, final int r, final int g, final int b) {
    this(x, y, width, height, b << 16 | g << 8 | r);
  }

  @Override
  public void render(final Gpu gpu) {
    gpu.command02FillRect(this.x, this.y, this.width, this.height, this.colour);
  }
}
