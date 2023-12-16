package legend.game.combat.deff;

import legend.game.types.Model124;
import legend.game.unpacker.FileData;

public abstract class Anim {
  public int magic_00;

  public Anim(final FileData data) {
    this.magic_00 = data.readInt(0);
  }

  public abstract void loadIntoModel(final Model124 model);
  public abstract void apply(final Model124 model, final int animationTicks);
}
