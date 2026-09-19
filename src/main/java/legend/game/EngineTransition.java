package legend.game;

import javax.annotation.Nullable;
import java.util.Objects;

/** One destination and its optional default return destination travel as an owned request. */
public record EngineTransition(EngineDestination destination, @Nullable EngineDestination returnTo) {
  public EngineTransition {
    Objects.requireNonNull(destination, "destination");
  }

  public EngineTransition(final EngineDestination destination) {
    this(destination, null);
  }

  /** An explicit accepted request owns return semantics, including explicitly choosing no return. */
  public EngineTransition withRequestedReturn(@Nullable final EngineTransition requested) {
    return requested != null && requested.destination.engineState().equals(this.destination.engineState())
      ? new EngineTransition(this.destination, requested.returnTo) : this;
  }
}
