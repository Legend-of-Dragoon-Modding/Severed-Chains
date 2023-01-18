package legend.core.memory.types;

import legend.core.memory.Value;

public class TriConsumerRef<T, U, V> implements MemoryRef {
  private final Value ref;

  public static <T, U, V> Class<TriConsumerRef<T, U, V>> classFor(final Class<T> t, final Class<U> u, final Class<V> v) {
    //noinspection unchecked
    return (Class<TriConsumerRef<T, U, V>>)(Class<?>)TriConsumerRef.class;
  }

  public TriConsumerRef(final Value ref) {
    this.ref = ref;

    if(ref.getSize() != 4) {
      throw new IllegalArgumentException("Size of callback refs must be 4");
    }
  }

  public void run(final T t, final U u, final V v) {
    this.ref.call(t, u, v);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
