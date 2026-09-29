package legend.game.types;

public class Flags {
  private int[] flags;
  private long revision;

  /** Changes through both the script ABI and typed APIs share this revision. */
  public long revision() {
    return this.revision;
  }

  public void ensureCapacity(final int count) {
    if(count > this.flags.length) {
      this.flags = java.util.Arrays.copyOf(this.flags, count);
    }
  }

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
    if(this.flags[index] == val) return;
    this.flags[index] = val;
    this.revision++;
  }

  public boolean get(final int index, final int bit) {
    return (this.flags[index] & 0x1 << bit) != 0;
  }

  public void set(final int index, final int bit, final boolean value) {
    this.setRaw(index, value ? this.flags[index] | 0x1 << bit : this.flags[index] & ~(0x1 << bit));
  }

  public boolean get(final int packedIndex) {
    return this.get(packedIndex >>> 5, packedIndex & 0x1f);
  }

  public void set(final int packedIndex, final boolean value) {
    this.set(packedIndex >>> 5, packedIndex & 0x1f, value);
  }

  public void set(final Flags other) {
    this.ensureCapacity(other.flags.length);
    for(int i = 0; i < this.flags.length; i++) {
      this.setRaw(i, i < other.flags.length ? other.flags[i] : 0);
    }
  }
}
