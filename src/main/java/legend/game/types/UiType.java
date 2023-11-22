package legend.game.types;

import legend.core.opengl.Obj;
import legend.game.unpacker.FileData;

import java.util.Arrays;

public class UiType {
  public final UiPart[] entries_08;
  public Obj obj;

  public UiType(final UiPart[] entries_08) {
    this.entries_08 = entries_08;
  }

  public static UiType fromFile(final FileData data) {
    final int entryCount = data.readUShort(0x6);

    final FileData entryIndicesData = data.slice(0x8 + entryCount * 0x8);

    final UiPart[] entries_08 = new UiPart[entryCount];
    Arrays.setAll(entries_08, i -> UiPart.fromFile(data, data.slice(0x8 + i * 0x8, 0x8), entryIndicesData));

    return new UiType(entries_08);
  }
}
