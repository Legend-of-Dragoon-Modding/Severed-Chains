package legend.game.modding.events.input;

import legend.game.input.InputAction;

public class InputPressedEvent extends InputEvent {
  public InputPressedEvent(final InputAction inputAction) {
    super(inputAction);
  }
}
