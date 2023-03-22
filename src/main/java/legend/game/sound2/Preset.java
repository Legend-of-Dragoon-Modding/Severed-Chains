package legend.game.sound2;

import java.nio.ByteBuffer;

final class Preset {
  private final Layer[] layers;
  Preset(final ByteBuffer data, final SoundBank soundBank) {
    final int layerCount = (data.get() & 0x0f) + 1;
    final int volume = data.get();
    data.getInt(); // Flag??
    final int startingKey = data.get();
    data.get(); // Unused??

    this.layers = new Layer[layerCount];

    final byte[] layerArr = new byte[16];
    for(int i = 0; i < layerCount; i++) {
      data.get(layerArr);
      this.layers[i] = new Layer(layerArr, soundBank);
    }
  }

  public Layer getLayer(final int index) {
    return this.layers[index];
  }
}
