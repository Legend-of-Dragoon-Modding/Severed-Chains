package legend.core.memory.types;

import legend.core.GameEngine;
import legend.core.memory.Value;

import javax.annotation.Nullable;
import java.util.function.Function;

public class RelativePointer<T extends MemoryRef> implements MemoryRef {
  public static <T extends MemoryRef> Function<Value, RelativePointer<T>> of(final int size, final Function<Value, T> constructor) {
    return ref -> new RelativePointer<>(ref, constructor, ref.getAddress(), size, 0, true);
  }

  public static <T extends MemoryRef> Function<Value, RelativePointer<T>> of(final int size, final Function<Value, T> constructor, final long nullValue) {
    return ref -> new RelativePointer<>(ref, constructor, ref.getAddress(), size, nullValue, true);
  }

  public static <T extends MemoryRef> Function<Value, RelativePointer<T>> of(final int size, final long baseAddress, final Function<Value, T> constructor) {
    return ref -> new RelativePointer<>(ref, constructor, baseAddress, size, 0, true);
  }

  public static <T extends MemoryRef> Function<Value, RelativePointer<T>> of(final int size, final long baseAddress, final Function<Value, T> constructor, final long nullValue) {
    return ref -> new RelativePointer<>(ref, constructor, baseAddress, size, nullValue, true);
  }

  /**
   * Lazy mode - don't resolve pointer until used
   */
  public static <T extends MemoryRef> Function<Value, RelativePointer<T>> deferred(final int size, final Function<Value, T> constructor) {
    return ref -> new RelativePointer<>(ref, constructor, ref.getAddress(), size, 0, false);
  }

  /**
   * Lazy mode - don't resolve pointer until used
   */
  public static <T extends MemoryRef> Function<Value, RelativePointer<T>> deferred(final int size, final Function<Value, T> constructor, final long nullValue) {
    return ref -> new RelativePointer<>(ref, constructor, ref.getAddress(), size, nullValue, false);
  }

  /**
   * Lazy mode - don't resolve pointer until used
   */
  public static <T extends MemoryRef> Function<Value, RelativePointer<T>> deferred(final int size, final long baseAddress, final Function<Value, T> constructor) {
    return ref -> new RelativePointer<>(ref, constructor, baseAddress, size, 0, false);
  }

  /**
   * Lazy mode - don't resolve pointer until used
   */
  public static <T extends MemoryRef> Function<Value, RelativePointer<T>> deferred(final int size, final long baseAddress, final Function<Value, T> constructor, final long nullValue) {
    return ref -> new RelativePointer<>(ref, constructor, baseAddress, size, nullValue, false);
  }

  public static <T extends MemoryRef> Class<RelativePointer<T>> classFor(final Class<T> t) {
    //noinspection unchecked
    return (Class<RelativePointer<T>>)(Class<?>)RelativePointer.class;
  }

  private final Value ref;
  private final Function<Value, T> constructor;
  private final long baseAddress;
  private final int size;
  private final long nullValue;
  @Nullable
  private T cache;

  public RelativePointer(final Value ref, final Function<Value, T> constructor, final long baseAddress, final int size, final long nullValue, final boolean precache) {
    this.ref = ref;

    this.constructor = constructor;
    this.baseAddress = baseAddress;
    this.size = size;
    this.nullValue = nullValue;

    if(precache) {
      try {
        this.updateCache();
      } catch(final IllegalArgumentException ignored) {}
    }
  }

  private void updateCache() {
    if(this.isNull()) {
      this.cache = null;
      return;
    }

    this.cache = this.constructor.apply(GameEngine.MEMORY.ref(this.size, this.baseAddress + this.ref.get()));
  }

  public boolean isNull() {
    return this.ref.get() == this.nullValue;
  }

  public T deref() {
    final T value = this.derefNullable();

    if(value == null) {
      throw new NullPointerException("Relative pointer " + Long.toHexString(this.getAddress()) + " is null");
    }

    return value;
  }

  @Nullable
  public T derefNullable() {
    if(this.isNull()) {
      this.cache = null;
      return null;
    }

    if(this.cache == null || this.baseAddress + this.ref.get() != this.cache.getAddress()) {
      this.updateCache();
    }

    return this.cache;
  }

  public <U> U derefAs(final Class<U> cls) {
    return cls.cast(this.deref());
  }

  public RelativePointer<T> set(final T ref) {
    this.ref.setu(ref.getAddress() - this.baseAddress);
    this.cache = ref;
    return this;
  }

  public RelativePointer<T> add(final long amount) {
    this.ref.addu(amount);
    this.cache = null;
    return this;
  }

  public RelativePointer<T> sub(final long amount) {
    this.ref.subu(amount);
    this.cache = null;
    return this;
  }

  public RelativePointer<T> incr() {
    this.add(this.size);
    this.cache = null;
    return this;
  }

  public RelativePointer<T> decr() {
    this.sub(this.size);
    this.cache = null;
    return this;
  }

  public long getPointer() {
    return this.ref.get();
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
