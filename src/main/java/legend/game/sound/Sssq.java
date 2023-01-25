package legend.game.sound;

import legend.core.MathHelper;

import java.util.Arrays;

public class Sssq implements Sshd.Subfile {
  public static final int MAGIC = 0x7173_5353; //SSsq

  public int volume_00;
  public final int ticksPerQuarterNote_02;
  public final int tempo_04;

  public final Entry[] entries_10;

  private final byte[] data;
  private final int offset;

  public Sssq(final byte[] data, final int offset) {
    if(MathHelper.getInt(data, offset + 0xc) != MAGIC) {
      throw new IllegalArgumentException("Invalid file magic");
    }

    this.volume_00 = MathHelper.getUbyte(data, offset);
    this.ticksPerQuarterNote_02 = MathHelper.getUshort(data, offset + 0x2);
    this.tempo_04 = MathHelper.getUshort(data, offset + 0x4);

    this.entries_10 = new Entry[16];
    Arrays.setAll(this.entries_10, i -> new Entry(data, offset + 0x10 + i * 0x10));

    this.data = data;
    this.offset = offset;
  }

  public Sssq(final byte[] data) {
    this(data, 0);
  }

  public Reader reader() {
    return new Reader();
  }

  public static class Entry {
    /** -1 means none */
    public int patchNumber_02;
    public int volume_03;
    /** 0x40 seems to be middle - most common value. That would make 0 left, and 0x7f right. */
    public int pan_04;

    public int modulation_09;
    public int pitchBend_0a;
    public int _0b;
    public int breath_0c;

    public int volume_0e;

    public Entry(final byte[] data, final int offset) {
      this.patchNumber_02 = MathHelper.getByte(data, offset + 0x2);
      this.volume_03 = MathHelper.getUbyte(data, offset + 0x3);
      this.pan_04 = MathHelper.getUbyte(data, offset + 0x4);

      this.modulation_09 = MathHelper.getUbyte(data, offset + 0x9);
      this.pitchBend_0a = MathHelper.getUbyte(data, offset + 0xa);
      this._0b = MathHelper.getUbyte(data, offset + 0xb);
      this.breath_0c = MathHelper.getUbyte(data, offset + 0xc);

      this.volume_0e = MathHelper.getUbyte(data, offset + 0xe);
    }
  }

  public class Reader implements SssqReader {
    private int offset = 0x10 + Sssq.this.entries_10.length * 0x10;

    @Override
    public int readByteAbsolute(final int absoluteOffset) {
      return Sssq.this.data[Sssq.this.offset + absoluteOffset] & 0xff;
    }

    @Override
    public int offset() {
      return this.offset;
    }

    @Override
    public void jump(final int offset) {
      this.offset = offset;
    }

    @Override
    public Entry entry(final int index) {
      return Sssq.this.entries_10[index];
    }

    @Override
    public int baseVolume() {
      return Sssq.this.volume_00;
    }

    @Override
    public void baseVolume(final int volume) {
      Sssq.this.volume_00 = volume;
    }
  }
}
