package legend.core.memory.types;

import legend.core.memory.Value;

import java.util.function.BiConsumer;

public class BiConsumerRef<T, U> implements MemoryRef {
  private final Value ref;

  public static <T, U> Class<BiConsumerRef<T, U>> classFor(final Class<T> t, final Class<U> u) {
    //noinspection unchecked
    return (Class<BiConsumerRef<T, U>>)(Class<?>)BiConsumerRef.class;
  }

  public BiConsumerRef(final Value ref) {
    this.ref = ref;

    if(ref.getSize() != 4) {
      throw new IllegalArgumentException("Size of callback refs must be 4");
    }
  }

  public void run(final T t, final U u) {
    this.ref.call(t, u);
  }

  public void set(final BiConsumer<T, U> val) {
    this.ref.set(val);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
