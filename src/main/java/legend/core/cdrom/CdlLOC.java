package legend.core.cdrom;

import legend.core.memory.Value;

import javax.annotation.Nullable;

import static legend.core.MathHelper.fromBcd;
import static legend.core.MathHelper.toBcd;

public class CdlLOC {
  @Nullable
  private final Value ref;

  private long minute;
  private long second;
  private long sector;
  private long track;

  public CdlLOC() {
    this.ref = null;
  }

  public CdlLOC(final Value ref) {
    this.ref = ref;
  }

  public long getMinute() {
    if(this.ref != null) {
      return fromBcd(this.ref.offset(1, 0x0L).get());
    }

    return this.minute;
  }

  public void setMinute(final long minute) {
    if(this.ref != null) {
      this.ref.offset(1, 0x0L).setu(toBcd(minute));
      return;
    }

    this.minute = minute;
  }

  public long getSecond() {
    if(this.ref != null) {
      return fromBcd(this.ref.offset(1, 0x1L).get());
    }

    return this.second;
  }

  public void setSecond(final long second) {
    if(this.ref != null) {
      this.ref.offset(1, 0x1L).setu(toBcd(second));
      return;
    }

    this.second = second;
  }

  public long getSector() {
    if(this.ref != null) {
      return fromBcd(this.ref.offset(1, 0x2L).get());
    }

    return this.sector;
  }

  public void setSector(final long sector) {
    if(this.ref != null) {
      this.ref.offset(1, 0x2L).setu(toBcd(sector));
      return;
    }

    this.sector = sector;
  }

  public long getTrack() {
    if(this.ref != null) {
      return fromBcd(this.ref.offset(1, 0x3L).get());
    }

    return this.track;
  }

  public void setTrack(final long track) {
    if(this.ref != null) {
      this.ref.offset(1, 0x3L).setu(toBcd(track));
      return;
    }

    this.track = track;
  }

  public CdlLOC set(final CdlLOC other) {
    this.setMinute(other.getMinute());
    this.setSecond(other.getSecond());
    this.setSector(other.getSector());
    this.setTrack(other.getTrack());
    return this;
  }

  public void advance(final int sectors) {
    long sector = this.getSector() + sectors;
    long second = this.getSecond();
    long minute = this.getMinute();

    while(sector >= 75) {
      sector -= 75;
      second++;
    }

    while(second >= 60) {
      second -= 60;
      minute++;
    }

    this.setSector(sector);
    this.setSecond(second);
    this.setMinute(minute);
  }

  /**
   * 800371a0
   *
   * Replacement for DsIntToPos
   *
   * The absolute sector number specified by <pre>packed</pre> is converted to minutes, seconds, and sectors and the result is returned
   *
   * @param packed Absolute sector number
   */
  public CdlLOC unpack(long packed) {
    packed += 150;
    this.setSector(packed % 75);
    final long remainder = packed / 75;
    this.setSecond(remainder % 60);
    this.setMinute(remainder / 60);
    return this;
  }

  /**
   * 800372b0
   *
   * Replacement for DsPosToInt
   *
   * Calculates the absolute sector number from the minutes, seconds, and sectors in the DslLOC.
   */
  public long pack() {
    final long min = this.getMinute();
    final long sec = this.getSecond();
    final long sect = this.getSector();

    assert sect < 75;
    assert sec < 60;

    return 75L * (60L * min + sec) + sect - 150L;
  }

  @Override
  public String toString() {
    return this.getMinute() + ":" + this.getSecond() + ':' + this.getSector() + ':' + this.getTrack() + " (sector " + this.pack() + ')';
  }
}
