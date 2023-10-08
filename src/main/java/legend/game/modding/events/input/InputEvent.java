package legend.game.modding.events.input;

import legend.game.input.InputAction;
import org.legendofdragoon.modloader.events.Event;

public class InputEvent extends Event {
  public final InputAction inputAction;

  public InputEvent(final InputAction inputAction) {
    this.inputAction = inputAction;
  }
}
