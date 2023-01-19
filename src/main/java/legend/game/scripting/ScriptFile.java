package legend.game.scripting;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ScriptFile {
  public final String name;
  private final int[] data;

  public ScriptFile(final String name, final byte[] data) {
    this(name, new int[data.length / 4]);
    ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).asIntBuffer().get(this.data);
  }

  public ScriptFile(final String name, final int[] data) {
    this.name = name;
    this.data = data;
  }

  public int getEntry(final int index) {
    return this.data[index] / 4;
  }

  public int getOp(final int offset) {
    return this.data[offset];
  }

  public void setOp(final int offset, final int value) {
    this.data[offset] = value;
  }
}
