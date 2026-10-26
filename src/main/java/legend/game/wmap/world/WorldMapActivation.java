package legend.game.wmap.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Explicit commit/compensation ownership for side effects after destination loading. */
public final class WorldMapActivation {
  private record Effect(Runnable apply, Runnable rollback) { }
  private final List<Effect> effects = new ArrayList<>();
  private int applied;

  public void add(final Runnable apply, final Runnable rollback) {
    this.effects.add(new Effect(Objects.requireNonNull(apply, "apply"), Objects.requireNonNull(rollback, "rollback")));
  }

  public void apply() {
    while(this.applied < this.effects.size()) {
      // Include a partially applied effect in compensation if its callback throws.
      this.effects.get(this.applied++).apply.run();
    }
  }

  public void rollback(final Throwable cause) {
    while(this.applied > 0) {
      try {
        this.effects.get(--this.applied).rollback.run();
      } catch(final RuntimeException | Error failure) {
        cause.addSuppressed(failure);
      }
    }
  }
}
