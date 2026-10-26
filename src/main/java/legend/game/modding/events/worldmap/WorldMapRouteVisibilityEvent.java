package legend.game.modding.events.worldmap;

import legend.game.modding.events.engine.InGameEvent;
import legend.game.types.GameState52c;
import legend.game.wmap.WMap;
import legend.game.wmap.world.WorldMapPortal;
import legend.game.wmap.world.WorldMapRenderContext;

/** Visibility only; traversal and entry access remain controlled by WorldMapRules. */
public class WorldMapRouteVisibilityEvent extends InGameEvent<WMap> implements WorldMapEvent {
  public final WorldMapRenderContext context;
  public final WorldMapPortal portal;
  public boolean visible;

  public WorldMapRouteVisibilityEvent(final WMap engineState, final GameState52c gameState, final WorldMapRenderContext context, final WorldMapPortal portal, final boolean visible) {
    super(engineState, gameState);
    this.context = context;
    this.portal = portal;
    this.visible = visible;
  }
}
