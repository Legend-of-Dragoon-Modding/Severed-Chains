package legend.game.modding.events.worldmap;

import legend.game.modding.events.engine.InGameEvent;
import legend.game.types.GameState52c;
import legend.game.wmap.WMap;
import legend.game.wmap.world.WorldMapProgression;

/**
 * Add named facts to a fresh snapshot. Do not mutate game state or reenter WMap queries here.
 * Fired when legacy flags change or a mod calls WMap.invalidateWorldMap() for its own saved state.
 */
public class WorldMapProgressionEvent extends InGameEvent<WMap> implements WorldMapEvent {
  public final WorldMapProgression.Builder progression;

  public WorldMapProgressionEvent(final WMap engineState, final GameState52c gameState, final WorldMapProgression.Builder progression) {
    super(engineState, gameState);
    this.progression = progression;
  }
}
