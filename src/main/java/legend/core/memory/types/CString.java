package legend.core.memory.types;

import legend.core.memory.Value;

import java.util.function.Function;

public class CString implements MemoryRef {
  public static Function<Value, CString> maxLength(final int length) {
    return ref -> new CString(ref.offset(length, 0x0L));
  }

  private final Value ref;

  public CString(final Value ref) {
    this.ref = ref;
  }

  public String get() {
    final StringBuilder sb = new StringBuilder();

    for(int offset = 0; ; offset++) {
      if(offset > this.ref.getSize()) {
        throw new IndexOutOfBoundsException("String buffer overrun - didn't find null terminator before reaching max length (" + this.ref.getSize() + "). Data was " + sb);
      }

      final char ascii = (char)this.ref.offset(1, offset).get();

      if(ascii == 0) {
        break;
      }

      sb.append(ascii);
    }

    return sb.toString();
  }

  public void set(final String string) {
    if(string.length() + 1 > this.ref.getSize()) {
      throw new IndexOutOfBoundsException("String buffer overrun - string \"" + string + "\" of length " + string.length() + " (+ null terminator) doesn't fit within " + this.ref.getSize() + " bytes");
    }

    for(int offset = 0; offset < string.length(); offset++) {
      this.ref.offset(1, offset).setu((byte)string.charAt(offset));
    }

    this.ref.offset(1, string.length()).setu(0);
  }

  public char charAt(final int index) {
    if(index >= this.ref.getSize() - 1) {
      throw new IndexOutOfBoundsException("String buffer overrun - index " + index + ", length " + this.ref.getSize());
    }

    return (char)this.ref.offset(1, index).get();
  }

  public void charAt(final int index, final char chr) {
    if(index >= this.ref.getSize() - 1) {
      throw new IndexOutOfBoundsException("String buffer overrun - index " + index + ", length " + this.ref.getSize());
    }

    this.ref.offset(1, index).setu(chr);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }

  @Override
  public String toString() {
    return this.get() + (this.ref == null ? " (local)" : " @ " + Long.toHexString(this.getAddress()));
  }
}
