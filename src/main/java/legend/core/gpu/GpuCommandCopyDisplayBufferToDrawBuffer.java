package legend.core.gpu;

public class GpuCommandCopyDisplayBufferToDrawBuffer extends GpuCommand {
  private final int sourceX;
  private final int sourceY;
  private final int destX;
  private final int destY;
  private final int width;
  private final int height;

  public GpuCommandCopyDisplayBufferToDrawBuffer(final int sourceX, final int sourceY, final int destX, final int destY, final int width, final int height) {
    if(sourceX < 0) {
      throw new IllegalArgumentException("Negative sourceX " + sourceX);
    }

    if(sourceY < 0) {
      throw new IllegalArgumentException("Negative sourceY " + sourceY);
    }

    if(destX < 0) {
      throw new IllegalArgumentException("Negative destX " + destX);
    }

    if(destY < 0) {
      throw new IllegalArgumentException("Negative destY " + destY);
    }

    this.sourceX = sourceX;
    this.sourceY = sourceY;
    this.destX = destX;
    this.destY = destY;
    this.width = width;
    this.height = height;
  }

  @Override
  public void render(final Gpu gpu) {
    final int[] data = new int[this.width * gpu.getScale() * this.height * gpu.getScale()];
    gpu.getDisplayBuffer().getRegion(new Rect4i(this.sourceX * gpu.getScale(), this.sourceY * gpu.getScale(), this.width * gpu.getScale(), this.height * gpu.getScale()), data);
    gpu.getDrawBuffer().setRegion(new Rect4i(this.destX * gpu.getScale(), this.destY * gpu.getScale(), this.width * gpu.getScale(), this.height * gpu.getScale()), data);
  }
}
