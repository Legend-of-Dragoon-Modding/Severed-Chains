package legend.core.memory.types;

import legend.core.memory.Value;

public class QuintConsumerRef<T, U, V, W, X> implements MemoryRef {
  private final Value ref;

  public static <T, U, V, W, X> Class<QuintConsumerRef<T, U, V, W, X>> classFor(final Class<T> t, final Class<U> u, final Class<V> v, final Class<W> w, final Class<X> x) {
    //noinspection unchecked
    return (Class<QuintConsumerRef<T, U, V, W, X>>)(Class<?>)QuintConsumerRef.class;
  }

  public QuintConsumerRef(final Value ref) {
    this.ref = ref;

    if(ref.getSize() != 4) {
      throw new IllegalArgumentException("Size of callback refs must be 4");
    }
  }

  public void run(final T t, final U u, final V v, final W w, final X x) {
    this.ref.call(t, u, v, w, x);
  }

  public void set(final QuintConsumer<T, U, V, W, X> val) {
    this.ref.set(val);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
