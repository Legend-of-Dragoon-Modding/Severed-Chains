package legend.core.gpu;

public class GpuCommandCopyVramToVram extends GpuCommand {
  private final int sourceX;
  private final int sourceY;
  private final int destX;
  private final int destY;
  private final int width;
  private final int height;

  public GpuCommandCopyVramToVram(final int sourceX, final int sourceY, final int destX, final int destY, final int width, final int height) {
    this.sourceX = sourceX;
    this.sourceY = sourceY;
    this.destX = destX;
    this.destY = destY;
    this.width = width;
    this.height = height;
  }

  @Override
  public void render(final Gpu gpu) {
    gpu.command80CopyRectFromVramToVram(this.sourceX, this.sourceY, this.destX, this.destY, this.width, this.height);
  }
}
