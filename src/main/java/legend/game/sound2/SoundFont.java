package legend.game.sound2;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

final class SoundFont {
  private final Preset[] presets;
  SoundFont(final ByteBuffer data, final SoundBank soundBank) {
    final int presetCount = data.getShort() + 1;
    this.presets = new Preset[presetCount];
    final short[] presetOffset = new short[presetCount];
    for(int i = 0; i < presetCount; i++) {
      presetOffset[i] = data.getShort();
    }

    for(int i = 0; i < presetCount; i++) {
      if(presetOffset[i] == -1) {
        continue;
      }

      this.presets[i] = new Preset(data.slice(presetOffset[i], data.limit() - presetOffset[i]).order(ByteOrder.LITTLE_ENDIAN), soundBank);
    }
  }


  public Preset getPreset(final int index) {
    return this.presets[index];
  }
}
