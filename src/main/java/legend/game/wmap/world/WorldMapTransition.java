package legend.game.wmap.world;

import javax.annotation.Nullable;
import java.util.Objects;

/** Owns a travel request from admission through the first playable destination frame. */
public final class WorldMapTransition {
  public enum Phase { IDLE, PREPARING, QUEUED, FADING, LOADING, ACTIVATING, FAILED }

  private Phase phase = Phase.IDLE;
  @Nullable private WorldMapTravelTarget target;
  private boolean respectAccess;

  public Phase phase() { return this.phase; }
  public boolean pending() { return this.phase != Phase.IDLE && this.phase != Phase.FAILED; }
  public boolean queued() { return this.phase == Phase.QUEUED; }
  public boolean arriving() { return this.phase == Phase.FADING || this.phase == Phase.LOADING; }
  public boolean activating() { return this.phase == Phase.ACTIVATING; }
  public WorldMapTravelTarget target() { return Objects.requireNonNull(this.target, "No world map travel target"); }
  public boolean respectAccess() { return this.respectAccess; }

  public boolean prepare(final boolean ready) {
    if(!ready || this.pending()) return false;
    this.phase = Phase.PREPARING;
    return true;
  }

  public void queue(final WorldMapTravelTarget target, final boolean respectAccess) {
    this.target = Objects.requireNonNull(target, "target");
    this.respectAccess = respectAccess;
    this.phase = Phase.QUEUED;
  }

  public void finishPreparation() {
    if(this.phase == Phase.PREPARING) this.clear();
  }

  public boolean begin() {
    if(!this.queued()) return false;
    this.phase = Phase.FADING;
    return true;
  }

  public void loading() {
    if(this.arriving()) this.phase = Phase.LOADING;
  }

  public void activate() {
    if(this.arriving()) this.phase = Phase.ACTIVATING;
  }

  public WorldMapTravelTarget complete() {
    final WorldMapTravelTarget result = this.target();
    this.clear();
    return result;
  }

  public void fail() {
    this.target = null;
    this.phase = Phase.FAILED;
  }

  private void clear() {
    this.target = null;
    this.phase = Phase.IDLE;
  }
}
