package legend.game.characters;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;

public class UnaryStat extends Stat {
  private final Int2IntFunction validator;
  private int value;

  public UnaryStat(final StatType<UnaryStat> type, final StatCollection stats, final Int2IntFunction validator) {
    super(type, stats);
    this.validator = validator;
  }

  public UnaryStat(final StatType<UnaryStat> type, final StatCollection stats) {
    this(type, stats, i -> i);
  }

  public void set(final UnaryStat other) {
    this.value = other.value;
    copyMods(other, this);
  }

  @Override
  public UnaryStat copy() {
    final UnaryStat stat = new UnaryStat((StatType<UnaryStat>)this.type, this.stats, this.validator);
    stat.set(this);
    return stat;
  }

  public int getRaw() {
    return this.value;
  }

  public void setRaw(final int value) {
    this.value = this.validator.applyAsInt(value);
  }

  public int get() {
    return this.getRaw() + this.getMods();
  }

  public int getMods() {
    int value = 0;

    for(final StatMod mod : this.mods.values()) {
      value += mod.apply(this.stats, this.type);
    }

    return value;
  }
}
