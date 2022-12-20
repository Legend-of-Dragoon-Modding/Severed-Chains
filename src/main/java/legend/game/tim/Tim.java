package legend.game.tim;

import legend.core.MathHelper;
import legend.core.gpu.RECT;

import static legend.core.GameEngine.GPU;

public class Tim {
  private final byte[] data;
  private final int offset;

  public Tim(final byte[] data) {
    this(data, 0);
  }

  public Tim(final byte[] data, final int offset) {
    this.data = data;
    this.offset = offset;
  }

  public byte[] getData() {
    return this.data;
  }

  private int getImageDataOffset() {
    if(this.hasClut()) {
      return (int)MathHelper.get(this.data, this.offset + 0x8, 2);
    }

    return 0;
  }

  public int getFlags() {
    return (int)MathHelper.get(this.data, this.offset + 0x4, 4);
  }

  public boolean hasClut() {
    return (this.getFlags() & 0b1000) != 0;
  }

  public RECT getClutRect() {
    if(!this.hasClut()) {
      throw new IllegalStateException("TIM has no CLUT");
    }

    return new RECT(
      (short)MathHelper.get(this.data, this.offset + 0x0c, 2),
      (short)MathHelper.get(this.data, this.offset + 0x0e, 2),
      (short)MathHelper.get(this.data, this.offset + 0x10, 2),
      (short)MathHelper.get(this.data, this.offset + 0x12, 2)
    );
  }

  public int getClutData() {
    if(!this.hasClut()) {
      throw new IllegalStateException("TIM has no CLUT");
    }

    return this.offset + 0x14;
  }

  public RECT getImageRect() {
    final int offset = this.offset + this.getImageDataOffset();

    return new RECT(
      (short)MathHelper.get(this.data, offset + 0x0c, 2),
      (short)MathHelper.get(this.data, offset + 0x0e, 2),
      (short)MathHelper.get(this.data, offset + 0x10, 2),
      (short)MathHelper.get(this.data, offset + 0x12, 2)
    );
  }

  public int getImageData() {
    return this.offset + this.getImageDataOffset() + 0x14;
  }

  public void uploadToGpu() {
    GPU.uploadData(this.getImageRect(), this.data, this.getImageData());

    if(this.hasClut()) {
      GPU.uploadData(this.getClutRect(), this.data, this.getClutData());
    }
  }
}
