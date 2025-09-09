package legend.game.modding.events.input;

import legend.core.platform.input.InputAction;
import org.legendofdragoon.modloader.events.Event;

public class InputEvent extends Event {
  public final InputAction action;

  public InputEvent(final InputAction action) {
    this.action = action;
  }
}
