package legend.game.unpacker.midi;

import legend.core.IoHelper;
import legend.core.memory.types.IntRef;
import legend.game.sound.Sssq;
import legend.game.unpacker.FileData;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class MidiWriter {
  static void main(final String[] args) {
    new MidiWriter().write();
  }

  public void write() {
    for(int i = 1; i < 2; i++) {
      try(final SeekableByteChannel channel = Files.newByteChannel(Paths.get("out.mid"), StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
        final Sssq sssqRaw = new Sssq(new FileData(Files.readAllBytes(Path.of("files/SECT/DRGN0.BIN/" + (5815 + i * 5) + "/1"))));

        final ByteBuffer header = ByteBuffer.allocate(14);
        IoHelper.write(header, 0x4d546864); // MThd
        IoHelper.write(header, 6); // Header size (always 6)
        IoHelper.write(header, (short)0); // Format
        IoHelper.write(header, (short)1); // Number of tracks
        IoHelper.write(header, (short)sssqRaw.ticksPerQuarterNote_02); // Ticks per beat (+0x2 in SSSQ)
        header.flip();
        channel.write(header);

        final FileData sssq = sssqRaw.data();
        final IntRef offset = new IntRef();

        final ByteBuffer track = ByteBuffer.allocate(sssqRaw.data().size() * 2); // Allocate a bit of extra room
        IoHelper.write(track, 0x4d54726b); // MTrk
        IoHelper.write(track, 0); // Will get replaced with chunk size
        IoHelper.write(track, (byte)0); // Delta time - first event, so 0 (varint)

        // Write initial tempo
        track.put((byte)0xff); // Meta
        track.put((byte)0x51); // Tempo change
        track.put((byte)3); // Data length
        IoHelper.write3(track, 60_000_000 / sssqRaw.tempo_04);
        track.put((byte)0); // No elapsed time

        byte previousCommand = (byte)0xff;

        outer:
        while(offset.get() < sssq.size()) {
          byte command = sssq.readByte(offset);
          System.out.printf("%#x - %#x%n", offset.get() - 1, command);

          if((command & 0xff) < 0x80) {
            command = previousCommand;
            offset.decr();
            System.out.printf("Continuation command - using command %x%n", command);
          }

          switch(command & 0xf0) {
            // Key off, key on
            case 0x80, 0x90 -> {
              track.put(command);
              track.put(sssq.readByte(offset)); // Note
              track.put(sssq.readByte(offset)); // Velocity
            }

            case 0xb0 -> {
              final byte controlNumber = sssq.readByte(offset);

              switch(controlNumber) {
                // Modulation wheel, breath control, data entry (???), channel volume, pan, non-registered parameter number (NRPN) - MSB (???)
                case 1, 2, 6, 7, 0xa -> {
                  track.put(command);
                  track.put(controlNumber);
                  track.put(sssq.readByte(offset));
                }

                case 0x63 -> {
                  track.put(command);
                  track.put(controlNumber);
                  track.put(sssq.readByte(offset));
                }

                default -> throw new RuntimeException("Unknown control number %x".formatted(controlNumber));
              }
            }

            case 0xc0 -> { // Program change (instrument)
              track.put(command);
              track.put(sssq.readByte(offset));
            }

            case 0xe0 -> { // Pitch bend
              track.put(command);
              track.put(sssq.readByte(offset)); // Coarse pitch
              track.put((byte)0); // Fine pitch (not used in SSSQ)
            }

            case 0xf0 -> { // Meta
              final byte metaEvent = sssq.readByte(offset);

              switch(metaEvent) {
                case 0x2f -> {
                  track.put((byte)0xff); // Meta
                  track.put((byte)0x2f); // End of track
                  track.put((byte)0); // Data length
                  break outer;
                }

                case 0x51 -> {
                  track.put((byte)0xff); // Meta
                  track.put((byte)0x51); // Tempo change
                  track.put((byte)3); // Data length
                  IoHelper.write3(track, 60_000_000 / sssq.readShort(offset));
                }

                default -> throw new RuntimeException("Unknown meta event %x".formatted(metaEvent));
              }
            }

            default -> throw new RuntimeException("Unknown command %x".formatted(command));
          }

          // Copy elapsed time since last event (varint)
          while(true) {
            final byte varint = sssq.readByte(offset);
            track.put(varint);

            if((varint & 0x80) == 0) {
              break;
            }
          }

          previousCommand = command;
        }

        track.putInt(4, track.position() - 8); // Chunk size
        track.flip();
        channel.write(track);
      } catch(final IOException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
