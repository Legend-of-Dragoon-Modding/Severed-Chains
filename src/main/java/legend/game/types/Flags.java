package legend.game.types;

public class Flags {
  private final int[] flags;

  public Flags(final int count) {
    this.flags = new int[count];
  }

  public int count() {
    return this.flags.length;
  }

  public int getRaw(final int index) {
    return this.flags[index];
  }

  public void setRaw(final int index, final int val) {
    this.flags[index] = val;
  }

  public boolean get(final int index, final int bit) {
    return (this.flags[index] & 0x1 << bit) != 0;
  }

  public void set(final int index, final int bit, final boolean value) {
    if(value) {
      this.flags[index] |= 0x1 << bit;
    } else {
      this.flags[index] &= ~(0x1 << bit);
    }
  }

  public boolean get(final int packedIndex) {
    return this.get(packedIndex >>> 5, packedIndex & 0x1f);
  }

  public void set(final int packedIndex, final boolean value) {
    this.set(packedIndex >>> 5, packedIndex & 0x1f, value);
  }

  public void set(final Flags other) {
    System.arraycopy(other.flags, 0, this.flags, 0, this.flags.length);
  }
}
