package legend.core.spu;

public enum Phase {
  Attack,
  Decay,
  Sustain,
  Release,
  Off,
  ;

  public Phase next() {
    return Phase.values()[(this.ordinal() + 1) % Phase.values().length];
  }
}
