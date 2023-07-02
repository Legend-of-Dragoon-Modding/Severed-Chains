package legend.core;

import legend.core.memory.Method;

public final class MemoryHelper {
  private MemoryHelper() { }

  public static long getMethodAddress(final Class<?> cls, final String method, final Class<?>... paramTypes) {
    try {
      return cls.getMethod(method, paramTypes).getAnnotation(Method.class).value();
    } catch(final NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }
}
