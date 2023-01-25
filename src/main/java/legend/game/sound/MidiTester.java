package legend.game.sound;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Soundbank;
import javax.sound.midi.Synthesizer;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class MidiTester {
  private MidiTester() { }

  public static void main(final String[] args) throws MidiUnavailableException, IOException, InvalidMidiDataException {
    final Soundbank soundfont = MidiSystem.getSoundbank(Files.newInputStream(Path.of("./test.sf2")));
    final Sequencer sequencer = MidiSystem.getSequencer(false);
    final Synthesizer synthesizer = MidiSystem.getSynthesizer();

    sequencer.open();
    synthesizer.open();
    synthesizer.loadAllInstruments(soundfont);

    sequencer.getTransmitter().setReceiver(synthesizer.getReceiver());
    sequencer.setSequence(Files.newInputStream(Path.of("./out.mid")));

    sequencer.start();
  }
}
