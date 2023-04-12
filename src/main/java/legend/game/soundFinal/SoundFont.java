package legend.game.soundFinal;

import legend.core.IoHelper;

final class SoundFont {
  private final Preset[] presets;

  SoundFont(final byte[] data, final SoundBank soundBank) {
    final int presetCount = IoHelper.readShort(data, 0) + 1;
    this.presets = new Preset[presetCount];
    final short[] presetOffsets = new short[presetCount];
    for(int i = 0; i < presetCount; i++) {
      presetOffsets[i] = IoHelper.readShort(data, i * 2 + 2);
    }

    for(int i = 0; i < presetCount; i++) {
      if(presetOffsets[i] == -1) {
        continue;
      }

      final byte[] presetData = new byte[data.length - presetOffsets[i]];
      System.arraycopy(data, presetOffsets[i], presetData, 0, presetData.length);

      this.presets[i] = new Preset(presetData, soundBank);
    }
  }

  Preset getPreset(final int index) {
    return this.presets[index];
  }
}

