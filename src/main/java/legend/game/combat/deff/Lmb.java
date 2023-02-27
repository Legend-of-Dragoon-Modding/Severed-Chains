package legend.game.combat.deff;

import legend.game.unpacker.FileData;

public abstract class Lmb extends Anim {
  public static final int MAGIC = 0x42_4d4c;

  public final int count_04;

  public Lmb(final FileData data) {
    super(data);

    this.count_04 = data.readInt(0x4);
  }
}
