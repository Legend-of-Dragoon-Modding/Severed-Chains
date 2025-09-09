package legend.game.combat;

public enum MashMode {
  MASH(0),
  HOLD(1),
  AUTO(2),
  ;

  public final int scriptValue;

  MashMode(final int scriptValue) {
    this.scriptValue = scriptValue;
  }
}
