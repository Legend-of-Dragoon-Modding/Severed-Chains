package legend.game.modding.events.worldmap;

import legend.game.modding.events.engine.InGameEvent;
import legend.game.types.GameState52c;
import legend.game.wmap.WMap;
import legend.game.wmap.world.WorldMapRenderContext;
import org.joml.Vector3f;

/** Fired after the vanilla camera update, before committing the frame's camera transform. */
public class WorldMapCameraEvent extends InGameEvent<WMap> implements WorldMapEvent {
  public final WorldMapRenderContext context;
  public final Vector3f viewpoint;
  public final Vector3f refpoint;
  public final Vector3f target;
  public float projectionDistance;

  public WorldMapCameraEvent(final WMap engineState, final GameState52c gameState, final WorldMapRenderContext context, final Vector3f viewpoint, final Vector3f refpoint, final Vector3f target, final float projectionDistance) {
    super(engineState, gameState);
    this.context = context;
    this.viewpoint = new Vector3f(viewpoint);
    this.refpoint = new Vector3f(refpoint);
    this.target = new Vector3f(target);
    this.projectionDistance = projectionDistance;
  }
}
