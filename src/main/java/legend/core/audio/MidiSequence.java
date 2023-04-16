package legend.core.audio;

public interface MidiSequence {

  public void tick();

  public void loadVoices(AudioStream[] audioStreams);
}
