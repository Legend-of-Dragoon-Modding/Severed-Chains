package legend.game.modding.events;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections8.Reflections;
import org.reflections8.util.ClasspathHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EventManager {
  private static final Logger LOGGER = LogManager.getFormatterLogger(EventManager.class);

  public static final EventManager INSTANCE = new EventManager();

  private final Map<Method, Class<?>> listeners = new HashMap<>();

  public EventManager() {
    LOGGER.info("Scanning for entry point class...");
    final Reflections reflections = new Reflections(ClasspathHelper.forClassLoader());
    final Set<Class<?>> listeners = reflections.getTypesAnnotatedWith(EventListener.class);

    for(final Class<?> listener : listeners) {
      for(final Method method : listener.getDeclaredMethods()) {
        if(method.isAnnotationPresent(EventListener.class)) {
          if(!method.canAccess(null)) {
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

          this.listeners.put(method, method.getParameters()[0].getType());
        }
      }
    }
  }

  public void postEvent(final Event event) {
    for(final var entry : this.listeners.entrySet()) {
      if(entry.getValue().isInstance(event)) {
        try {
          entry.getKey().invoke(null, event);
        } catch(IllegalAccessException | InvocationTargetException e) {
          LOGGER.error("Failed to deliver event", e);
        }
      }
    }
  }
}
