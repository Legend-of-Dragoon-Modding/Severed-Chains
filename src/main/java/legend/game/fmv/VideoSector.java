package legend.game.fmv;

import legend.core.MathHelper;

public class VideoSector {
  private final byte[] data;

  public VideoSector(final byte[] data) {
    this.data = data;
  }

  public int getChunkNumber() {
    return (int)MathHelper.get(this.data, SectorHeader.HEADER_SIZE + 4, 2);
  }

  public int getChunkCount() {
    return (int)MathHelper.get(this.data, SectorHeader.HEADER_SIZE + 6, 2);
  }

  public int getFrameNumber() {
    return (int)MathHelper.get(this.data, SectorHeader.HEADER_SIZE + 8, 4);
  }

  public int getDemuxedSize() {
    return (int)MathHelper.get(this.data, SectorHeader.HEADER_SIZE + 12, 4);
  }

  public int getFrameWidth() {
    return (int)MathHelper.get(this.data, SectorHeader.HEADER_SIZE + 16, 2);
  }

  public int getFrameHeight() {
    return (int)MathHelper.get(this.data, SectorHeader.HEADER_SIZE + 18, 2);
  }

  /** Number of 32-byte blocks to hold uncompressed MDEC codes */
  public int getUncompressedBlockCount() {
    return (int)MathHelper.get(this.data, SectorHeader.HEADER_SIZE + 20, 2);
  }

  public void readSector(final byte[] out, final int sectorIndex) {
    System.arraycopy(this.data, SectorHeader.HEADER_SIZE + 32, out, 2016 * sectorIndex, 2016);
  }
}
