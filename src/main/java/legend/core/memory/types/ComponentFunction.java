package legend.core.memory.types;

public interface ComponentFunction<T, U, V, X, Y> {
  int apply(T t, U u, V v, X x, Y y);
}
