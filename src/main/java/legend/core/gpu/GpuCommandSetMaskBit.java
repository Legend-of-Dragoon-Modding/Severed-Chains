package legend.core.gpu;

public class GpuCommandSetMaskBit extends GpuCommand {
  private final boolean setMaskBit;
  private final Gpu.DRAW_PIXELS drawPixels;

  public GpuCommandSetMaskBit(final boolean setMaskBit, final Gpu.DRAW_PIXELS drawPixels) {
    this.setMaskBit = setMaskBit;
    this.drawPixels = drawPixels;
  }

  @Override
  public void render(final Gpu gpu) {
    gpu.status.setMaskBit = this.setMaskBit;
    gpu.status.drawPixels = this.drawPixels;
  }
}
