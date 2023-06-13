package legend.game.modding;

public enum ModState {
  INITIALIZED,
  READY,
  ;

  public boolean isReady() {
    return this == READY;
  }
}
