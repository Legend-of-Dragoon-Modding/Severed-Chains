package legend.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Comparator;

public final class Sort {
  private Sort() { }

  private static final Method sort;

  static {
    try {
      final Class<?> timSort = Class.forName("java.util.TimSort");
      sort = timSort.getDeclaredMethod("sort", Object[].class, int.class, int.class, Comparator.class, Object[].class, int.class, int.class);
      sort.setAccessible(true);
    } catch(final ClassNotFoundException | NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }

  public static <T> void sort(final T[] a, final int lo, final int hi, final Comparator<? super T> c, final T[] work, final int workBase, final int workLen) {
    try {
      sort.invoke(null, a, lo, hi, c, work, workBase, workLen);
    } catch(final IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }
}
