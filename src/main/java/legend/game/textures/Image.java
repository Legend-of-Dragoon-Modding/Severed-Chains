package legend.game.textures;

import java.nio.ByteBuffer;

public abstract class Image {
  public final int width;
  public final int height;

  public Image(final int width, final int height) {
    this.width = width;
    this.height = height;
  }

  public abstract ByteBuffer getBuffer();
  public abstract void getRow(final int srcPos, final byte[] dest, final int destPos, final int length);
}
