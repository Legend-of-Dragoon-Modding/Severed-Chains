package legend.game.tim;

import legend.core.gpu.Bpp;
import legend.core.gpu.Rect4i;
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

  public int getImageDataOffset() {
    if(this.hasClut()) {
      return this.data.readUShort(0x8);
    }

    return 0;
  }

  public int getFlags() {
    return this.data.readInt(0x4);
  }

  public Bpp getBpp() {
    return Bpp.of(this.getFlags() & 0b111);
  }

  public boolean hasClut() {
    return (this.getFlags() & 0b1000) != 0;
  }

  public Rect4i getClutRect() {
    if(!this.hasClut()) {
      throw new IllegalStateException("TIM has no CLUT");
    }

    return this.data.readRect(0xc, new Rect4i());
  }

  private int getClutOffset() {
    if(!this.hasClut()) {
      throw new IllegalStateException("TIM has no CLUT");
    }

    return 0x14;
  }

  public FileData getClutData() {
    return this.data.slice(this.getClutOffset(), this.data.readInt(0x8) - 0xc);
  }

  public Rect4i getImageRect() {
    return this.data.readRect(this.getImageDataOffset() + 0xc, new Rect4i());
  }

  private int getImageOffset() {
    return this.getImageDataOffset() + 0x14;
  }

  public FileData getImageData() {
    return this.data.slice(this.getImageOffset(), this.data.readInt(this.getImageDataOffset() + 0x8) - 0xc);
  }

  public void uploadToGpu() {
    GPU.uploadData15(this.getImageRect(), this.getImageData());

    if(this.hasClut()) {
      GPU.uploadData15(this.getClutRect(), this.getClutData());
    }
  }
}
