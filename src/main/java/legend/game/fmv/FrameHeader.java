package legend.game.fmv;

import legend.core.MathHelper;

public class FrameHeader {
  private final byte[] data;

  public FrameHeader(final byte[] data) {
    this.data = data;
  }

  public int getRunLengthCodesCount() {
    return (int)MathHelper.get(this.data, 0, 2);
  }

  public int getWidth() {
    return (int)MathHelper.get(this.data, 4, 2);
  }

  public int getHeight() {
    return (int)MathHelper.get(this.data, 6, 2);
  }

  public int getCompressedCodesSize() {
    return (int)MathHelper.get(this.data, 8, 2);
  }
}
