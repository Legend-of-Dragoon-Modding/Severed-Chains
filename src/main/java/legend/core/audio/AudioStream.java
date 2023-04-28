package legend.core.audio;

public interface AudioStream {
  void keyOn(final MidiChannel channel, final MidiInstrument instrument, final MidiInstrumentLayer layer, final int note, final int velocity);
  void keyOff(final int velocity);
  MidiChannel getChannel();
  int getNote();
  boolean isEmpty();
  void updateSampleRate();
  void updateVolume();
}
