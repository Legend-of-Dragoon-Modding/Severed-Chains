package legend.game.scripting;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ScriptFile {
  public final String name;
  public final byte[] data;
  private final int[] ops;

  public ScriptFile(final String name, final byte[] data) {
    this.name = name;
    this.data = data;
    this.ops = new int[data.length / 4];
    ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).asIntBuffer().get(this.ops);
  }

  public int getEntry(final int index) {
    return this.ops[index] / 4;
  }

  public int getOp(final int offset) {
    return this.ops[offset];
  }

  public void setOp(final int offset, final int value) {
    this.ops[offset] = value;
  }

  @Override
  public String toString() {
    return "ScriptFile[" + this.name + ']';
  }
}
