package legend.game.characters;

import legend.game.unpacker.FileData;

public class Addition04 {
  public final int _00;
  public final int spMultiplier_02;
  public final int damageMultiplier_03;

  public static Addition04 fromFile(final FileData data) {
    final int _00 = data.readUShort(0x0);
    final int spMultiplier = data.readUByte(0x2);
    final int damageMultiplier = data.readUByte(0x2);
    return new Addition04(_00, spMultiplier, damageMultiplier);
  }

  public Addition04(final int _00, final int spMultiplier02, final int damageMultiplier03) {
    this._00 = _00;
    this.spMultiplier_02 = spMultiplier02;
    this.damageMultiplier_03 = damageMultiplier03;
  }
}
