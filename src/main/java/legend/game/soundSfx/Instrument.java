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

    this.playsMultipleLayers = (data.readUByte(0) & 0x80) != 0;
    this.volume = data.readUByte(1) / 127d;
    this.pan = data.readUByte(2);
    this.pitchBendMultiplier = data.readUByte(4);

    //Flags?? ADSR??

    this.startingKey = data.readUByte(6);

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
}
