package legend.game.saves.types;

public class RetailSaveDisplay extends SaveDisplay {
  public final String location;
  public final int maxHp;
  public final int maxMp;

  public RetailSaveDisplay(final String location, final int maxHp, final int maxMp) {
    this.location = location;
    this.maxHp = maxHp;
    this.maxMp = maxMp;
  }
}
