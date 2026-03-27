package legend.game.textures;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;

public class ArrayImage extends Image {
  private final byte[] buffer;

  public ArrayImage(final byte[] buffer, final int width, final int height) {
    super(width, height);
    this.buffer = buffer;
  }

  @Override
  public ByteBuffer getBuffer() {
    final ByteBuffer buffer = BufferUtils.createByteBuffer(this.buffer.length);
    buffer.put(this.buffer);
    return buffer;
  }

  @Override
  public void getRow(final int srcPos, final byte[] dest, final int destPos, final int length) {
    System.arraycopy(this.buffer, srcPos, dest, destPos, length);
  }
}
