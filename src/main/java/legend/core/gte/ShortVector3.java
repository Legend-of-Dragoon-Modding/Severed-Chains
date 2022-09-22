package legend.core.gte;

import legend.core.IoHelper;

import java.nio.ByteBuffer;

class ShortVector3 {
  public short x;
  public short y;
  public short z;

  public ShortVector3() { }

  public ShortVector3(final short x, final short y, final short z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public void setXY(final int xy) {
    this.x = (short)(xy & 0xffff);
    this.y = (short)(xy >>> 16);
  }

  public int getXY() {
    return (this.y & 0xffff) << 16 | this.x & 0xffff;
  }

  public ShortVector3 copy() {
    return new ShortVector3(this.x, this.y, this.z);
  }

  public void dump(final ByteBuffer stream) {
    IoHelper.write(stream, this.x);
    IoHelper.write(stream, this.y);
    IoHelper.write(stream, this.z);
  }

  public void load(final ByteBuffer stream) {
    this.x = IoHelper.readShort(stream);
    this.y = IoHelper.readShort(stream);
    this.z = IoHelper.readShort(stream);
  }
}
