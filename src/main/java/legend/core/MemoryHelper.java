package legend.core;

import legend.core.Hardware;
import legend.core.memory.Method;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.BiFunctionRef;
import legend.core.memory.types.FunctionRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.UnboundedArrayRef;

import java.lang.reflect.Field;

public final class MemoryHelper {
  private MemoryHelper() { }

  public static long getMethodAddress(final Class<?> cls, final String method, final Class<?>... paramTypes) {
    try {
      return cls.getMethod(method, paramTypes).getAnnotation(Method.class).value();
    } catch(final NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }

  public static <T, U, R> BiFunctionRef<T, U, R> getBiFunctionAddress(final Class<?> cls, final String method, final Class<T> arg1, final Class<U> arg2, final Class<R> ret) {
    return Hardware.MEMORY.ref(4, getMethodAddress(cls, method, arg1, arg2), BiFunctionRef::new);
  }

  public static <T, R> FunctionRef<T, R> getFunctionAddress(final Class<?> cls, final String method, final Class<T> arg, final Class<R> ret) {
    return Hardware.MEMORY.ref(4, getMethodAddress(cls, method, arg), FunctionRef::new);
  }

  public static <T extends MemoryRef> void copyPointerTypes(final T dest, final T src) {
    try {
      for(final Field field : dest.getClass().getFields()) {
        if(field.getType() == Pointer.class) {
          final Pointer srcPtr = (Pointer)src.getClass().getField(field.getName()).get(src);
          final Pointer destPtr = (Pointer)field.get(dest);

          if(srcPtr.isNull()) {
            destPtr.clear();
          } else {
            if(srcPtr.deref() instanceof ArrayRef<?> || srcPtr.deref() instanceof UnboundedArrayRef<?>) {
              continue;
            }

            destPtr.set(srcPtr.deref().getClass().getConstructor(Value.class).newInstance(Hardware.MEMORY.ref(4, destPtr.getPointer())));

            copyPointerTypes(destPtr.deref(), srcPtr.deref());
          }
        }
      }
    } catch(final Exception e) {
      throw new RuntimeException("Failed to copy pointers", e);
    }
  }
}
