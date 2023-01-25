package legend.game.sound;

import legend.core.MathHelper;

import java.util.Arrays;

public class VolumeRamp implements Sshd.Subfile {
  public int _00;
  public final int[] ramp_02;

  public VolumeRamp(final byte[] data, final int offset) {
    this._00 = MathHelper.getUshort(data, offset);
    this.ramp_02 = new int[0x80];
    Arrays.setAll(this.ramp_02, i -> MathHelper.getUbyte(data, offset + 2 + i));
  }
}
