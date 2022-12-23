package legend.game.types;

import legend.core.MathHelper;

import static legend.core.MathHelper.get;

public class SavedGameDisplayData {
  public final String filename;
  private final byte[] data;
  private final int version;

  public final int fileIndex;

  public final int char0Index;
  public final int char1Index;
  public final int char2Index;

  public final int level;
  public final int dlevel;
  public final int currentHp;
  public final int maxHp;

  public final int gold;
  public final int time;
  public final int dragoonSpirits;
  public final int stardust;
  public final int locationIndex;
  /**
   * 1 - world map
   * 3 - chapter title
   * other - submap
   */
  public final int saveType;

  public static SavedGameDisplayData read(final String filename, final byte[] data) {
    return switch((int)MathHelper.get(data, 0, 4)) {
      case 0x01114353 -> fromRetail(filename, data);
      case 0x76615344 -> v1(filename, data);
      default -> throw new RuntimeException("Invalid saved game file " + filename);
    };
  }

  public static SavedGameDisplayData fromRetail(final String filename, final byte[] data) {
    final int fileIndex = data[0x184] & 0xff;

    final int char0Index = (int)get(data, 0x188, 4);
    final int char1Index = (int)get(data, 0x18c, 4);
    final int char2Index = (int)get(data, 0x190, 4);

    final int level = data[0x194] & 0xff;
    final int dlevel = data[0x195] & 0xff;
    final int currentHp = (int)get(data, 0x196, 2);
    final int maxHp = (int)get(data, 0x198, 2);

    final int gold = (int)get(data, 0x19c, 4);
    final int time = (int)get(data, 0x1a0, 4);
    final int dragoonSpirits = (int)get(data, 0x1a4, 4);
    final int stardust = (int)get(data, 0x1a8, 4);
    final int locationIndex = data[0x1ac] & 0xff;
    final int saveType = data[0x1ad] & 0xff;

    return new SavedGameDisplayData(filename, data, 0, fileIndex, char0Index, char1Index, char2Index, level, dlevel, currentHp, maxHp, gold, time, dragoonSpirits, stardust, locationIndex, saveType);
  }

  public static SavedGameDisplayData v1(final String filename, final byte[] data) {
    final int fileIndex = data[0x04] & 0xff;

    final int char0Index = (int)get(data, 0x08, 4);
    final int char1Index = (int)get(data, 0x0c, 4);
    final int char2Index = (int)get(data, 0x10, 4);

    final int level = data[0x14] & 0xff;
    final int dlevel = data[0x15] & 0xff;
    final int currentHp = (int)get(data, 0x16, 2);
    final int maxHp = (int)get(data, 0x18, 2);

    final int gold = (int)get(data, 0x1c, 4);
    final int time = (int)get(data, 0x20, 4);
    final int dragoonSpirits = (int)get(data, 0x24, 4);
    final int stardust = (int)get(data, 0x28, 4);
    final int locationIndex = data[0x2c] & 0xff;
    final int saveType = data[0x2d] & 0xff;

    return new SavedGameDisplayData(filename, data, 1, fileIndex, char0Index, char1Index, char2Index, level, dlevel, currentHp, maxHp, gold, time, dragoonSpirits, stardust, locationIndex, saveType);
  }

  private SavedGameDisplayData(final String filename, final byte[] data, final int version, final int fileIndex, final int char0Index, final int char1Index, final int char2Index, final int level, final int dlevel, final int currentHp, final int maxHp, final int gold, final int time, final int dragoonSpirits, final int stardust, final int locationIndex, final int saveType) {
    this.data = data;
    this.version = version;
    this.filename = filename;
    this.fileIndex = fileIndex;
    this.char0Index = char0Index;
    this.char1Index = char1Index;
    this.char2Index = char2Index;
    this.level = level;
    this.dlevel = dlevel;
    this.currentHp = currentHp;
    this.maxHp = maxHp;
    this.gold = gold;
    this.time = time;
    this.dragoonSpirits = dragoonSpirits;
    this.stardust = stardust;
    this.locationIndex = locationIndex;
    this.saveType = saveType;
  }
}
