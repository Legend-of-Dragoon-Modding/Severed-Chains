package legend.game.characters;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;

public class FractionalStat extends Stat {
  protected final Int2IntFunction validator;
  protected int value;
  protected int max;

  public FractionalStat(final StatType<? extends FractionalStat> type, final StatCollection stats, final Int2IntFunction validator) {
    super(type, stats);
    this.validator = validator;
  }

  public void set(final FractionalStat other) {
    this.value = other.value;
    this.max = other.max;
    copyMods(other, this);
  }

  @Override
  public FractionalStat copy() {
    final FractionalStat stat = new FractionalStat((StatType<? extends FractionalStat>)this.type, this.stats, this.validator);
    stat.set(this);
    return stat;
  }

  public int getCurrent() {
    // We could do this in setCurrent, but then you'd have to set the max first or it'd end up clamping to the old maximum
    final int actualMax = this.getMax();
    if(this.value > actualMax) {
      this.value = actualMax;
    }

    return this.value;
  }

  public boolean isFull() {
    return this.getCurrent() == this.getMax();
  }

  public void setCurrent(final int value) {
    this.value = this.validator.applyAsInt(value);
  }

  /** Adds to the current value of this vital */
  public void restore(final int value) {
    this.value += this.validator.applyAsInt(value);
  }

  /** Restore this vital to max */
  public void restore() {
    this.value = this.validator.applyAsInt(this.max);
  }

  public int getMaxRaw() {
    return this.max;
  }

  public void setMaxRaw(final int value) {
    this.max = this.validator.applyAsInt(value);
    final int actualMax = this.getMax();

    if(this.value > actualMax) {
      this.value = actualMax;
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
