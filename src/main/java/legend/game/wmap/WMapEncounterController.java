package legend.game.wmap;

/** Accumulates world-map encounter progress without changing event or RNG ordering. */
final class WMapEncounterController {
  private WMapEncounterController() {
  }

  static Accumulation accumulate(final int accumulator, final int increment, final int threshold) {
    final int updated = (int)Math.min(Integer.MAX_VALUE, (long)accumulator + increment);
    return updated >= threshold ? new Accumulation(0, true) : new Accumulation(updated, false);
  }

  record Accumulation(int accumulator, boolean encounterTriggered) {
  }
}
