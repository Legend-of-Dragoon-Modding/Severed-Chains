package legend.game.combat.deff;

import legend.game.unpacker.FileData;

public abstract class Lmb extends Anim {
  public static final int MAGIC = 0x42_4d4c;

  public final int count_04;

  public Lmb(final FileData data) {
    super(data);

    if(data.readInt(0) != MAGIC) {
      throw new RuntimeException("Not an LMB! Magic: %x".formatted(data.readInt(0)));
    }

    this.count_04 = data.readInt(0x4);
  }
}
