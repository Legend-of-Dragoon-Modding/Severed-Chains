package legend.core.audio;

public interface AudioStream {
  void keyOn(final MidiChannel channel, final MidiInstrument instrument, final MidiInstrumentLayer layer, final int note, final int velocity, final byte[][] breathControls);
  void keyOff(final int velocity);
  MidiChannel getChannel();
  int getNote();
  boolean isEmpty();
  void updateSampleRate();
  void updateVolume();
  void setModulation(final int value);
  void setBreath(final int value);
}
