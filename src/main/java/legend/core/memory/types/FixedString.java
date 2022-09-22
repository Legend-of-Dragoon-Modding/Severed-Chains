package legend.core.memory.types;

import legend.core.memory.Value;

import javax.annotation.Nullable;
import java.util.function.Function;

public class FixedString implements MemoryRef {
  public static Function<Value, FixedString> length(final int length) {
    return ref -> new FixedString(ref.offset(length, 0x0L));
  }

  @Nullable
  private final Value ref;

  private final int size;
  private String string;

  public FixedString(final int size) {
    this.ref = null;
    this.size = size;
    this.string = "\0".repeat(size);
  }

  public FixedString(final Value ref) {
    this.ref = ref;
    this.size = ref.getSize();
  }

  public boolean isEmpty() {
    if(this.ref == null) {
      return this.string.charAt(0) == '\0';
    }

    return this.ref.offset(1, 0x0L).get() == 0;
  }

  public String get() {
    if(this.ref == null) {
      return this.string.substring(0, this.string.indexOf('\0'));
    }

    final StringBuilder sb = new StringBuilder();

    for(int offset = 0; offset < this.size; offset++) {
      final char ascii = (char)this.ref.offset(1, offset).get();

      if(ascii == 0) {
        break;
      }

      sb.append(ascii);
    }

    return sb.toString();
  }

  public void set(final String string) {
    if(string.length() >= this.size) {
      throw new IndexOutOfBoundsException("String buffer overrun - string of length " + string.length() + " can't fit within " + this.size + " bytes");
    }

    if(this.ref == null) {
      int end = string.indexOf('\0');
      if(end == -1) {
        end = string.length();
      }

      this.string = string.substring(0, end) + "\0".repeat(this.size - end);
      return;
    }

    for(int offset = 0; offset < string.length(); offset++) {
      this.ref.offset(1, offset).set((byte)string.charAt(offset));
    }

    for(int offset = string.length(); offset < this.ref.getSize(); offset++) {
      this.ref.offset(1, offset).setu(0);
    }
  }

  public void set(final FixedString string) {
    this.set(string.get());
  }

  @Override
  public long getAddress() {
    if(this.ref == null) {
      return 0;
    }

    return this.ref.getAddress();
  }

  @Override
  public String toString() {
    return this.get() + (this.ref == null ? " (local)" : " @ " + Long.toHexString(this.getAddress()));
  }
}
