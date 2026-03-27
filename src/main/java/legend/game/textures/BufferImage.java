package legend.game.textures;

import java.nio.ByteBuffer;

import static org.lwjgl.system.MemoryUtil.memFree;

public class BufferImage extends Image implements AutoCloseable {
  private final ByteBuffer buffer;

  public BufferImage(final ByteBuffer buffer, final int width, final int height) {
    super(width, height);
    this.buffer = buffer;
  }

  @Override
  public ByteBuffer getBuffer() {
    return this.buffer;
  }

  @Override
  public void getRow(final int srcPos, final byte[] dest, final int destPos, final int length) {
    this.buffer.get(srcPos, dest, destPos, length);
  }

  @Override
  public void close() {
    memFree(this.buffer);
  }
}
