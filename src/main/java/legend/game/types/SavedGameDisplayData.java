package legend.game.types;

import static legend.core.MathHelper.get;

public class SavedGameDisplayData {
  public final String filename;

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

  public SavedGameDisplayData(final String filename, final byte[] data) {
    this.filename = filename;

    this.fileIndex = data[0x184] & 0xff;

    this.char0Index = (int)get(data, 0x188, 4);
    this.char1Index = (int)get(data, 0x18c, 4);
    this.char2Index = (int)get(data, 0x190, 4);

    this.level = data[0x194] & 0xff;
    this.dlevel = data[0x195] & 0xff;
    this.currentHp = (int)get(data, 0x196, 2);
    this.maxHp = (int)get(data, 0x198, 2);

    this.gold = (int)get(data, 0x19c, 4);
    this.time = (int)get(data, 0x1a0, 4);
    this.dragoonSpirits = (int)get(data, 0x1a4, 4);
    this.stardust = (int)get(data, 0x1a8, 4);
    this.locationIndex = data[0x1ac] & 0xff;
    this.saveType = data[0x1ad] & 0xff;
  }
}
