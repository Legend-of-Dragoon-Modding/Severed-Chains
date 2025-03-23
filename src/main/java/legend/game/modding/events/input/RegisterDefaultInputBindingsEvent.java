package legend.game.modding.events.input;

import legend.core.platform.input.InputAction;
import legend.core.platform.input.InputActivation;
import legend.core.platform.input.InputBinding;
import org.legendofdragoon.modloader.events.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RegisterDefaultInputBindingsEvent extends Event {
  private final Map<Class<? extends InputActivation>, List> bindings;

  public RegisterDefaultInputBindingsEvent(final Map<Class<? extends InputActivation>, List> bindings) {
    this.bindings = bindings;
  }

  public RegisterDefaultInputBindingsEvent add(final InputAction action, final InputActivation activation) {
    this.bindings.computeIfAbsent(activation.getClass(), key -> new ArrayList<>()).add(new InputBinding(action, activation));
    return this;
  }
}
