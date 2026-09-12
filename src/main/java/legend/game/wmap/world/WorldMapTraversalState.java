package legend.game.wmap.world;

import legend.game.modding.events.worldmap.WorldMapTraversalEvent;
import legend.game.types.GameState52c;
import legend.game.wmap.WMap;
import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static legend.core.GameEngine.EVENTS;

/**
 * Per-world-map traversal lifecycle. The owner supplies geometry progress after movement and invokes
 * arrive for discontinuous relocation, including same-route warps. Pause/zoom does not exit a route.
 * A lifecycle callback may relocate the player; the revision then prevents stale movement callbacks
 * or modifiers from being applied to the new arrival.
 */
public final class WorldMapTraversalState {
  private record Crossing(WorldMapTraversalProfile profile, WorldMapTraversalProfile.Marker marker) { }

  private static final WorldMapPoint ZERO = new WorldMapPoint(0.0f, 0.0f, 0.0f);
  private final List<WorldMapTraversalProfile> profiles;
  @Nullable private WorldMapRoute route;
  private float progress;
  private long revision;
  private float speedMultiplier = 1.0f;
  @Nullable private RegistryId avatar;
  private WorldMapPoint visualOffset = ZERO;

  /** Profiles must already be sorted by priority and effective registry ID by the snapshot. */
  public WorldMapTraversalState(final List<WorldMapTraversalProfile> profiles) {
    this.profiles = List.copyOf(profiles);
  }

  public long revision() {
    return this.revision;
  }

  @Nullable
  public WorldMapRoute route() {
    return this.route;
  }

  public float speedMultiplier() {
    return this.speedMultiplier;
  }

  @Nullable
  public RegistryId avatar() {
    return this.avatar;
  }

  public WorldMapPoint visualOffset() {
    return this.visualOffset;
  }

  /** Notify an ordinary route selection after its final endpoint/direction has been established. */
  public void synchronize(final WMap engine, final GameState52c gameState, final WorldMapRoute route, final float progress, final WorldMapTraversalEvent.Cause cause) {
    if(this.route != null && this.route.id().equals(route.id())) {
      this.route = route;
      this.progress = progress;
      return;
    }
    this.arrive(engine, gameState, route, progress, cause);
  }

  /**
   * Notify a discontinuous arrival. Even a same-route warp exits and enters; crossed markers between
   * the old and new positions are deliberately not executed.
   */
  public void arrive(final WMap engine, final GameState52c gameState, final WorldMapRoute route, final float progress, final WorldMapTraversalEvent.Cause cause) {
    final long expected = this.revision + 1;
    this.leave(engine, gameState, cause);
    if(this.revision != expected) return;
    this.route = route;
    this.progress = progress;
    this.resetModifiers();
    this.revision++;
    this.dispatch(engine, gameState, WorldMapTraversalEvent.Phase.ENTER, cause, route, progress, progress, null, null);
  }

  /** Exit once on map teardown or before an engine-state transition. */
  public void leave(final WMap engine, final GameState52c gameState, final WorldMapTraversalEvent.Cause cause) {
    final WorldMapRoute previous = this.route;
    final float previousProgress = this.progress;
    this.route = null;
    this.resetModifiers();
    this.revision++;
    if(previous != null) {
      this.dispatch(engine, gameState, WorldMapTraversalEvent.Phase.EXIT, cause, previous, previousProgress, previousProgress, null, null);
    }
  }

  /** Resolve one active local-movement frame. Modifiers are rebuilt from vanilla defaults each time. */
  public void tick(final WMap engine, final GameState52c gameState) {
    if(this.route == null) return;
    this.resetModifiers();
    final long expected = this.revision;
    final WorldMapTraversalEvent event = this.dispatch(engine, gameState, WorldMapTraversalEvent.Phase.TICK, WorldMapTraversalEvent.Cause.WALK, this.route, this.progress, this.progress, null, null);
    if(this.revision != expected || engine.isWorldMapTravelPending()) return;
    event.validateModifiers();
    this.speedMultiplier = event.speedMultiplier;
    this.avatar = event.avatar;
    this.visualOffset = event.visualOffset;
  }

  /**
   * Report actual continuous motion on the old route before selecting any next route. Clamp progress
   * to its endpoint if movement reaches a junction; do not spend leftover movement on the next route.
   * Crossing intervals exclude departure and include arrival, preventing repeated stationary triggers.
   */
  public void moved(final WMap engine, final GameState52c gameState, final WorldMapRoute route, final float previousProgress, final float progress) {
    if(this.route == null || !this.route.id().equals(route.id()) || previousProgress == progress) return;
    final long expected = this.revision;
    this.progress = progress;
    this.dispatch(engine, gameState, WorldMapTraversalEvent.Phase.MOVE, WorldMapTraversalEvent.Cause.WALK, route, previousProgress, progress, null, null);
    if(this.revision != expected || engine.isWorldMapTravelPending()) return;

    final boolean forwards = progress > previousProgress;
    final List<Crossing> crossings = new ArrayList<>();
    for(final WorldMapTraversalProfile profile : this.profiles) {
      if(!profile.appliesTo(route)) continue;
      for(final WorldMapTraversalProfile.Marker marker : profile.markers()) {
        if(forwards ? marker.progress() > previousProgress && marker.progress() <= progress : marker.progress() < previousProgress && marker.progress() >= progress) {
          crossings.add(new Crossing(profile, marker));
        }
      }
    }
    final Comparator<Crossing> order = Comparator.comparingDouble(crossing -> crossing.marker().progress());
    crossings.sort(forwards ? order : order.reversed());
    for(final Crossing crossing : crossings) {
      // CROSS belongs to its declaring profile; global listeners also receive the marker's stable ID.
      this.dispatch(engine, gameState, WorldMapTraversalEvent.Phase.CROSS, WorldMapTraversalEvent.Cause.WALK, route, previousProgress, crossing.marker().progress(), crossing.marker().id(), crossing.profile());
      if(this.revision != expected || engine.isWorldMapTravelPending()) return;
    }
  }

  private void resetModifiers() {
    this.speedMultiplier = 1.0f;
    this.avatar = this.route == null ? null : this.route.avatar();
    this.visualOffset = ZERO;
  }

  private WorldMapTraversalEvent dispatch(final WMap engine, final GameState52c gameState, final WorldMapTraversalEvent.Phase phase, final WorldMapTraversalEvent.Cause cause, final WorldMapRoute route, final float previousProgress, final float progress, @Nullable final RegistryId marker, @Nullable final WorldMapTraversalProfile markerOwner) {
    final WorldMapTraversalEvent event = new WorldMapTraversalEvent(engine, gameState, phase, cause, route, previousProgress, progress, marker);
    final long expected = this.revision;
    if(markerOwner != null) {
      markerOwner.handler().accept(event);
    } else {
      for(final WorldMapTraversalProfile profile : this.profiles) {
        if(profile.appliesTo(route)) profile.handler().accept(event);
        if(this.revision != expected || phase != WorldMapTraversalEvent.Phase.EXIT && engine.isWorldMapTravelPending()) return event;
      }
    }
    if(this.revision == expected && (phase == WorldMapTraversalEvent.Phase.EXIT || !engine.isWorldMapTravelPending())) EVENTS.postEvent(event);
    return event;
  }
}
