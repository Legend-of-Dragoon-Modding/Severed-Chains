package legend.game.types;

public enum TexPageY {
  Y_0,
  Y_256,
  ;

  public static TexPageY fromY(final int y) {
    return y < 256 ? Y_0 : Y_256;
  }
}
