package legend.game.sound2;

import java.nio.ByteBuffer;
import java.security.InvalidParameterException;

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

  public void resetBuffs() {
    for(final Layer layer : this.layers) {
      layer.resetBuffer();
    }
  }

  public void setOff() {
    for(final Layer layer : this.layers) {
      layer.getAdsr().release();
    }
  }

  public void resetAdsr() {
    for(final Layer layer : this.layers) {
      layer.resetAdsr();
    }
  }

  public Layer getLayer(final int note) {
    for(final Layer layer : this.layers) {
      if(layer.noteInRange(note)) {
        return layer;
      }
    }
    throw new InvalidParameterException("Note out of range " + note);
  }
}
