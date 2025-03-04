package legend.game.modding.events.input;

import legend.core.platform.input.InputAction;

public class InputReleasedEvent extends InputEvent {
  public InputReleasedEvent(final InputAction action) {
    super(action);
  }
}
