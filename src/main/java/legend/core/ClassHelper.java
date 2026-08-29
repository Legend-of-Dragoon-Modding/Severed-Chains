package legend.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

public final class ClassHelper {
  private ClassHelper() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(ClassHelper.class);

  public static <T> T loadClassWithDefault(@Nullable final String fqcn, final Class<T> expectedType, final Supplier<T> def) {
    T inst = null;

    if(fqcn != null) {
      inst = loadClass(fqcn, expectedType);
    }

    if(inst == null) {
      inst = def.get();
    }

    return inst;
  }

  public static <T> T loadClass(final String fqcn, final Class<T> expectedType) {
    final Class<?> cls;
    try {
      cls = Class.forName(fqcn);
    } catch(final ClassNotFoundException e) {
      LOGGER.error("Failed to find class %s", fqcn);
      return null;
    }

    if(!expectedType.isAssignableFrom(cls)) {
      LOGGER.error("Class %s was not an instance of %s", fqcn, expectedType.getSimpleName());
      return null;
    }

    try {
      return expectedType.cast(cls.getConstructor().newInstance());
    } catch(final InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      LOGGER.error("Failed to create class", e);
      return null;
    }
  }
}
