package legend.game.combat.deff;

import legend.core.IoHelper;

public abstract class Lmb extends Anim {
  public static final int MAGIC = 0x42_4d4c;

  public final int count_04;

  public Lmb(final byte[] data, final int offset) {
    super(data, offset);

    this.count_04 = IoHelper.readInt(data, offset + 4);
  }
}
