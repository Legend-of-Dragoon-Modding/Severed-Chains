package legend.core.gpu;

public class GpuCommandCopyVramToDrawBuffer extends GpuCommand {
  private final int sourceX;
  private final int sourceY;
  private final int destX;
  private final int destY;
  private final int width;
  private final int height;

  public GpuCommandCopyVramToDrawBuffer(final int sourceX, final int sourceY, final int destX, final int destY, final int width, final int height) {
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
    final int scale = gpu.getScale();
    final int[] data = new int[this.width * scale * this.height * scale];

    for(int y = 0; y < this.height; y++) {
      for(int x = 0; x < this.width; x++) {
        final int pixel = gpu.getPixel(this.sourceX + x, this.sourceY + y);

        for(int yy = 0; yy < scale; yy++) {
          for(int xx = 0; xx < scale; xx++) {
            data[(y * scale + yy) * this.width + x * scale + xx] = pixel;
          }
        }
      }
    }

    gpu.getDrawBuffer().setRegion(new Rect4i(this.destX * scale, this.destY * scale, this.width * scale, this.height * scale), data);
  }
}
