package legend.game.soundFinal;

final class Preset {
  private final Layer[] layers;

  Preset(final byte[] data, final SoundBank soundBank) {
    final int layerCount = (data[0] & 0xf) + 1;
    final int volume = data[1];
    //Flag??
    final int startingKey = data[6];

    this.layers = new Layer[layerCount];


    for(int i = 0; i < layerCount; i++) {
      final byte[] layerArr = new byte[16];
      System.arraycopy(data, i * 16 + 8, layerArr, 0, layerArr.length);
      this.layers[i] = new Layer(layerArr, soundBank);
    }
  }

  Layer getLayer(final int note) {
    int offset = 1000;
    int offsetIndex = 0;

    for(int i = 0; i < this.layers.length; i++) {
      if(this.layers[i].getMinimumKeyRange() >= note && this.layers[i].getMaximumKeyRange() <= note) {
        return this.layers[i];
      }

      final int currentOffset;
      if(note < this.layers[i].getMinimumKeyRange()) {
        currentOffset = note - this.layers[i].getMinimumKeyRange();
      } else {
        currentOffset = this.layers[i].getMaximumKeyRange() - note;
      }

      if(currentOffset > offset) {
        return this.layers[offsetIndex];
      }

      offset = currentOffset;
      offsetIndex = i;
    }

    return this.layers[offsetIndex];
  }
}
