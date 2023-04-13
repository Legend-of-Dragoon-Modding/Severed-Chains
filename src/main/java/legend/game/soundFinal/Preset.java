package legend.game.soundFinal;

import java.util.ArrayList;
import java.util.List;

final class Preset {
  private final Layer[] layers;
  public final boolean playsMultipleLayers;

  Preset(final byte[] data, final SoundBank soundBank) {
    final int layerCount = (data[0] & 0x7f) + 1;
    final int volume = data[1];
    //Flag??
    final int startingKey = data[6];

    this.layers = new Layer[layerCount];
    this.playsMultipleLayers = (data[0] & 0x80) != 0;

    for(int i = 0; i < layerCount; i++) {
      final byte[] layerArr = new byte[16];
      System.arraycopy(data, i * 16 + 8, layerArr, 0, layerArr.length);
      this.layers[i] = new Layer(layerArr, soundBank);
    }
  }

  List<Layer> getLayers(final int note) {
    final List<Layer> layers = new ArrayList<>();

    for(final Layer layer : this.layers) {
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
