package legend.core.memory.types;

import legend.core.memory.Value;

public class QuadFunctionRef<T, U, V, W, R> implements MemoryRef {
  private final Value ref;

  public static <T, U, V, W, R> Class<QuadFunctionRef<T, U, V, W, R>> classFor(final Class<T> t, final Class<U> u, final Class<V> v, final Class<W> w, final Class<R> r) {
    //noinspection unchecked
    return (Class<QuadFunctionRef<T, U, V, W, R>>)(Class<?>)QuadFunctionRef.class;
  }

  public QuadFunctionRef(final Value ref) {
    this.ref = ref;

    if(ref.getSize() != 4) {
      throw new IllegalArgumentException("Size of callback refs must be 4");
    }
  }

  public R run(final T t, final U u, final V v, final W w) {
    //noinspection unchecked
    return (R)this.ref.call(t, u, v, w);
  }

  public void set(final Value.QuadFunction<T, U, V, W, R> val) {
    this.ref.set(val);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
