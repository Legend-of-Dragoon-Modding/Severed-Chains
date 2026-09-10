package legend.game.modding.events.worldmap;

import legend.game.modding.events.engine.InGameEvent;
import legend.game.types.GameState52c;
import legend.game.wmap.WMap;
import legend.game.wmap.world.WorldMapDefinition;
import legend.game.wmap.world.WorldMapRules;

/** Configure this WMap instance after importing legacy tables and before resolving any locations. */
public class WorldMapConfigureEvent extends InGameEvent<WMap> implements WorldMapEvent {
  public final WorldMapDefinition.Builder definition;
  public final WorldMapRules.Builder rules;

  public WorldMapConfigureEvent(final WMap engineState, final GameState52c gameState, final WorldMapDefinition.Builder definition, final WorldMapRules.Builder rules) {
    super(engineState, gameState);
    this.definition = definition;
    this.rules = rules;
  }
}
