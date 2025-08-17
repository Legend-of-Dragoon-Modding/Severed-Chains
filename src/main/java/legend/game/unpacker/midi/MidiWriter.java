package legend.game.unpacker.midi;

import legend.core.IoHelper;
import legend.core.MathHelper;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class MidiWriter {
  public static void main(final String[] args) {
    new MidiWriter().write();
  }

  public void write() {
    for(int i = 1; i < 2; i++) {
      try(final SeekableByteChannel channel = Files.newByteChannel(Paths.get("out.mid"), StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
        final byte[] sssqRaw = Files.readAllBytes(Path.of("files/SECT/DRGN0.BIN/" + (5815 + i * 5) + "/1"));

        final ByteBuffer header = ByteBuffer.allocate(14);
        IoHelper.write(header, 0x4d546864); // MThd
        IoHelper.write(header, 6); // Header size (always 6)
        IoHelper.write(header, (short)0); // Format
        IoHelper.write(header, (short)1); // Number of tracks
        IoHelper.write(header, (short)MathHelper.get(sssqRaw, 2, 2)); // Ticks per beat (+0x2 in SSSQ)
        header.flip();
        channel.write(header);

        final ByteBuffer sssq = ByteBuffer.wrap(sssqRaw, 0x110, sssqRaw.length - 0x110);
        sssq.order(ByteOrder.LITTLE_ENDIAN);

        final ByteBuffer track = ByteBuffer.allocate(sssqRaw.length * 2); // Allocate a bit of extra room
        IoHelper.write(track, 0x4d54726b); // MTrk
        IoHelper.write(track, 0); // Will get replaced with chunk size
        IoHelper.write(track, (byte)0); // Delta time - first event, so 0 (varint)

        // Write initial tempo
        track.put((byte)0xff); // Meta
        track.put((byte)0x51); // Tempo change
        track.put((byte)3); // Data length
        IoHelper.write3(track, 60_000_000 / (short)MathHelper.get(sssqRaw, 4, 2));
        track.put((byte)0); // No elapsed time

        byte previousCommand = (byte)0xff;

        outer:
        while(sssq.hasRemaining()) {
          byte command = sssq.get();
          System.out.printf("%x - %x%n", sssq.position() - 1, command);

          if((command & 0xff) < 0x80) {
            command = previousCommand;
            sssq.position(sssq.position() - 1);
            System.out.printf("Continuation command - using command %x%n", command);
          }

          switch(command & 0xf0) {
            // Key off, key on
            case 0x80, 0x90 -> {
              track.put(command);
              track.put(sssq.get()); // Note
              track.put(sssq.get()); // Velocity
            }

            case 0xb0 -> {
              final byte controlNumber = sssq.get();

              switch(controlNumber) {
                // Modulation wheel, breath control, data entry (???), channel volume, pan, non-registered parameter number (NRPN) - MSB (???)
                case 1, 2, 6, 7, 0xa -> {
                  track.put(command);
                  track.put(controlNumber);
                  track.put(sssq.get());
                }

                case 0x63 -> {
                  track.put(command);
                  track.put(controlNumber);
                  track.put(sssq.get());
                }

                default -> throw new RuntimeException("Unknown control number %x".formatted(controlNumber));
              }
            }

            case 0xc0 -> { // Program change (instrument)
              track.put(command);
              track.put(sssq.get());
            }

            case 0xe0 -> { // Pitch bend
              track.put(command);
              track.put(sssq.get()); // Coarse pitch
              track.put((byte)0); // Fine pitch (not used in SSSQ)
            }

            case 0xf0 -> { // Meta
              final byte metaEvent = sssq.get();

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
                  IoHelper.write3(track, 60_000_000 / IoHelper.readShort(sssq));
                }

                default -> throw new RuntimeException("Unknown meta event %x".formatted(metaEvent));
              }
            }

            default -> throw new RuntimeException("Unknown command %x".formatted(command));
          }

          // Copy elapsed time since last event (varint)
          while(true) {
            final byte varint = sssq.get();
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
