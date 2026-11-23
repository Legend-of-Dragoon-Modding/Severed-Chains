package legend.core.gpu;

public class GpuCommandCopyVramToVram {
  public final int sourceX;
  public final int sourceY;
  public final int destX;
  public final int destY;
  public final int width;
  public final int height;

  public GpuCommandCopyVramToVram(final int sourceX, final int sourceY, final int destX, final int destY, final int width, final int height) {
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
}
