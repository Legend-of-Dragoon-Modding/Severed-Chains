package legend.game.soundSfx;

import legend.core.audio.MidiInstrument;
import legend.game.unpacker.FileData;

import java.util.ArrayList;
import java.util.List;

final class Instrument implements MidiInstrument {
  private final InstrumentLayer[] layers;
  private final boolean playsMultipleLayers;

  private final double volume;
  private final int pan;
  private final int _05;
  private final int pitchBendMultiplier;
  private final int startingKey;

  Instrument(final FileData data, final SoundBank soundBank) {
    final int layerCountByte = (data.readUByte(0));
    final int layerCount;
    if(layerCountByte != 0xff) {
      layerCount = (layerCountByte & 0x7f) + 1;
    } else {
      layerCount = (data.size() - 8) / 16;
    }

    this.playsMultipleLayers = (data.readUByte(0x0) & 0x80) != 0;
    this.volume = data.readUByte(0x1) / 127d;
    this.pan = data.readUByte(0x2);
    this.pitchBendMultiplier = data.readUByte(0x4);
    this._05 = data.readUByte(0x5);

    this.startingKey = data.readUByte(0x6);

    this.layers = new InstrumentLayer[layerCount];
    for(int layer = 0; layer < this.layers.length; layer++) {
      this.layers[layer] = new InstrumentLayer(data.slice(8 + layer * 16, 16), soundBank);
    }
  }

  List<InstrumentLayer> getLayers(final int note) {
    final List<InstrumentLayer> layers = new ArrayList<>();

    for(final InstrumentLayer layer : this.layers) {
      if(layer.getMinimumKeyRange() <= note && layer.getMaximumKeyRange() >= note) {
        layers.add(layer);

        if(!this.playsMultipleLayers) {
          break;
        }
      }
    }

    return layers;
  }

  @Override
  public double getVolume() {
    return this.volume;
  }

  @Override
  public int getPan() {
    return this.pan;
  }

  @Override
  public int getPitchBendMultiplier() {
    return this.pitchBendMultiplier;
  }

  @Override
  public int get_05() {
    return this._05;
  }
}
