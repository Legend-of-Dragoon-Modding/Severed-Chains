package legend.core.memory.types;

import legend.core.memory.Value;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.function.Function;

public class EnumMapRef<E extends Enum<E>, T extends MemoryRef> implements MemoryRef, Iterable<T> {
  public static <E extends Enum<E>, T extends MemoryRef> Function<Value, EnumMapRef<E, T>> of(final Class<E> keyCls, final Class<T> cls, final int length, final int elementSize, final int stride, final Function<Value, T> constructor) {
    return ref -> new EnumMapRef<>(ref, keyCls, cls, length, elementSize, stride, constructor);
  }

  public static <E extends Enum<E>, T extends MemoryRef> Function<Value, EnumMapRef<E, T>> of(final Class<E> keyCls, final Class<T> cls, final int length, final int stride, final Function<Value, T> constructor) {
    return ref -> new EnumMapRef<>(ref, keyCls, cls, length, stride, constructor);
  }

  public static <E extends Enum<E>, T extends MemoryRef> Class<EnumMapRef<E, T>> classFor(final Class<T> t) {
    //noinspection unchecked
    return (Class<EnumMapRef<E, T>>)(Class<?>)EnumMapRef.class;
  }

  private final Value ref;
  private final T[] elements;

  public EnumMapRef(final Value ref, final Class<E> keyCls, final Class<T> cls, final int length, final int elementSize, final int stride, final Function<Value, T> constructor) {
    this.ref = ref;

    //noinspection unchecked
    this.elements = (T[])Array.newInstance(cls, length);

    for(int i = 0; i < length; i++) {
      this.elements[i] = constructor.apply(ref.offset(ref.getSize(), i * stride));
    }
  }

  public EnumMapRef(final Value ref, final Class<E> keyCls, final Class<T> cls, final int length, final int stride, final Function<Value, T> constructor) {
    this(ref, keyCls, cls, length, stride, stride, constructor);
  }

  private void checkIndex(final int index) {
    if(index < 0 || index >= this.elements.length) {
      throw new IndexOutOfBoundsException("Index " + index + " is out of bounds (0 <= n < " + this.elements.length + ')');
    }
  }

  public T get(final E index) {
    this.checkIndex(index.ordinal());
    return this.elements[index.ordinal()];
  }

  public int length() {
    return this.elements.length;
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }

  @Override
  public Iterator<T> iterator() {
    return new Iterator<T>() {
      private int index;

      @Override
      public boolean hasNext() {
        return this.index < EnumMapRef.this.elements.length;
      }

      @Override
      public T next() {
        return EnumMapRef.this.elements[this.index++];
      }
    };
  }
}
