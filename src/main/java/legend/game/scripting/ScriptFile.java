package legend.game.scripting;

import legend.core.MathHelper;
import legend.core.memory.types.IntRef;
import legend.game.unpacker.FileData;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class ScriptFile {
  private final ScriptManager manager;
  public final String name;
  private final ScriptFile[] includes;
  final String[] includeFiles;
  final int[] includeOffsets;
  public final byte[] data;
  private final int[] ops;

  public ScriptFile(final ScriptManager manager, final String name, final byte[] data) {
    this.manager = manager;
    this.name = name;
    this.data = data;

    final FileData header = new FileData(data);
    final IntRef offset = new IntRef();

    // Enhanced script format
    if(header.size() >= 0x8 && header.readInt(offset) == 0x0 && header.readInt(offset) == 0x5c000001) {
      this.includes = new ScriptFile[header.readInt(offset) + 1];
      this.includeFiles = new String[this.includes.length];
      this.includeOffsets = new int[this.includes.length];
      this.includes[0] = this;

      for(int includeIndex = 1; includeIndex < this.includes.length; includeIndex++) {
        this.includeOffsets[includeIndex] = header.readInt(offset);
        final char[] path = new char[header.readInt(offset)];

        for(int i = 0; i < path.length; i++) {
          path[i] = header.readChar(offset);
        }

        this.includeFiles[includeIndex] = new String(path);

        offset.set(MathHelper.roundUp(offset.get(), 4));
      }
    } else {
      this.includes = new ScriptFile[] {this};
      this.includeFiles = new String[] {null};
      this.includeOffsets = new int[1];
      offset.set(0);
    }

    this.ops = new int[(data.length - offset.get()) / 4];
    ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).asIntBuffer().get(offset.get() / 4, this.ops);
  }

  private int getIncludeForOffset(final int offset) {
    // optimize handling for main script
    if(offset < this.ops.length) {
      return 0;
    }

    for(int i = this.includeOffsets.length - 1; i >= 1; i--) {
      if(offset >= this.includeOffsets[i]) {
        this.getInclude(i);
        return i;
      }
    }

    throw new RuntimeException("Invalid offset " + offset + " for script " + this.name + " with includes " + Arrays.toString(this.includes) + " with offsets " + Arrays.toString(this.includeOffsets));
  }

  ScriptFile getInclude(final int includeIndex) {
    // Lazy load includes
    if(this.includes[includeIndex] == null) {
      try {
        this.includes[includeIndex] = this.manager.loadScriptInclude(this.includeFiles[includeIndex]);
      } catch(final IOException e) {
        throw new RuntimeException("Failed to load include " + this.includeFiles[includeIndex] + " for " + this.name, e);
      }
    }

    return this.includes[includeIndex];
  }

  public ScriptFile getIncludeAdjustedFile(final int offset) {
    final int includeIndex = this.getIncludeForOffset(offset);
    return this.includes[includeIndex];
  }

  public int getIncludeAdjustedOffset(final int offset) {
    final int includeIndex = this.getIncludeForOffset(offset);
    return offset - this.includeOffsets[includeIndex];
  }

  public int getEntry(final int index) {
    final int includeIndex = this.getIncludeForOffset(index);
    return this.includes[includeIndex].ops[index - this.includeOffsets[includeIndex]] / 4;
  }

  public int getOp(final int offset) {
    final int includeIndex = this.getIncludeForOffset(offset);

    if(offset - this.includeOffsets[includeIndex] >= this.includes[includeIndex].ops.length) {
      int a = 0;
    }

    return this.includes[includeIndex].ops[offset - this.includeOffsets[includeIndex]];
  }

  public void setOp(final int offset, final int value) {
    final int includeIndex = this.getIncludeForOffset(offset);
    this.includes[includeIndex].ops[offset - this.includeOffsets[includeIndex]] = value;
  }

  @Override
  public String toString() {
    return "ScriptFile[" + this.name + ']';
  }
}
