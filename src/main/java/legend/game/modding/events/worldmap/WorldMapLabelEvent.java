package legend.game.modding.events.worldmap;

import legend.game.modding.events.engine.InGameEvent;
import legend.game.types.GameState52c;
import legend.game.wmap.WMap;
import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;

/** Labels use logical UI coordinates. Listeners may replace text, move it, or suppress it. */
public class WorldMapLabelEvent extends InGameEvent<WMap> implements WorldMapEvent {
  public enum Kind {
    LOCATION,
    OBJECTIVE,
    REGION,
  }

  public final Kind kind;
  public final RegistryId region;
  @Nullable public final RegistryId portal;
  public String text;
  public float x;
  public float y;
  public boolean visible = true;

  public WorldMapLabelEvent(final WMap engineState, final GameState52c gameState, final Kind kind, final RegistryId region, @Nullable final RegistryId portal, final String text, final float x, final float y) {
    super(engineState, gameState);
    this.kind = kind;
    this.region = region;
    this.portal = portal;
    this.text = text;
    this.x = x;
    this.y = y;
  }
}
