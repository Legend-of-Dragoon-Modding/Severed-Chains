package legend.game.modding.events.input;

import legend.core.platform.input.InputAction;

public class InputPressedEvent extends InputEvent {
  public final boolean repeat;

  public InputPressedEvent(final InputAction action, final boolean repeat) {
    super(action);
    this.repeat = repeat;
  }
}
