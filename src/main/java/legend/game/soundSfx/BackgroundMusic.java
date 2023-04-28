package legend.game.soundSfx;

import legend.core.audio.AudioStream;
import legend.core.audio.MidiSequence;

import java.util.List;

//TODO this should work for both BGM and SFX, since both are sequenced
final class BackgroundMusic implements MidiSequence {
  private int samplesToProcess;
  private AudioStream[] audioStreams;
  private final Sssq sssq;

  BackgroundMusic(final Sssq sssq) {
    this.sssq = sssq;
  }

  @Override
  public void tick() {
    if(this.samplesToProcess > 0) {
      this.samplesToProcess--;
      return;
    }

    while(this.samplesToProcess == 0) {
      final Sequence.Command command = this.sssq.getNextCommand();
      switch(command.getCommand()) {
        case KEY_ON -> this.keyOn(command.getChannel(), command.getValue1(), command.getValue2());
        case KEY_OFF -> this.keyOff(command.getChannel(), command.getValue1(), command.getValue2());
        case POLYPHONIC_KEY_PRESSURE -> this.polyphonicKeyPressure();
        case CONTROL_CHANGE -> this.controlChange(command.getChannel(), command.getValue1(), command.getValue2());
        case PROGRAM_CHANGE -> this.programChange(command.getChannel(), command.getValue1());
        case PITCH_BEND -> this.pitchBend(command.getChannel(), command.getValue1());
        case META -> this.meta(command.getValue1(), command.getValue2());
        default -> System.err.println("Unhandled message " + command.getCommand());
      }

      this.samplesToProcess = this.sssq.deltaTimeToSampleCount(command.getDeltaTime());
    }

    System.out.printf("Delta ms %.02f%n", this.samplesToProcess / 44.1f);
  }

  private void keyOn(final int channelIndex, final int note, final int velocity) {
    final Channel channel = this.sssq.getChannel(channelIndex);
    final Instrument instrument = channel.getInstrument();
    final List<InstrumentLayer> layers = channel.getLayers(note);

    int layerIndex = 0;
    int voice;
    for(voice = 0; voice < this.audioStreams.length && layerIndex < layers.size(); voice++) {
      final AudioStream stream = this.audioStreams[voice];

      if(stream.getChannel() == channel && stream.getNote() == note) {
        System.out.printf("Key On (Reset) Channel: %d [Voice: %d] Note: %d%n", channelIndex, voice, note);
        this.audioStreams[voice].keyOn(channel, instrument, layers.get(layerIndex++), note, velocity);
      }
    }

    for(voice = 0; voice < this.audioStreams.length && layerIndex < layers.size(); voice++) {
      if(this.audioStreams[voice].isEmpty()) {
        System.out.printf("Key On Channel: %d [Voice: %d] Note: %d%n", channelIndex, voice, note);
        this.audioStreams[voice].keyOn(channel, instrument, layers.get(layerIndex++), note, velocity);
      }
    }

    if(voice == this.audioStreams.length) {
      throw new IndexOutOfBoundsException("Voice Pool overflow!");
    }
  }

  private void keyOff(final int channelIndex, final int note, final int velocity) {
    for(int voice = 0; voice < this.audioStreams.length; voice++) {
      final AudioStream stream = this.audioStreams[voice];
      if(stream.getChannel() == this.sssq.getChannel(channelIndex) && stream.getNote() == note) {
        System.out.printf("Key Off Channel: %d [Voice: %d]%n", channelIndex, voice);
        stream.keyOff(velocity);
      }
    }
  }

  private void polyphonicKeyPressure() {

  }

  private void controlChange(final int channelIndex, final int control, final int value) {
    switch(control) {
      case 0x01 -> System.err.printf("Control Change Channel: %d Modulation: 0x%x%n", channelIndex, value);
      case 0x02 -> System.err.printf("Control Change Channel: %d Breath Control: 0x%x%n", channelIndex, value);
      case 0x07 -> this.changeVolume(channelIndex, value);
      case 0x0a -> {
        System.out.printf("Control Change Channel: %d Pan: %d%n", channelIndex, value);
        this.sssq.getChannel(channelIndex).setPan(value);
      }
      default -> System.err.printf("Bad Control Change Channel: %d Command: 0x%x Value: 0x%x%n", channelIndex, control, value);
    }
  }

  public void changeVolume(final int channelIndex, final int value) {
    System.out.printf("Control Change Channel: %d Volume: %d%n", channelIndex, value);

    final Channel channel = sssq.getChannel(channelIndex);
    channel.setVolume(value);

    for(final AudioStream audioStream : this.audioStreams) {
      if(audioStream.getChannel() == channel) {
        audioStream.updateVolume();
      }
    }
  }

  private void programChange(final int channelIndex, final int value) {
    this.sssq.getChannel(channelIndex).setProgram(value);

    System.out.printf("Program Change Channel: %d Program: %d%n", channelIndex, value);
  }

  private void pitchBend(final int channelIndex, final int value) {
    final Channel channel = this.sssq.getChannel(channelIndex);
    channel.setPitchBend(value);

    for(final AudioStream audioStream : this.audioStreams) {
      if(audioStream.getChannel() == channel) {
        audioStream.updateSampleRate();
      }
    }

    System.out.printf("Pitch Bend Channel: %d, Value: %d%n", channelIndex, value);
  }

  private void meta(final int command, final int value) {
    switch(command) {
      case 0x51 -> {
        System.out.printf("Tempo change: %d%n", value);
        this.sssq.setTempo(value);
      }

      default -> System.err.printf("Unhandled meta event %d value %d%n", command, value);
    }
  }

  @Override
  public void loadVoices(final AudioStream[] audioStreams) {
    this.audioStreams = audioStreams;
  }
}
