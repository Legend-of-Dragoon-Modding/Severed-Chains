package legend.core.platform.input;

import legend.game.modding.coremod.CoreMod;
import legend.game.modding.events.input.RegisterDefaultInputBindingsEvent;
import legend.game.saves.ConfigCollection;
import org.legendofdragoon.modloader.registries.RegistryDelegate;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static legend.core.GameEngine.EVENTS;
import static legend.core.GameEngine.PLATFORM;
import static legend.core.GameEngine.REGISTRIES;

public final class InputBindings {
  private InputBindings() { }

  private static final Map<Class<? extends InputActivation>, List/*<InputBinding<?>>*/> BINDINGS = new HashMap<>();

  public static void initBindings() {
    PLATFORM.resetActionStates();
    BINDINGS.clear();
    EVENTS.postEvent(new RegisterDefaultInputBindingsEvent(BINDINGS));
  }

  public static void loadBindings(final ConfigCollection config) {
    final Map<RegistryDelegate<InputAction>, List<InputActivation>> savedBindings = config.getConfig(CoreMod.CONTROLLER_KEYBINDS_CONFIG.get());
    savedBindings.forEach(InputBindings::overwriteBindings);
  }

  public static void saveBindings(final ConfigCollection config) {
    final Map<RegistryDelegate<InputAction>, List<InputActivation>> activations = new HashMap<>();

    for(final RegistryId actionId : REGISTRIES.inputActions) {
      final RegistryDelegate<InputAction> action = REGISTRIES.inputActions.getEntry(actionId);
      activations.put(action, getActivationsForAction(action.get()));
    }

    config.setConfig(CoreMod.CONTROLLER_KEYBINDS_CONFIG.get(), activations);
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

  public static void overwriteBindings(final RegistryDelegate<InputAction> action, final List<InputActivation> activations) {
    overwriteBindings(action.get(), activations);
  }

  public static void overwriteBindings(final InputAction action, final List<InputActivation> activations) {
    // Clear all existing bindings for the action
    for(final var entry : BINDINGS.entrySet()) {
      final Iterator it = entry.getValue().iterator();

      while(it.hasNext()) {
        final InputBinding binding = (InputBinding)it.next();

        if(binding.action == action) {
          it.remove();
        }
      }
    }

    // Add the new bindings
    for(final InputActivation activation : activations) {
      BINDINGS.computeIfAbsent(activation.getClass(), key -> new ArrayList<>()).add(new InputBinding(action, activation));
    }
  }
}
