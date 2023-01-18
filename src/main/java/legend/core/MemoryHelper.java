package legend.core;

import legend.core.memory.Method;
import legend.core.memory.types.BiFunctionRef;

public final class MemoryHelper {
  private MemoryHelper() { }

  public static long getMethodAddress(final Class<?> cls, final String method, final Class<?>... paramTypes) {
    try {
      return cls.getMethod(method, paramTypes).getAnnotation(Method.class).value();
    } catch(final NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }

  public static <T, U, R> BiFunctionRef<T, U, R> getBiFunctionAddress(final Class<?> cls, final String method, final Class<T> arg1, final Class<U> arg2, final Class<R> ret) {
    return GameEngine.MEMORY.ref(4, getMethodAddress(cls, method, arg1, arg2), BiFunctionRef::new);
  }
}
