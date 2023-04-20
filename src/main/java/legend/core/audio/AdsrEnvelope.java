package legend.core.audio;

public interface AdsrEnvelope {
  short tick();
  void mute();
  void keyOff();
  boolean isFinished();
}
