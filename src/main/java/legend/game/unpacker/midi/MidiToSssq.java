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

public class MidiToSssq {
  static void main(final String[] args) throws IOException {
    new MidiToSssq().write();
  }

  public void write() throws IOException {
    final FileData originalData = Loader.loadFileSync(Loader.resolve("SECT/DRGN0.BIN/5820/1-orig"));
    final Sssq original = new Sssq(originalData);
    final byte[] midiRaw = Files.readAllBytes(Path.of("out-new.mid"));
    final ByteBuffer midi = ByteBuffer.wrap(midiRaw);

    final int numberOfTracks = midi.getShort(0x0a);
    final int tempo = midi.getShort(0x0c);

    if(numberOfTracks != 1) {
      throw new RuntimeException("Only single-track midis supported");
    }

    final ExpandableFileData out = new ExpandableFileData(originalData.size());
    final IntRef offset = new IntRef();

    // Copy header and instruments from original
    out.write(0, originalData, offset, 0x110);

    // Copy tempo
    out.writeShort(0x2, tempo);

    midi.position(0x17);
    offset.set(0x110);

    byte previousCommand = (byte)0xff;
    boolean lastCommandWasNoOp = false;

    outer:
    while(midi.hasRemaining()) {
      byte command = midi.get();
      System.out.printf("%#x - %#x%n", midi.position() - 1, command);

      if((command & 0xff) < 0x80) {
        command = previousCommand;
        midi.position(midi.position() - 1);
        System.out.printf("Continuation command - using command %x%n", command);

        if(lastCommandWasNoOp) {
          out.writeByte(offset, command);
        }
      }

      lastCommandWasNoOp = false;

      switch(command & 0xf0) {
        // Key off, key on
        case 0x80, 0x90 -> {
          out.writeByte(offset, command);
          out.writeByte(offset, midi.get()); // Note
          out.writeByte(offset, midi.get()); // Velocity
        }

        case 0xb0 -> {
          final byte controlNumber = midi.get();

          switch(controlNumber) {
            // Modulation wheel, breath control, data entry (???), channel volume, pan, non-registered parameter number (NRPN) - MSB (???)
            case 1, 2, 6, 7, 0xa -> {
              out.writeByte(offset, command);
              out.writeByte(offset, controlNumber);
              out.writeByte(offset, midi.get());
            }

            case 0x63 -> {
              out.writeByte(offset, command);
              out.writeByte(offset, controlNumber);
              out.writeByte(offset, midi.get());
            }

            default -> throw new RuntimeException("Unknown control number %x".formatted(controlNumber));
          }
        }

        case 0xc0 -> { // Program change (instrument)
          out.writeByte(offset, command);
          out.writeByte(offset, midi.get());
        }

        case 0xf0 -> { // Meta
          final byte metaEvent = midi.get();

          switch(metaEvent) {
            case 0x3 -> { // Track name
              final byte length = midi.get();
              final byte[] chars = new byte[length];
              midi.get(chars);
              System.out.println("Track name: " + new String(chars));
              lastCommandWasNoOp = true;
            }

            case 0x2f -> {
              out.writeByte(offset, 0xff); // Meta
              out.writeByte(offset, 0x2f); // End of track
              break outer;
            }

            case 0x51 -> {
              out.writeByte(offset, 0xff);
              out.writeByte(offset, 0x51);

              final int size = midi.get();
              if(size != 3) {
                throw new RuntimeException("Unknown tempo change size " + size);
              }

              out.writeShort(offset, 60_000_000 / IoHelper.read3(midi));
            }

            default -> throw new RuntimeException("Unknown meta event %x".formatted(metaEvent));
          }
        }

        default -> throw new RuntimeException("Unknown command %x".formatted(command));
      }

      // Copy elapsed time since last event (varint)
      while(true) {
        final byte varint = midi.get();

        if(!lastCommandWasNoOp) {
          out.writeByte(offset, varint);
        }

        if((varint & 0x80) == 0) {
          break;
        }
      }

      previousCommand = command;
    }

    final byte[] trimmed = new byte[offset.get() + 1];
    out.read(0, trimmed, 0, trimmed.length);
    Files.write(Path.of("out.sssq"), trimmed);
  }
}
