package legend.game.modding.events;

import legend.core.GameEngine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class EventManager {
  private static final Logger LOGGER = LogManager.getFormatterLogger(EventManager.class);

  public static final EventManager INSTANCE = new EventManager();

  private final Map<Consumer<Event>, Class<?>> listeners = new HashMap<>();
  private final Set<Consumer<Event>> staleListeners = Collections.synchronizedSet(new HashSet<>());

  public EventManager() {
    LOGGER.info("Scanning for event consumers...");

    final ConfigurationBuilder config = new ConfigurationBuilder()
      .addClassLoaders(this.getClass().getClassLoader())
      .addUrls(ClasspathHelper.forPackage("legend"));
    final Reflections reflections = new Reflections(GameEngine.MODS.addModsToReflectionsConfig(config));
    final Set<Class<?>> listeners = reflections.getTypesAnnotatedWith(EventListener.class);

    for(final Class<?> listener : listeners) {
      this.register(listener, null);
    }
  }

  private void register(final Class<?> listener, @Nullable final Object instance) {
    for(final Method method : listener.getDeclaredMethods()) {
      if(method.isAnnotationPresent(EventListener.class)) {
        if(!method.canAccess(instance)) {
          LOGGER.warn("Event listener %s must be static", listener);
          continue;
        }

        if(method.getParameterCount() != 1) {
          LOGGER.warn("Event listener %s must have one parameter", listener);
          continue;
        }

        if(!Event.class.isAssignableFrom(method.getParameters()[0].getType())) {
          LOGGER.warn("Event listener %s must have event parameter", listener);
          continue;
        }

        if(instance == null) {
          this.listeners.put(event -> {
            try {
              method.invoke(null, event);
            } catch(IllegalAccessException | InvocationTargetException e) {
              LOGGER.error("Failed to deliver event", e);
            }
          }, method.getParameters()[0].getType());
        } else {
          final WeakReference<Object> ref = new WeakReference<>(instance);
          this.listeners.put(new Consumer<>() {
            @Override
            public void accept(final Event event) {
              if(ref.get() == null) {
                EventManager.this.staleListeners.add(this);
              } else {
                try {
                  method.invoke(ref.get(), event);
                } catch(IllegalAccessException | InvocationTargetException e) {
                  LOGGER.error("Failed to deliver event", e);
                }
              }
            }
          }, method.getParameters()[0].getType());
        }
      }
    }
  }

  public void register(final Object listener) {
    this.register(listener.getClass(), listener);
  }

  public <T extends Event> T postEvent(final T event) {
    for(final var entry : this.listeners.entrySet()) {
      if(entry.getValue().isInstance(event)) {
        entry.getKey().accept(event);
      }
    }

    return event;
  }

  public void clearStaleRefs() {
    this.listeners.keySet().removeAll(this.staleListeners);
    this.staleListeners.clear();
  }
}
