package legend.game.modding.coremod.config;

public enum QuickTextMode {
  HOLD(0),
  ALWAYS(1),
  INSTANT(2);

  public final int scriptValue;

  QuickTextMode(final int scriptValue) {
    this.scriptValue = scriptValue;
  }
}
