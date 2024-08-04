package legend.game.characters;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;

public class FractionalStat extends Stat {
  private final Int2IntFunction validator;
  private int value;
  private int max;

  public FractionalStat(final StatType<? extends FractionalStat> type, final StatCollection stats, final Int2IntFunction validator) {
    super(type, stats);
    this.validator = validator;
  }

  public int getCurrent() {
    // We could do this in setCurrent, but then you'd have to set the max first or it'd end up clamping to the old maximum
    if(this.value > this.max) {
      this.value = this.max;
    }

    return this.value;
  }

  public void setCurrent(final int value) {
    this.value = this.validator.applyAsInt(value);
  }

  public int getMaxRaw() {
    return this.max;
  }

  public void setMaxRaw(final int value) {
    this.max = this.validator.applyAsInt(value);

    if(this.value > this.max) {
      this.value = this.max;
    }
  }

  public int getMax() {
    int value = this.getMaxRaw();

    for(final StatMod mod : this.mods.values()) {
      value += mod.apply(this.stats, this.type);
    }

    return value;
  }
}
