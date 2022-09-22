package legend.core.memory.types;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import legend.core.memory.Value;

import javax.annotation.Nullable;
import java.util.function.Function;
import java.util.function.IntSupplier;

public class UnboundedArrayRef<T extends MemoryRef> implements MemoryRef {
  public static <T extends MemoryRef> Function<Value, UnboundedArrayRef<T>> of(final int stride, final Function<Value, T> constructor) {
    return ref -> new UnboundedArrayRef<>(ref, stride, constructor, null);
  }

  public static <T extends MemoryRef> Function<Value, UnboundedArrayRef<T>> of(final int stride, final Function<Value, T> constructor, final IntSupplier length) {
    return ref -> new UnboundedArrayRef<>(ref, stride, constructor, length);
  }

  public static <T extends MemoryRef> Class<UnboundedArrayRef<T>> classFor(final Class<T> t) {
    //noinspection unchecked
    return (Class<UnboundedArrayRef<T>>)(Class<?>)UnboundedArrayRef.class;
  }

  private final Value ref;
  private final Int2ObjectMap<T> elements;
  private final int stride;
  private final Function<Value, T> constructor;
  @Nullable
  private final IntSupplier length;

  public UnboundedArrayRef(final Value ref, final int stride, final Function<Value, T> constructor, @Nullable final IntSupplier length) {
    this.ref = ref;

    this.elements = new Int2ObjectOpenHashMap<>();
    this.stride = stride;
    this.constructor = constructor;
    this.length = length;
  }

  public T get(final int index) {
    if(this.length != null && index >= this.length.getAsInt()) {
      throw new IndexOutOfBoundsException("Index " + index + " is out of bounds (0 <= n < " + this.length.getAsInt() + ')');
    }

    return this.elements.computeIfAbsent(index, key -> this.constructor.apply(this.ref.offset(this.ref.getSize(), (long)key * this.stride)));
  }

  public UnboundedArrayRef<T> slice(final int offset) {
    return this.ref.offset(offset * this.stride).cast(UnboundedArrayRef.of(this.stride, this.constructor, this.length));
  }

  public ArrayRef<T> bound(final Class<T> cls, final int length) {
    return this.reinterpret(ArrayRef.of(cls, length, this.stride, this.constructor));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
