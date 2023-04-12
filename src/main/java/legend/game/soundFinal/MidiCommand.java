package legend.game.soundFinal;

enum MidiCommand {
  KEY_OFF(0x80),
  KEY_ON(0x90),
  POLYPHONIC_KEY_PRESSURE(0xa0),
  CONTROL_CHANGE(0xb0),
  PROGRAM_CHANGE(0xc0),
  PITCH_BEND(0xe0),
  META(0xf0),
  ;

  static MidiCommand fromEvent(final int event) {
    final int command = event & 0xf0;

    for(final MidiCommand cmd : MidiCommand.values()) {
      if(cmd.command == command) {
        return cmd;
      }
    }

    throw new IllegalArgumentException("Unknown command %x".formatted(command));
  }

  final int command;

  MidiCommand(final int command) {
    this.command = command;
  }
}

