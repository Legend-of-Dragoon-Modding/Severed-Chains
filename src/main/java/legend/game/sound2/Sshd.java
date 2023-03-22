package legend.game.sound2;

import legend.game.unpacker.FileData;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

final class Sshd {
  private static final byte[] SSHD = {0x53, 0x53, 0x68, 0x64};
  private final SoundFont soundFont;

  Sshd(final FileData sshd, final SoundBank soundBank) {
    final ByteBuffer data = ByteBuffer.wrap(sshd.data()).order(ByteOrder.LITTLE_ENDIAN);

    data.position(12);
    final byte[] sshdFlag = new byte[4];
    data.get(sshdFlag);

    assert Arrays.equals(sshdFlag, SSHD) : "Not a SShd file.";

    final int[] subfileOffsets = new int[24];
    for(int i = 0; i < 24; i++) {
      subfileOffsets[i] = data.getInt();
    }

    if(subfileOffsets[0] != -1) {
      this.soundFont = new SoundFont(data.slice(subfileOffsets[0], data.limit() - subfileOffsets[0]).order(ByteOrder.LITTLE_ENDIAN), soundBank);
    } else {
      this.soundFont = new SoundFont(data.slice(subfileOffsets[4], data.limit() - subfileOffsets[4]).order(ByteOrder.LITTLE_ENDIAN), soundBank);
    }
  }

  Preset getPreset(final int index) {
    return this.soundFont.getPreset(index);
  }
}
