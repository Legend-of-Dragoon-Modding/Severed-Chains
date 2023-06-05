package legend.game.modding.events.input;

import legend.game.input.InputAction;

public class InputReleasedEvent extends InputEvent {
  public InputReleasedEvent(final InputAction inputAction) {
    super(inputAction);
  }
}
