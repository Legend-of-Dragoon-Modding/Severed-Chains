package legend.core.memory.types;

import legend.core.memory.Value;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.function.Function;

public class ArrayRef<T extends MemoryRef> implements MemoryRef, Iterable<T> {
  public static <T extends MemoryRef> Function<Value, ArrayRef<T>> of(final Class<T> cls, final int length, final int elementSize, final int stride, final Function<Value, T> constructor) {
    return ref -> new ArrayRef<>(ref, cls, length, elementSize, stride, constructor);
  }

  public static <T extends MemoryRef> Function<Value, ArrayRef<T>> of(final Class<T> cls, final int length, final int stride, final Function<Value, T> constructor) {
    return ref -> new ArrayRef<>(ref, cls, length, stride, constructor);
  }

  public static <T extends MemoryRef> Class<ArrayRef<T>> classFor(final Class<T> t) {
    //noinspection unchecked
    return (Class<ArrayRef<T>>)(Class<?>)ArrayRef.class;
  }

  private final Value ref;
  private final T[] elements;

  public ArrayRef(final Value ref, final Class<T> cls, final int length, final int elementSize, final int stride, final Function<Value, T> constructor) {
    this.ref = ref;

    //noinspection unchecked
    this.elements = (T[])Array.newInstance(cls, length);

    for(int i = 0; i < length; i++) {
      this.elements[i] = constructor.apply(ref.offset(elementSize, i * stride));
    }
  }

  public ArrayRef(final Value ref, final Class<T> cls, final int length, final int stride, final Function<Value, T> constructor) {
    this(ref, cls, length, stride, stride, constructor);
  }

  private void checkIndex(final int index) {
    if(index < 0 || index >= this.elements.length) {
      throw new IndexOutOfBoundsException("Index " + index + " is out of bounds (0 <= n < " + this.elements.length + ')');
    }
  }

  public T get(final int index) {
    this.checkIndex(index);
    return this.elements[index];
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
        return this.index < ArrayRef.this.elements.length;
      }

      @Override
      public T next() {
        return ArrayRef.this.elements[this.index++];
      }
    };
  }
}
