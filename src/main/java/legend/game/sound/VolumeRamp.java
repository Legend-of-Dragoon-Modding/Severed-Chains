package legend.game.sound;

import legend.game.unpacker.FileData;

import java.util.Arrays;

public class VolumeRamp implements Sshd.Subfile {
  public int _00;
  public final int[] ramp_02;

  public VolumeRamp(final FileData data, final int offset) {
    this._00 = data.readUShort(offset);
    this.ramp_02 = new int[0x80];
    Arrays.setAll(this.ramp_02, i -> data.readUByte(offset + 2 + i));
  }
}
