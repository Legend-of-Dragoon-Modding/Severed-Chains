package legend.core.memory.types;

import legend.core.memory.Value;

import java.util.function.Function;
import java.util.function.ToIntFunction;

public final class EnumRef<T extends Enum<T>> implements MemoryRef {
  @SafeVarargs
  public static <T extends Enum<T>> Function<Value, EnumRef<T>> of(final T... values) {
    return EnumRef.of(T::ordinal, values);
  }

  @SafeVarargs
  public static <T extends Enum<T>> Function<Value, EnumRef<T>> of(final ToIntFunction<T> toInt, final T... values) {
    return ref -> new EnumRef<>(ref, values, toInt);
  }

  private final Value ref;
  private final T[] values;
  private final ToIntFunction<T> toInt;

  private EnumRef(final Value ref, final T[] values, final ToIntFunction<T> toInt) {
    this.ref = ref;
    this.values = values;
    this.toInt = toInt;
  }

  public T get() {
    final int val = (int)this.ref.get();

    for(final T t : this.values) {
      if(this.toInt.applyAsInt(t) == val) {
        return t;
      }
    }

    throw new IllegalArgumentException(val + " is not a valid value for this enum ref");
  }

  public void set(final T val) {
    for(final T t : this.values) {
      if(t == val) {
        this.ref.setu(this.toInt.applyAsInt(val));
        return;
      }
    }

    throw new IllegalArgumentException(val + " is not a valid value for this enum ref");
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
