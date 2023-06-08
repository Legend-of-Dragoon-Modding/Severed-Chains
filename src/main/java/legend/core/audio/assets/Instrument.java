package legend.core.audio.assets;

import legend.game.unpacker.FileData;

public final class Instrument {
  private final Type type;
  private final InstrumentLayer[] layers;

  private final int volume;
  private final int pan;
  // 0x03 unused
  private final int pitchBendMultiplier;
  private final int breathControlIndex;
  /** For SFX only */
  private final int startingNote;

  Instrument(final FileData data, final SoundBank soundBank) {
    final int upperBoundByte = data.readUByte(0x00);
    this.type = Type.getType(upperBoundByte);

    final int layerCount = this.type == Type.SFX ? (data.size() - 8) / 16 : (upperBoundByte & 0x7F) + 1;
    this.layers = new InstrumentLayer[layerCount];
    for(int layer = 0; layer < this.layers.length; layer++) {
      this.layers[layer] = new InstrumentLayer(data.slice(8 + layer * 16, 16), soundBank);
    }

    this.volume = data.readUByte(0x01);
    this.pan = data.readUByte(0x02);
    // 0x03 unused
    this.pitchBendMultiplier = data.readUByte(0x04);
    this.breathControlIndex = data.readUByte(0x05);
    this.startingNote = data.readUByte(0x06);
  }

  private enum Type {
    Standard,
    MultiLayer,
    SFX;

    private static Type getType(final int upperBoundByte) {
      if(upperBoundByte == 0xFF) {
        return Type.SFX;
      }

      if((upperBoundByte & 0x80) != 0) {
        return Type.MultiLayer;
      }

      return Type.Standard;
    }
  }
}
