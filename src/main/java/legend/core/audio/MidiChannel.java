package legend.core.audio;

public interface MidiChannel {
  double getVolume();
  int getPan();
  int getPitchBend();
  int getModulation();
  int getBreath();
}
