package legend.game.soundSfx;

import legend.game.unpacker.FileData;

import java.util.ArrayList;
import java.util.List;

final class Instrument {
  private final InstrumentLayer[] layers;
  private final boolean playsMultipleLayers;

  private final int volume;
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
    this.volume = data.readUByte(1);

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
}
