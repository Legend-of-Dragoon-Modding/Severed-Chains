package legend.core.audio;

public interface AdsrEnvelope {
  short get();
  void mute();
  void keyOff();
}
