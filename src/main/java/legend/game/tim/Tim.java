package legend.game.tim;

import legend.core.gpu.RECT;
import legend.game.unpacker.FileData;

import static legend.core.GameEngine.GPU;

public class Tim {
  private final FileData data;

  public Tim(final FileData data) {
    this.data = data;
  }

  public FileData getData() {
    return this.data;
  }

  private int getImageDataOffset() {
    if(this.hasClut()) {
      return this.data.readUShort(0x8);
    }

    return 0;
  }

  public int getFlags() {
    return this.data.readInt(0x4);
  }

  public boolean hasClut() {
    return (this.getFlags() & 0b1000) != 0;
  }

  public RECT getClutRect() {
    if(!this.hasClut()) {
      throw new IllegalStateException("TIM has no CLUT");
    }

    return this.data.readRect(0xc, new RECT());
  }

  private int getClutOffset() {
    if(!this.hasClut()) {
      throw new IllegalStateException("TIM has no CLUT");
    }

    return 0x14;
  }

  public FileData getClutData() {
    return this.data.slice(this.getClutOffset());
  }

  public RECT getImageRect() {
    return this.data.readRect(this.getImageDataOffset() + 0xc, new RECT());
  }

  private int getImageOffset() {
    return this.getImageDataOffset() + 0x14;
  }

  public FileData getImageData() {
    return this.data.slice(this.getImageOffset());
  }

  public void uploadToGpu() {
    GPU.uploadData(this.getImageRect(), this.getImageData());

    if(this.hasClut()) {
      GPU.uploadData(this.getClutRect(), this.getClutData());
    }
  }
}
