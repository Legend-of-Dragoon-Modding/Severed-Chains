package legend.game.sound;

import legend.core.MathHelper;

public class Waveform implements Sshd.Subfile {
  public final int[] _00;

  public Waveform(final byte[] data, final int offset) {
    this._00 = new int[0x40];

    for(int i = 0; i < this._00.length; i++) {
      this._00[i] = MathHelper.getUbyte(data, offset + i);
    }
  }
}
