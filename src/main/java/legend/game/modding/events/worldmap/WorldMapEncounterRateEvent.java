package legend.game.modding.events.worldmap;

import legend.game.modding.events.engine.InGameEvent;
import legend.game.types.GameState52c;
import legend.game.wmap.WMap;
import legend.game.wmap.world.WorldMapRoute;

/** Adjusts one eligible movement tick before encounter accumulation. Zero increment pauses accumulation. */
public class WorldMapEncounterRateEvent extends InGameEvent<WMap> implements WorldMapEvent {
  public final WorldMapRoute route;
  public final int accumulator;
  public int increment;
  public int threshold = 5120;

  public WorldMapEncounterRateEvent(final WMap engineState, final GameState52c gameState, final WorldMapRoute route, final int accumulator, final int increment) {
    super(engineState, gameState);
    this.route = route;
    this.accumulator = accumulator;
    this.increment = increment;
  }

  public void validate() {
    if(this.increment < 0 || this.threshold <= 0) {
      throw new IllegalArgumentException("World map encounter increment must be nonnegative and threshold must be positive");
    }
  }
}
