package legend.core.memory.types;

@FunctionalInterface
public interface QuintConsumer<T, U, V, W, X> {
  void accept(T t, U u, V v, W w, X x);
}
