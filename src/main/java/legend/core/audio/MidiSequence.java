package legend.core.audio;

public interface MidiSequence {

  void tick();

  void loadVoices(AudioStream[] audioStreams);
}
