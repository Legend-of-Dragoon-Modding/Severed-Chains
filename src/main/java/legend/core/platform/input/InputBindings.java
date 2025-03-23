package legend.core.platform.input;

import legend.game.modding.events.input.RegisterDefaultInputBindingsEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static legend.core.GameEngine.EVENTS;
import static legend.core.GameEngine.PLATFORM;

public final class InputBindings {
  private InputBindings() { }

  private static final Map<Class<? extends InputActivation>, List> BINDINGS = new HashMap<>();

  public static void loadBindings() {
    PLATFORM.resetActionStates();
    BINDINGS.clear();
    EVENTS.postEvent(new RegisterDefaultInputBindingsEvent(BINDINGS));
  }

  public static <T extends InputActivation> List<InputBinding<T>> getBindings(final Class<T> cls) {
    final List list = BINDINGS.get(cls);

    if(list == null) {
      return List.of();
    }

    return list;
  }

  public static List<InputActivation> getActivationsForAction(final InputAction action) {
    final List<InputActivation> activations = new ArrayList<>();

    for(final List<InputBinding<?>> bindings : BINDINGS.values()) {
      for(final InputBinding<?> binding : bindings) {
        if(binding.action == action) {
          activations.add(binding.activation);
        }
      }
    }

    return activations;
  }
}
