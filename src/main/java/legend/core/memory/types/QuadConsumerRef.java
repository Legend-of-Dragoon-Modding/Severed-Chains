package legend.core.memory.types;

import legend.core.memory.Value;

public class QuadConsumerRef<T, U, V, W> implements MemoryRef {
  private final Value ref;

  public static <T, U, V, W> Class<QuadConsumerRef<T, U, V, W>> classFor(final Class<T> t, final Class<U> u, final Class<V> v, final Class<W> w) {
    //noinspection unchecked
    return (Class<QuadConsumerRef<T, U, V, W>>)(Class<?>)QuadConsumerRef.class;
  }

  public QuadConsumerRef(final Value ref) {
    this.ref = ref;

    if(ref.getSize() != 4) {
      throw new IllegalArgumentException("Size of callback refs must be 4");
    }
  }

  public void run(final T t, final U u, final V v, final W w) {
    this.ref.call(t, u, v, w);
  }

  public void set(final QuadConsumer<T, U, V, W> val) {
    this.ref.set(val);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
