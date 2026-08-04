package legend.game.unpacker.midi;

import legend.core.IoHelper;
import legend.core.memory.types.IntRef;
import legend.game.sound.Sssq;
import legend.game.unpacker.ExpandableFileData;
import legend.game.unpacker.FileData;
import legend.game.unpacker.Loader;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MidiToSssq {
  static void main(final String[] args) throws IOException {
    new MidiToSssq().write();
  }

  public void write() throws IOException {
    final FileData originalData = Loader.loadFileSync(Loader.resolve("SECT/DRGN0.BIN/5820/1-orig"));
    final Sssq original = new Sssq(originalData);
    final byte[] midiRaw = Files.readAllBytes(Path.of("out-new-all-type0.mid"));
    final ByteBuffer midi = ByteBuffer.wrap(midiRaw);

    final int numberOfTracks = midi.getShort(0x0a);
    final int tempo = midi.getShort(0x0c);

    final ExpandableFileData out = new ExpandableFileData(originalData.size());
    final IntRef offset = new IntRef();

    // Copy header and instruments from original
    out.write(0, originalData, offset, 0x110);

    // Copy tempo
    out.writeShort(0x2, tempo);

    midi.position(0x0e);

    final List<MidiCommand> commands = new ArrayList<>();

    for(int trackIndex = 0; trackIndex < numberOfTracks; trackIndex++) {
      System.out.printf("Processing track %d/%d%n...", trackIndex + 1, numberOfTracks);

      final String trackMagic = IoHelper.readString(midi, 4);
      if(!"MTrk".equals(trackMagic)) {
        throw new RuntimeException("Invalid track magic " + trackMagic);
      }

      final int trackChunkSize = midi.getInt();
      final int startPosition = midi.position();

      midi.get(); // initial delta time

      byte previousCommand = (byte)0xff;
      int time = 0;

      while(midi.position() - startPosition < trackChunkSize) {
        byte command = midi.get();
        System.out.printf("%#x - %#x%n", midi.position() - 1, command);

        if((command & 0xff) < 0x80) {
          command = previousCommand;
          midi.position(midi.position() - 1);
          System.out.printf("Continuation command - using command %x%n", command);
        }

        switch(command & 0xf0) {
          // Key off, key on
          case 0x80, 0x90 -> {
            final byte note = midi.get();
            final byte velocity = midi.get();
            commands.add(new MidiCommand(command, new byte[] {note, velocity}));
          }

          case 0xb0 -> {
            final byte controlNumber = midi.get();

            switch(controlNumber) {
              // Modulation wheel, breath control, data entry (???), channel volume, pan, non-registered parameter number (NRPN) - MSB (???)
              case 1, 2, 6, 7, 0xa, 0x63 -> {
                final byte value = midi.get();
                commands.add(new MidiCommand(command, new byte[] {controlNumber, value}));
              }

              default -> throw new RuntimeException("Unknown control number %x".formatted(controlNumber));
            }
          }

          case 0xc0 -> { // Program change (instrument)
            final byte instrument = midi.get();
            commands.add(new MidiCommand(command, new byte[] {instrument}));
          }

          case 0xf0 -> { // Meta
            final byte metaEvent = midi.get();

            switch(metaEvent) {
              case 0x3 -> { // Track name
                final byte length = midi.get();
                System.out.println("Track name: " + IoHelper.readString(midi, length));
              }

              case 0x2f -> {
                commands.add(new MidiCommand(command, new byte[] {metaEvent}));
              }

              case 0x51 -> {
                final int size = midi.get();
                if(size != 3) {
                  throw new RuntimeException("Unknown tempo change size " + size);
                }

                final int tempoChange = 60_000_000 / IoHelper.read3(midi);
                commands.add(new MidiCommand(command, new byte[] {metaEvent, (byte)tempoChange, (byte)(tempoChange >>> 16)}));
              }

              default -> throw new RuntimeException("Unknown meta event %x".formatted(metaEvent));
            }
          }

          default -> throw new RuntimeException("Unknown command %x".formatted(command));
        }

        // Copy elapsed time since last event (varint)
        int deltaTime = 0;
        while(true) {
          final int varint = midi.get() & 0xff;

          deltaTime <<= 7;
          deltaTime |= varint;

          if((varint & 0x80) == 0) {
            break;
          }
        }

        time += deltaTime;
        commands.getLast().time = time;
        previousCommand = command;
      }
    }

    // Sort all commands
    commands.sort(Comparator.comparingInt(command -> command.time));

    // Relativize timestamps
    for(int i = 0; i < commands.size() - 1; i++) {
      final MidiCommand current = commands.get(i);
      final MidiCommand next = commands.get(i + 1);
      current.time = next.time - current.time;
    }

    offset.set(0x110);
    int previousCommand = 0;

    for(final MidiCommand command : commands) {
      // Continuation
      if(previousCommand != command.command) {
        out.writeByte(offset, command.command);
      }

      out.write(0, command.data, offset, command.data.length);
      out.writeVarInt(offset, command.time);

      previousCommand = command.command;
    }

    final byte[] trimmed = new byte[offset.get() + 1];
    out.read(0, trimmed, 0, trimmed.length);
    Files.write(Path.of("out.sssq"), trimmed);
  }
}
