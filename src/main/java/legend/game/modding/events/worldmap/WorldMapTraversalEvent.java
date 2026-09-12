package legend.game.modding.events.worldmap;

import legend.game.modding.events.engine.InGameEvent;
import legend.game.types.GameState52c;
import legend.game.wmap.WMap;
import legend.game.wmap.world.WorldMapPoint;
import legend.game.wmap.world.WorldMapRoute;
import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;
import java.util.Objects;

/**
 * Traversal lifecycle notification. Registered profiles run first, followed by global event listeners.
 * Only TICK consumes modifier fields: multiply speedMultiplier to compose speed changes, set avatar
 * to select a visual (null restores the vanilla player), and set visualOffset to move the rendered
 * player without changing graph position, saves, encounters, or junction matching.
 *
 * Progress is normalized geometry point-interval progress, independent of route direction. MOVE and
 * CROSS describe continuous travel only; relocation emits EXIT/ENTER instead. TICK is suspended while
 * the local walking interaction is paused by menus, camera transitions, or fast-travel cinematics.
 */
public final class WorldMapTraversalEvent extends InGameEvent<WMap> implements WorldMapEvent {
  public enum Phase { ENTER, TICK, MOVE, CROSS, EXIT }
  public enum Cause { ARRIVAL, WALK, TRAVEL, UNLOAD }

  public final Phase phase;
  public final Cause cause;
  public final WorldMapRoute route;
  public final float previousProgress;
  public final float progress;
  @Nullable public final RegistryId marker;

  public float speedMultiplier = 1.0f;
  @Nullable public RegistryId avatar;
  public WorldMapPoint visualOffset = new WorldMapPoint(0.0f, 0.0f, 0.0f);

  public WorldMapTraversalEvent(final WMap engineState, final GameState52c gameState, final Phase phase, final Cause cause, final WorldMapRoute route, final float previousProgress, final float progress, @Nullable final RegistryId marker) {
    super(Objects.requireNonNull(engineState, "engineState"), Objects.requireNonNull(gameState, "gameState"));
    this.phase = Objects.requireNonNull(phase, "phase");
    this.cause = Objects.requireNonNull(cause, "cause");
    this.route = Objects.requireNonNull(route, "route");
    this.previousProgress = previousProgress;
    this.progress = progress;
    this.marker = marker;
    this.avatar = route.avatar();
  }

  public void validateModifiers() {
    if(!Float.isFinite(this.speedMultiplier) || this.speedMultiplier < 0.0f) {
      throw new IllegalArgumentException("World map traversal speed must be finite and nonnegative");
    }
    Objects.requireNonNull(this.visualOffset, "visualOffset");
    if(!Float.isFinite(this.visualOffset.x()) || !Float.isFinite(this.visualOffset.y()) || !Float.isFinite(this.visualOffset.z())) {
      throw new IllegalArgumentException("World map traversal visual offset must be finite");
    }
  }

  /** Geometry point-interval progress, independent of the selected route's direction. */
  public float geometryProgress() {
    return this.progress;
  }

  public float previousGeometryProgress() {
    return this.previousProgress;
  }
}
