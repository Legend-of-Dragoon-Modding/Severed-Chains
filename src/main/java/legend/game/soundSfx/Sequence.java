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
      case KEY_OFF, KEY_ON, POLYPHONIC_KEY_PRESSURE, CONTROL_CHANGE -> {
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

    int nextTrackOffset = data.size();
    for(int sequenceTrack = sequenceTrackUpperBound; sequenceTrack >= 0; sequenceTrack--) {
      int nextSequenceOffset = nextTrackOffset;

      nextTrackOffset = data.readUShort(2 + sequenceTrack * 2);

      final int sequenceUpperBound = data.readUShort(nextTrackOffset);
      sequences[sequenceTrack] = new Sequence[sequenceUpperBound + 1];

      for(int sequence = sequenceUpperBound; sequence >= 0; sequence--) {
        final int sequenceOffset = data.readUShort(nextSequenceOffset + 2 + sequence * 2);

        sequences[sequenceTrack][sequence] = new Sequence(data.slice(sequenceOffset, nextSequenceOffset - sequenceOffset));

        nextSequenceOffset = sequenceOffset;
      }
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
