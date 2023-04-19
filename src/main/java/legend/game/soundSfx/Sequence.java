package legend.game.soundSfx;

import legend.game.unpacker.FileData;

final class Sequence {
  private final FileData sequenceData;
  private int offset;

  private final Command previousCommand = new Command();

  Sequence(final FileData sequenceData) {
    this.sequenceData = sequenceData;
  }

  Command getNextCommand() {
    final int event = this.sequenceData.readUByte(this.offset);

    if((event & 0x80) != 0) {
      this.previousCommand.command = MidiCommand.fromEvent(event);
      this.previousCommand.channel = event & 0x0f;
      this.offset++;
    }

    switch(this.previousCommand.command) {
      case POLYPHONIC_KEY_PRESSURE -> {
        this.previousCommand.value1 = this.sequenceData.readUByte(this.offset++);
        this.previousCommand.value2 = this.sequenceData.readUByte(this.offset++);
        this.offset++;
      }
      case KEY_OFF, KEY_ON, CONTROL_CHANGE -> {
        this.previousCommand.value1 = this.sequenceData.readUByte(this.offset++);
        this.previousCommand.value2 = this.sequenceData.readUByte(this.offset++);
      }
      case PROGRAM_CHANGE, PITCH_BEND -> this.previousCommand.value1 = this.sequenceData.readUByte(this.offset++);
      case META -> {
        this.previousCommand.value1 = this.sequenceData.readUByte(this.offset++);
        if(this.previousCommand.value1 == 0x51) {
          this.previousCommand.value2 = this.sequenceData.readUShort(this.offset);
          this.offset += 2;
        }
      }
      default -> throw new RuntimeException("Bad massage: " + this.previousCommand.command);
    }

    this.previousCommand.deltaTime = this.readDeltaTime();
    return this.previousCommand;
  }

  static Sequence[][] getMultiSequence(final FileData data) {
    final int sequenceTrackUpperBound = data.readUShort(0);
    final Sequence[][] sequences = new Sequence[sequenceTrackUpperBound + 1][];

    int trackEnd = data.size();

    for(int sequenceTrack = sequenceTrackUpperBound; sequenceTrack >= 0; sequenceTrack--) {
      final int trackStart = data.readUShort(2 + sequenceTrack * 2);

      int sequenceEnd = trackEnd;

      final int sequenceUpperBound = data.readUShort(trackStart);
      sequences[sequenceTrack] = new Sequence[sequenceUpperBound + 1];

      for(int sequence = sequenceUpperBound; sequence >= 0; sequence--) {
        final int sequenceStart = data.readUShort(trackStart + 2 + sequence * 2);

        sequences[sequenceTrack][sequence] = new Sequence(data.slice(sequenceStart, sequenceEnd - sequenceStart));

        sequenceEnd = sequenceStart;
      }

      trackEnd = sequenceEnd;
    }

    return sequences;
  }



  private int readDeltaTime() {
    int deltaTime = 0;
    while(true) {
      final int part = this.sequenceData.readUByte(this.offset++);

      deltaTime <<=7;
      deltaTime |= part & 0x7f;

      if((part & 0x80) == 0) {
        break;
      }
    }

    return deltaTime;
  }



  class Command {
    private MidiCommand command;
    private int channel;
    private int value1;
    private int value2;
    private int deltaTime;

    MidiCommand getCommand() {
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
}
