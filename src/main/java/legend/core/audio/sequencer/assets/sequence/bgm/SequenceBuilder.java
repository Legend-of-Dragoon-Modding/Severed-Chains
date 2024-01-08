package legend.core.audio.sequencer.assets.sequence.bgm;

import legend.core.audio.sequencer.assets.Channel;
import legend.core.audio.sequencer.assets.sequence.Command;
import legend.game.unpacker.FileData;

import java.util.ArrayList;
import java.util.List;

public final class SequenceBuilder {
  private int position;
  private int currentEvent;
  private int previousEvent;
  private final FileData data;
  private final Channel[] channels;
  private SequenceBuilder(final FileData data, final Channel[] channels) {
    this.data = data;
    this.channels = channels;
  }

  private int readDeltaTime() {
    int deltaTime = 0;

    while(true) {
      final int part = this.data.readUByte(this.position++);

      deltaTime <<= 7;
      deltaTime |= part & 0x7f;

      if((part & 0x80) == 0) {
        return deltaTime;
      }
    }
  }

  private Command keyOff(final int channelIndex) {
    final int note = this.data.readUByte(this.position++);
    this.position++;
    final int deltaTime = this.readDeltaTime();

    return new KeyOff(this.channels[channelIndex], note, deltaTime);
  }

  private Command keyOn(final int channelIndex) {
    final int note = this.data.readUByte(this.position++);
    final int velocity = this.data.readUByte(this.position++);
    final int deltaTime = this.readDeltaTime();

    return new KeyOn(this.channels[channelIndex], note, velocity, deltaTime);
  }

  private Command controlChange(final int channelIndex) {
    final int commandType = this.data.readUByte(this.position++);

    return switch(commandType) {
      case 0x01 -> new ModulationChange(this.channels[channelIndex], this.data.readUByte(this.position++), this.readDeltaTime());
      case 0x02 -> new BreathChange(this.channels[channelIndex], this.data.readUByte(this.position++), this.readDeltaTime());
      case 0x06 -> new DataEntry(this.data.readUByte(this.position++), this.readDeltaTime());
      case 0x07 -> new VolumeChange(this.channels[channelIndex], this.data.readUByte(this.position++), this.readDeltaTime());
      case 0x0a -> new PanChange(this.channels[channelIndex], this.data.readUByte(this.position++), this.readDeltaTime());
      case 0x62 -> new DataEntryLsb(this.data.readUByte(this.position++), this.readDeltaTime());
      case 0x63 -> new DataEntryMsb(this.data.readUByte(this.position++), this.readDeltaTime());
      default -> throw new RuntimeException("Unexpected control change type 0x%x".formatted(commandType));
    };
  }

  private Command programChange(final int channelIndex) {
    return new ProgramChange(this.channels[channelIndex], this.data.readUByte(this.position++), this.readDeltaTime());
  }

  private Command pitchBend(final int channelIndex) {
    return new PitchBendChange(this.channels[channelIndex], this.data.readUByte(this.position++), this.readDeltaTime());
  }

  private Command meta() {
    final int metaType = this.data.readUByte(this.position++);

    switch(metaType) {
      case 0x2f -> { return new EndOfTrack(this.readDeltaTime()); }
      case 0x51 -> {
        final int tempo = this.data.readUShort(this.position);
        this.position += 2;
        return new TempoChange(tempo, this.readDeltaTime());
      }
      default -> throw new RuntimeException("Unexpected meta type 0x%x".formatted(metaType));
    }
  }

  private Command getCommand(final int event) {
    return switch (event & 0xF0) {
      case 0x80 -> this.keyOff(event & 0x0f);
      case 0x90 -> this.keyOn(event & 0x0f);
      case 0xb0 -> this.controlChange(event & 0x0f);
      case 0xc0 -> this.programChange(event & 0x0f);
      case 0xe0 -> this.pitchBend(event & 0x0f);
      case 0xf0 -> this.meta();
      default -> throw new RuntimeException("Unexpected command 0x%x".formatted(event));
    };
  }

  private Command[] processSequence() {
    final List<Command> commands = new ArrayList<>();

    while(this.position < this.data.size()) {
      this.currentEvent = this.data.readUByte(this.position);

      if((this.currentEvent & 0x80) == 0) {
        this.currentEvent = this.previousEvent;
      } else {
        this.previousEvent = this.currentEvent;
        this.position++;
      }

      commands.add(this.getCommand(this.currentEvent));
    }

    return commands.toArray(new Command[0]);
  }

  public static Command[] create(final FileData data, final Channel[] channels) {
    final SequenceBuilder sequenceBuilder = new SequenceBuilder(data, channels);

    return sequenceBuilder.processSequence();
  }
}
