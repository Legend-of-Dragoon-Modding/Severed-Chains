package legend.game.characters;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;

public class UnaryStat extends Stat {
  private final Int2IntFunction validator;
  private int value;

  public UnaryStat(final StatCollection stats, final StatType<UnaryStat> type, final Int2IntFunction validator) {
    super(type, stats);
    this.validator = validator;
  }

  public int getRaw() {
    return this.value;
  }

  public void setRaw(final int value) {
    this.value = this.validator.applyAsInt(value);
  }

  public int get() {
    int value = this.getRaw();

    for(final StatMod mod : this.mods) {
      value += mod.apply(this.stats, this.type);
    }

    return value;
  }
}
