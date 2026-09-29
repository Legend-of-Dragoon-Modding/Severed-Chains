package legend.game.modding.events.worldmap;

import legend.game.modding.events.engine.InGameEvent;
import legend.game.tim.Tim;
import legend.game.types.GameState52c;
import legend.game.wmap.WMap;
import legend.game.wmap.world.WorldMapPlace;
import legend.game.wmap.world.WorldMapPortal;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/** Resolves the location prompt thumbnail before loading retail data. The engine owns the GPU upload. */
public class WorldMapLocationThumbnailEvent extends InGameEvent<WMap> implements WorldMapEvent {
  public final WorldMapPortal portal;
  public final WorldMapPlace place;
  /** Null retains the retail thumbnail; a replacement runs synchronously on the render thread. */
  @Nullable public Supplier<Tim> thumbnail;

  public WorldMapLocationThumbnailEvent(final WMap engineState, final GameState52c gameState, final WorldMapPortal portal, final WorldMapPlace place) {
    super(engineState, gameState);
    this.portal = portal;
    this.place = place;
  }
}
