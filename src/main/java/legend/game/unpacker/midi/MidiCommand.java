package legend.game.unpacker.midi;

public class MidiCommand {
  public final byte command;
  public final byte[] data;
  public int time;

  public MidiCommand(final byte command, final byte[] data) {
    this.command = command;
    this.data = data;
  }
}
