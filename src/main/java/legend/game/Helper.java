package legend.game;

import legend.core.memory.Method;

public final class Helper {
  private Helper() { }

  public static <T> T rethrow(final ThrowingSupplier<T> callback) {
    try {
      return callback.get();
    } catch(final Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static long getMethodAddress(final Class<?> cls, final String method, final Class<?>... paramTypes) {
    try {
      return cls.getMethod(method, paramTypes).getAnnotation(Method.class).value();
    } catch(final NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }

  @FunctionalInterface
  public interface ThrowingSupplier<T> {
    T get() throws Exception;
  }
}
