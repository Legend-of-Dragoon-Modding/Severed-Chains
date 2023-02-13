package legend.game.combat.deff;

import legend.core.IoHelper;

public abstract class Anim {
  public int magic_00;

  public Anim(final byte[] data, final int offset) {
    this.magic_00 = IoHelper.readInt(data, offset);
  }
}
