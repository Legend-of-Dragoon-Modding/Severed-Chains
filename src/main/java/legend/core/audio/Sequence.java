package legend.core.audio;

import legend.core.audio.assets.SequencedAudio;
import legend.game.unpacker.FileData;

public final class Sequence {
  private final SequencedAudio sequencedAudio;
  private final FileData sequenceData;
  private int sequenceOffset;
  private final Command previousCommand = new Command();
  int samplesToProcess;

  Sequence(final SequencedAudio sequencedAudio, final int sequenceIndex) {
    this.sequencedAudio = sequencedAudio;
    this.sequenceData = new FileData(this.sequencedAudio.getSequence(sequenceIndex));
  }

  SequencedAudio getSequencedAudio() {
    return this.sequencedAudio;
  }

  void setSamplesToProcess(final int deltaTime) {
    if(deltaTime == 0) {
      this.samplesToProcess = 0;
      return;
    }

    final double deltaMs = deltaTime / this.sequencedAudio.getTicksPerMs();
    this.samplesToProcess = (int)(deltaMs * 44.1d);
  }

  Command getNextCommand() {
    final int event = this.sequenceData.readUByte(this.sequenceOffset);

    if((event & 0x80) != 0) {
      this.previousCommand.command = MidiCommand.fromEvent(event);
      this.previousCommand.channel = event & 0x0F;
      this.sequenceOffset++;
    }

    switch(this.previousCommand.command) {
      case POLYPHONIC_KEY_PRESSURE -> {
        this.previousCommand.value1 = this.sequenceData.readUByte(this.sequenceOffset++);
        this.previousCommand.value2 = this.sequenceData.readUByte(this.sequenceOffset++);
        this.sequenceOffset++; //TODO extra value.
      }
      case KEY_OFF, KEY_ON, CONTROL_CHANGE -> {
        this.previousCommand.value1 = this.sequenceData.readUByte(this.sequenceOffset++);
        this.previousCommand.value2 = this.sequenceData.readUByte(this.sequenceOffset++);
      }
      case PROGRAM_CHANGE, PITCH_BEND -> {
        this.previousCommand.value1 = this.sequenceData.readUByte(this.sequenceOffset++);
      }
      case META -> {
        this.previousCommand.value1 = this.sequenceData.readUByte(this.sequenceOffset++);
        if(this.previousCommand.value1 == 0x51) {
          this.previousCommand.value2 = this.sequenceData.readUShort(this.sequenceOffset);
          this.sequenceOffset += 2;
        }
      }

      default -> throw new RuntimeException("Bad message: " + this.previousCommand.command);
    }

    if(this.sequencedAudio.repeat) {

    }

    this.previousCommand.deltaTime = this.readDeltaTime();
    return this.previousCommand;
  }

  private int readDeltaTime() {
    int deltaTime = 0;
    while(true) {
      final int part = this.sequenceData.readUByte(this.sequenceOffset++);

      deltaTime <<=7;
      deltaTime |= part & 0x7f;

      if((part & 0x80) == 0) {
        break;
      }
    }

    return deltaTime;
  }

  public final class Command {
    private MidiCommand command;
    private int channel;
    private int value1;
    private int value2;
    private int deltaTime;

    MidiCommand getMidiCommand() {
      return this.command;
    }

    int getChannel() {
      return this.channel;
    }

    int getValue1() {
      return this.value1;
    }

    int getValue2() {
      return this.value2;
    }

    int getDeltaTime() {
      return this.deltaTime;
    }
  }

  enum MidiCommand {
    KEY_OFF(0x80),
    KEY_ON(0x90),
    POLYPHONIC_KEY_PRESSURE(0xA0),
    CONTROL_CHANGE(0xB0),
    PROGRAM_CHANGE(0xC0),
    PITCH_BEND(0xE0),
    META(0xF0);

    private final int command;

    MidiCommand(final int command) {
      this.command = command;
    }

    private static MidiCommand fromEvent(final int event) {
      final int command = event & 0xF0;

      for(final MidiCommand cmd : MidiCommand.values()) {
        if(cmd.command == command) {
          return cmd;
        }
      }

      throw new IllegalArgumentException("Unknown command %x".formatted(command));
    }
  }
}
