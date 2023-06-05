package legend.game.sound;

import legend.game.unpacker.FileData;

import java.util.Arrays;

public class WaveformList implements Sshd.Subfile {
  public final int count_00;
  public final Waveform[] waveforms_02;

  public WaveformList(final FileData data, final int offset) {
    this.count_00 = data.readUShort(offset);
    this.waveforms_02 = new Waveform[this.count_00 + 1];
    Arrays.setAll(this.waveforms_02, i -> new Waveform(data, offset + 2 + i * 2));
  }
}
