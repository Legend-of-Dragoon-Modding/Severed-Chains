package legend.core.audio.sequencer.assets.sequence.sfx;

import legend.core.audio.sequencer.assets.Channel;
import legend.core.audio.sequencer.assets.sequence.Command;
import legend.game.unpacker.FileData;

import java.util.ArrayList;
import java.util.List;

public final class SequenceBuilder {
  private int position;

  private final FileData data;
  private final Channel[] channels;

  private SequenceBuilder(final FileData data, final Channel[] channels) {
    this.data = data;
    this.channels = channels;
  }

  private int readDeltaTime() {
    int deltaTime = 0;

    for(int i = 0; i < 4; i++) {
      final int part = this.data.readUByte(this.position++);

      deltaTime <<= 7;
      deltaTime |= part & 0x7f;

      if((part & 0x80) == 0) {
        break;
      }
    }

    return deltaTime;
  }

  private Command sfxOn() {
    return new SfxOn(this.data.readUByte(this.position++), this.data.readUByte(this.position++), this.channels[this.data.readUByte(this.position++)], this.readDeltaTime());
  }

  private Command controlChange() {
    final int controlType = this.data.readUByte(this.position++);

    return switch(controlType) {
      case 0x1 -> new ModulationChange(this.data.readUByte(this.position++), this.channels[this.data.readUByte(this.position++)], this.data.readUByte(this.position++), this.readDeltaTime());
      case 0x2 -> new BreathChange(this.data.readUByte(this.position++), this.channels[this.data.readUByte(this.position++)], this.data.readUByte(this.position++), this.readDeltaTime());
      case 0x7 -> new VolumeChange(this.data.readUByte(this.position++), this.data.readUByte(this.position++), this.channels[this.data.readUByte(this.position++)], this.data.readUByte(this.position++), this.readDeltaTime());
      case 0xa -> new PanChange(this.data.readUByte(this.position++), this.data.readUByte(this.position++), this.channels[this.data.readUByte(this.position++)], this.data.readUByte(this.position++), this.readDeltaTime());
      case 0x41 -> new PortamentoChange(this.data.readUByte(this.position++), this.data.readUByte(this.position++), this.channels[this.data.readUByte(this.position++)], this.data.readUByte(this.position++), this.readDeltaTime());
      default -> throw new RuntimeException("Unexpected control change type 0x%x".formatted(controlType));
    };
  }

  private Command meta() {
    final int metaType = this.data.readUByte(this.position++);
    switch(metaType) {
      case 0x2f -> { return new EndOfTrack(this.readDeltaTime()); }
      default -> throw new RuntimeException("Unexpected meta type 0x%x".formatted(metaType));
    }
  }

  private Command getCommand(final int event) {
    return switch (event & 0xf0) {
      case 0xa0 -> this.sfxOn();
      case 0xb0 -> this.controlChange();
      case 0xf0 -> this.meta();
      default -> throw new RuntimeException("Unexpected command 0x%x".formatted(event));
    };
  }

  private Command[] processSequence(final int offset) {
    this.position = offset;

    final List<Command> commands = new ArrayList<>();
    int currentEvent;
    int previousEvent = 0;

    while(this.position < this.data.size()) {
      currentEvent = this.data.readUByte(this.position);

      if((currentEvent & 0x80) == 0) {
        currentEvent = previousEvent;
      } else {
        previousEvent = currentEvent;
        this.position++;
      }

      final Command command = this.getCommand(currentEvent);
      commands.add(command);

      if(command.getClass() == EndOfTrack.class) {
        break;
      }
    }

    return commands.toArray(new Command[0]);
  }

  public static Command[][][] process(final FileData data, final Channel[] channels) {
    final SequenceBuilder sequenceBuilder = new SequenceBuilder(data, channels);

    final int sets = data.readShort(0) + 1;
    final Command[][][] sequences = new Command[sets][][];

    for(int set = 0; set < sets; set++) {
      final int setOffset = data.readShort(2 + set * 2);
      final int entries = data.readShort(setOffset) + 1;
      sequences[set] = new Command[entries][];

      for(int entry = 0; entry < entries; entry++) {
        final int entryOffset = data.readShort(setOffset + 2 + entry * 2);
        sequences[set][entry] = sequenceBuilder.processSequence(entryOffset);
      }
    }

    return sequences;
  }
}
