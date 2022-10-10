package legend.core.gpu;

public class VramTexture {
  public final int x;
  public final int y;
  public final int width;
  public final int height;
  public final int originalWidth;
  public final int originalHeight;
  public final Bpp bpp;

  private final int scale;

  private final int[] data;

  public VramTexture(final int x, final int y, final int width, final int height, final Bpp bpp, final byte[] data) {
    this.x = x;
    this.y = y;
    this.originalWidth = width;
    this.originalHeight = height;
    this.bpp = bpp;

    switch(bpp) {
      case BITS_4 -> {
        this.scale = 4;
        this.width = width * 4;
        this.height = height;
        this.data = new int[this.width * this.height];

        for(int i = 0, n = 0; i < data.length; i++) {
          this.data[n    ] = data[i / 2] & 0xf;
          this.data[n + 2] = data[i / 2] >> 4 & 0xf;
          this.data[n + 1] = this.data[n    ];
          this.data[n + 3] = this.data[n + 2];
        }
      }

      case BITS_8 -> {
        this.scale = 2;
        this.width = width * 2;
        this.height = height;
        this.data = new int[this.width * this.height];

        for(int i = 0; i < data.length; i++) {
          this.data[i] = data[i];
        }
      }

      //TODO
      default -> {
        this.scale = 1;
        this.width = 1;
        this.height = 1;
        this.data = new int[1];
      }
    }
  }

  public int adjustX(final int pageX) {
    return (pageX - this.x) * this.scale;
  }

  public int adjustY(final int pageY) {
    return (pageY - this.y) * this.scale;
  }

  public boolean containsPixel(final int x, final int y) {
    return x >= this.x && y >= this.y && x < this.x + this.width && y < this.y + this.height;
  }

  public int getPixel(final int x, final int y) {
    return this.data[(y - this.y) * this.width + x - this.x];
  }
}
