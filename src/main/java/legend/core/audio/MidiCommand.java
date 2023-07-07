package legend.core.audio;

public enum MidiCommand {
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

  public static MidiCommand formEvent(final int event) {
    final int command = event & 0xF0;

    for(final MidiCommand cmd : MidiCommand.values()) {
      if(cmd.command == command) {
        return cmd;
      }
    }

    throw new IllegalArgumentException("Unknown command %x".formatted(command));
  }
}
