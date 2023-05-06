package legend.game.combat.ui;

public enum AdditionOverlayMode {
  FULL("Retail"),
  PARTIAL("No reticle"),
  OFF("HUDless"),
  ;

  public final String name;

  AdditionOverlayMode(final String name) {
    this.name = name;
  }
}
