package legend.game.fmv;

import legend.core.MathHelper;

public class FrameHeader {
  private final byte[] data;

  public FrameHeader(final byte[] data) {
    this.data = data;
  }

  public int getRunLengthCodesCount() {
    return MathHelper.getShort(this.data, 0);
  }

  public int getWidth() {
    return MathHelper.getShort(this.data, 4);
  }

  public int getHeight() {
    return MathHelper.getShort(this.data, 6);
  }

  public int getCompressedCodesSize() {
    return MathHelper.getShort(this.data, 8);
  }
}
