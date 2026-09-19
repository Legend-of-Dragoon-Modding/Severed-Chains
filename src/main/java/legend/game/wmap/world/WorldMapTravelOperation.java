package legend.game.wmap.world;

import javax.annotation.Nullable;
import java.util.Objects;

/** Owns one admission, activation, and recovery across the retail loading state machines. */
public final class WorldMapTravelOperation<C> extends WorldMapTransition implements AutoCloseable {
  public record Recovery<C>(C configuration, WorldMapCampaignSnapshot campaign, WorldMapTravelTarget target) {
    public Recovery {
      Objects.requireNonNull(configuration, "configuration");
      Objects.requireNonNull(campaign, "campaign");
      Objects.requireNonNull(target, "target");
    }
  }

  private final WorldMapResourceLoader resources = new WorldMapResourceLoader();
  private C candidate;
  private C activating;
  private Recovery<C> recovery;
  private WorldMapActivation activation;
  private boolean recovering;

  public WorldMapResourceLoader resources() { return this.resources; }
  @Nullable public C candidate() { return this.candidate; }
  @Nullable public C activatingConfiguration() { return this.activating; }
  public boolean recovering() { return this.recovering; }
  public boolean hasRecovery() { return this.recovery != null; }

  /** Admission publishes all transition-owned state together after preflight succeeds. */
  public void admit(final WorldMapTravelTarget target, final boolean respectAccess, final WorldMapActivation activation, @Nullable final C candidate, final Recovery<C> recovery) {
    if(this.phase() != Phase.PREPARING) throw new IllegalStateException("World map admission requires preparation");
    this.activation = Objects.requireNonNull(activation, "activation");
    this.recovery = Objects.requireNonNull(recovery, "recovery");
    this.candidate = candidate;
    this.activating = null;
    this.recovering = false;
    this.queue(target, respectAccess);
  }

  public C takeCandidate() {
    this.activating = Objects.requireNonNull(this.candidate, "No prepared world map configuration");
    this.candidate = null;
    return this.activating;
  }

  public void beginInitialRecovery(final C candidate) {
    if(this.recovering) throw new IllegalStateException("World map recovery already attempted");
    this.recovering = true;
    this.candidate = Objects.requireNonNull(candidate, "candidate");
    this.activating = null;
  }

  /** Retail readiness is an input; effects, publication, and release have one owner. */
  public boolean activateWhenReady(final boolean ready, final Runnable publish) {
    if(!ready || !this.activating()) return false;
    if(this.activation != null) this.activation.apply();
    publish.run();
    this.complete();
    this.release();
    return true;
  }

  /** Cleanup failure remains terminal, but campaign restoration is still attempted. */
  public boolean recover(final Throwable failure, final Runnable cleanup) {
    if(this.activation != null) this.activation.rollback(failure);
    this.activation = null;
    if(this.recovery == null || this.recovering) return false;
    this.recovering = true;
    boolean restored = true;
    try {
      cleanup.run();
    } catch(final RuntimeException | Error cleanupFailure) {
      failure.addSuppressed(cleanupFailure);
      restored = false;
    } finally {
      try {
        this.recovery.campaign().restore();
      } catch(final RuntimeException | Error restoreFailure) {
        failure.addSuppressed(restoreFailure);
        restored = false;
      }
    }
    if(!restored || failure instanceof Error) return false;
    this.candidate = this.recovery.configuration();
    this.activating = null;
    this.queue(this.recovery.target(), false);
    this.begin();
    this.loading();
    return true;
  }

  @Override public void fail() {
    super.fail();
    this.resources.clear();
    this.release();
  }

  @Override public void close() {
    final IllegalStateException cancelled = new IllegalStateException("World map owner closed during travel");
    try {
      if(this.activation != null) this.activation.rollback(cancelled);
      if(this.recovery != null && this.pending()) this.recovery.campaign().restore();
    } finally {
      this.fail();
    }
    if(cancelled.getSuppressed().length != 0) throw cancelled;
  }

  private void release() {
    this.activation = null;
    this.recovery = null;
    this.candidate = null;
    this.activating = null;
    this.recovering = false;
  }
}
