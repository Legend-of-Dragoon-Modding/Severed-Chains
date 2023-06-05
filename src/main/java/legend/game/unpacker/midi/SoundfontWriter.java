package legend.game.unpacker.midi;

import legend.core.IoHelper;
import legend.core.MathHelper;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class SoundfontWriter {
  public static void main(final String[] args) {
    new SoundfontWriter().write();
  }

  public void write() {
    try(final SeekableByteChannel channel = Files.newByteChannel(Paths.get("out.sf2"), StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
      final byte[] sbRaw = Files.readAllBytes(Path.of("files/SECT/DRGN0.BIN/5820/3"));

      final ByteBuffer sb = ByteBuffer.wrap(sbRaw);
      sb.order(ByteOrder.LITTLE_ENDIAN);

      final Chunk sfbk = new Chunk("RIFF", "sfbk");
      final Chunk sdta = sfbk.addChild(new Chunk("LIST", "sdta"));

      final byte[][] datas = new byte[22][];
      int totalSize = 0;

      for(int i = 0; i < 22; i++) {
        datas[i] = Files.readAllBytes(Paths.get("files/SECT/DRGN0.BIN/5820/3_%05d.wav".formatted(i)));
        final int size = (int)MathHelper.get(datas[i], 0x2a, 4);
        totalSize += size;
      }

      final byte[] data = new byte[totalSize];
      int offset = 0;
      for(int i = 0; i < 22; i++) {
        final int size = (int)MathHelper.get(datas[i], 0x2a, 4);
        System.arraycopy(datas[i], 0x2a, data, offset, size);
        offset += size;
      }

      sdta.addChild(new Chunk("smpl", "", data));

      final ByteBuffer buffer = ByteBuffer.allocate(sfbk.getSize() + 8);
      this.writeChunk(buffer, sfbk);

      buffer.flip();
      channel.write(buffer);
    } catch(final IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void writeChunk(final ByteBuffer buffer, final Chunk chunk) {
    buffer.put(chunk.format.getBytes(StandardCharsets.US_ASCII));
    IoHelper.write(buffer, chunk.getSize());

    if(!chunk.type.isEmpty()) {
      buffer.put(chunk.type.getBytes(StandardCharsets.US_ASCII));
    }

    buffer.put(chunk.data);

    for(final Chunk child : chunk.children) {
      this.writeChunk(buffer, child);
    }
  }

  public static class Chunk {
    public final String format;
    public final String type;
    public final byte[] data;

    private final List<Chunk> children = new ArrayList<>();

    public Chunk(final String format, final String type, final byte[] data) {
      this.format = format;
      this.type = type;
      this.data = data;
    }

    public Chunk(final String format, final String type) {
      this(format, type, new byte[0]);
    }

    public Chunk addChild(final Chunk child) {
      this.children.add(child);
      return child;
    }

    public int getSize() {
      int size = this.type.length() + this.data.length; // Type + internal data

      for(final Chunk child : this.children) {
        size += child.getSize() + 8; // Child size + header
      }

      return size;
    }
  }
}
