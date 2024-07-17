package legend.game.characters;

public class FractionalStatModConfig implements StatModConfig {
  protected int amount;
  protected boolean percentile;
  protected int turns = -1;

  /** A percentage modifier (e.g. 200% buff would be 200, -20% debuff would be -20) */
  public FractionalStatModConfig percent(final int percent) {
    this.amount = percent;
    this.percentile = true;
    return this;
  }

  /** A flat modifier (e.g. 20 would add 20 to the stat) */
  public FractionalStatModConfig flat(final int percent) {
    this.amount = percent;
    this.percentile = false;
    return this;
  }

  public FractionalStatModConfig turns(final int turns) {
    this.turns = turns;
    return this;
  }

  public FractionalStatModConfig permanent() {
    return this.turns(-1);
  }
}
