package legend.game.soundFinal;

import legend.game.unpacker.FileData;

final class Sshd {
  private final SoundFont soundFont;

  Sshd(final FileData sshd, final SoundBank soundBank) {
    assert sshd.readInt(12) == 0x53536864 : "Not a SShd file!";

    final int[] subfileOffsets = new int[24];
    for(int i = 0; i < 24; i++) {
      subfileOffsets[i] = sshd.readInt(16 + i * 4);
    }

    if(subfileOffsets[0] != -1) {
      final byte[] data = new byte[sshd.size() - subfileOffsets[0]];
      sshd.copyFrom(subfileOffsets[0], data, 0, data.length);

      this.soundFont = new SoundFont(data, soundBank);
    } else {
      final byte[] data = new byte[sshd.size() - subfileOffsets[4]];
      sshd.copyFrom(subfileOffsets[4], data, 0, data.length);

      this.soundFont = new SoundFont(data, soundBank);
    }
  }

  Preset getPreset(final int index) {
    return this.soundFont.getPreset(index);
  }
}
