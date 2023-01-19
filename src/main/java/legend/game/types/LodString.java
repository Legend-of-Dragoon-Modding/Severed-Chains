package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.game.scripting.Param;

import javax.annotation.Nullable;

public class LodString implements MemoryRef {
  @Nullable
  private final Value ref;
  @Nullable
  private final int[] chars;

  public LodString(final Value ref) {
    this.ref = ref;
    this.chars = null;
  }

  public LodString(final int length) {
    this.ref = null;
    this.chars = new int[length];
  }

  public LodString(final int[] chars) {
    this.ref = null;
    this.chars = chars;
  }

  public LodString(final String text) {
    this(text.length() + 1);
    this.set(text);
  }

  public static LodString fromParam(final Param param) {
    int charCount = 0;
    for(int paramIndex = 0; ; paramIndex++) {
      final Param p = param.array(paramIndex);

      charCount++;
      if((p.get() & 0xffff) == 0xa0ff) {
        break;
      }

      charCount++;
      if((p.get() >>> 16 & 0xffff) == 0xa0ff) {
        break;
      }
    }

    final int[] chars = new int[charCount];
    for(int charIndex = 0; charIndex < charCount; charIndex++) {
      final Param p = param.array(charIndex / 2);
      chars[charIndex] = p.get() >>> ((charIndex & 1) * 16) & 0xffff;
    }

    return new LodString(chars);
  }

  public String get() {
    final StringBuilder sb = new StringBuilder();

    for(int i = 0; i < (this.chars != null ? this.chars.length : 500); i++) {
      final long c = this.charAt(i);

      if(c == 0xa0ffL || c == 0xffffL) {
        break;
      }

      sb.append(switch((int)c) {
        case 0x00 -> ' ';
        case 0x01 -> ',';
        case 0x02 -> '.';
        case 0x03 -> '\u00b7';
        case 0x04 -> ':';
        case 0x05 -> '?';
        case 0x06 -> '!';
        case 0x07 -> '_';
        case 0x08 -> '/';
        case 0x09 -> '\'';
        case 0x0a -> '"';
        case 0x0b -> '(';
        case 0x0c -> ')';
        case 0x0d -> '-';
        case 0x0e -> '`';
        case 0x0f -> '%';
        case 0x10 -> '&';
        case 0x11 -> '*';
        case 0x12 -> '@';
        case 0x13 -> '+';
        case 0x14 -> '~';
        case 0x15 -> '0';
        case 0x16 -> '1';
        case 0x17 -> '2';
        case 0x18 -> '3';
        case 0x19 -> '4';
        case 0x1a -> '5';
        case 0x1b -> '6';
        case 0x1c -> '7';
        case 0x1d -> '8';
        case 0x1e -> '9';
        case 0x1f -> 'A';
        case 0x20 -> 'B';
        case 0x21 -> 'C';
        case 0x22 -> 'D';
        case 0x23 -> 'E';
        case 0x24 -> 'F';
        case 0x25 -> 'G';
        case 0x26 -> 'H';
        case 0x27 -> 'I';
        case 0x28 -> 'J';
        case 0x29 -> 'K';
        case 0x2a -> 'L';
        case 0x2b -> 'M';
        case 0x2c -> 'N';
        case 0x2d -> 'O';
        case 0x2e -> 'P';
        case 0x2f -> 'Q';
        case 0x30 -> 'R';
        case 0x31 -> 'S';
        case 0x32 -> 'T';
        case 0x33 -> 'U';
        case 0x34 -> 'V';
        case 0x35 -> 'W';
        case 0x36 -> 'X';
        case 0x37 -> 'Y';
        case 0x38 -> 'Z';
        case 0x39 -> 'a';
        case 0x3a -> 'b';
        case 0x3b -> 'c';
        case 0x3c -> 'd';
        case 0x3d -> 'e';
        case 0x3e -> 'f';
        case 0x3f -> 'g';
        case 0x40 -> 'h';
        case 0x41 -> 'i';
        case 0x42 -> 'j';
        case 0x43 -> 'k';
        case 0x44 -> 'l';
        case 0x45 -> 'm';
        case 0x46 -> 'n';
        case 0x47 -> 'o';
        case 0x48 -> 'p';
        case 0x49 -> 'q';
        case 0x4a -> 'r';
        case 0x4b -> 's';
        case 0x4c -> 't';
        case 0x4d -> 'u';
        case 0x4e -> 'v';
        case 0x4f -> 'w';
        case 0x50 -> 'x';
        case 0x51 -> 'y';
        case 0x52 -> 'z';
        case 0x53 -> '[';
        case 0x54 -> ']';
        case 0x55 -> ';';
        case 0xa1ff -> '\n';
        default -> '?';
      });
    }

    return sb.toString();
  }

  public int charAt(final int index) {
    if(this.ref == null) {
      if(index >= this.chars.length) {
        throw new IndexOutOfBoundsException("Index %d out of bounds for length %d".formatted(index, this.chars.length));
      }

      return this.chars[index];
    }

    return (int)this.ref.offset(2, index * 0x2L).get();
  }

  public void charAt(final int index, final int c) {
    if(this.ref == null) {
      this.chars[index] = c;
      return;
    }

    this.ref.offset(2, index * 0x2L).setu(c);
  }

  public void set(final String text) {
    for(int i = 0; i < text.length(); i++) {
      this.charAt(i, switch(text.charAt(i)) {
        case ' ' -> 0x00;
        case ',' -> 0x01;
        case '.' -> 0x02;
        case '\u00b7' -> 0x03;
        case ':' -> 0x04;
        case '?' -> 0x05;
        case '!' -> 0x06;
        case '_' -> 0x07;
        case '/' -> 0x08;
        case '\'' -> 0x09;
        case '"' -> 0x0a;
        case '(' -> 0x0b;
        case ')' -> 0x0c;
        case '-' -> 0x0d;
        case '`' -> 0x0e;
        case '%' -> 0x0f;
        case '&' -> 0x10;
        case '*' -> 0x11;
        case '@' -> 0x12;
        case '+' -> 0x13;
        case '~' -> 0x14;
        case '0' -> 0x15;
        case '1' -> 0x16;
        case '2' -> 0x17;
        case '3' -> 0x18;
        case '4' -> 0x19;
        case '5' -> 0x1a;
        case '6' -> 0x1b;
        case '7' -> 0x1c;
        case '8' -> 0x1d;
        case '9' -> 0x1e;
        case 'A' -> 0x1f;
        case 'B' -> 0x20;
        case 'C' -> 0x21;
        case 'D' -> 0x22;
        case 'E' -> 0x23;
        case 'F' -> 0x24;
        case 'G' -> 0x25;
        case 'H' -> 0x26;
        case 'I' -> 0x27;
        case 'J' -> 0x28;
        case 'K' -> 0x29;
        case 'L' -> 0x2a;
        case 'M' -> 0x2b;
        case 'N' -> 0x2c;
        case 'O' -> 0x2d;
        case 'P' -> 0x2e;
        case 'Q' -> 0x2f;
        case 'R' -> 0x30;
        case 'S' -> 0x31;
        case 'T' -> 0x32;
        case 'U' -> 0x33;
        case 'V' -> 0x34;
        case 'W' -> 0x35;
        case 'X' -> 0x36;
        case 'Y' -> 0x37;
        case 'Z' -> 0x38;
        case 'a' -> 0x39;
        case 'b' -> 0x3a;
        case 'c' -> 0x3b;
        case 'd' -> 0x3c;
        case 'e' -> 0x3d;
        case 'f' -> 0x3e;
        case 'g' -> 0x3f;
        case 'h' -> 0x40;
        case 'i' -> 0x41;
        case 'j' -> 0x42;
        case 'k' -> 0x43;
        case 'l' -> 0x44;
        case 'm' -> 0x45;
        case 'n' -> 0x46;
        case 'o' -> 0x47;
        case 'p' -> 0x48;
        case 'q' -> 0x49;
        case 'r' -> 0x4a;
        case 's' -> 0x4b;
        case 't' -> 0x4c;
        case 'u' -> 0x4d;
        case 'v' -> 0x4e;
        case 'w' -> 0x4f;
        case 'x' -> 0x50;
        case 'y' -> 0x51;
        case 'z' -> 0x52;
        case '[' -> 0x53;
        case ']' -> 0x54;
        case ';' -> 0x55;
        case '\n' -> 0xa1ff;
        default -> 0x05;
      });
    }

    this.charAt(text.length(), 0xa0ff);
  }

  public LodString slice(final int index, final int length) {
    if(this.ref == null) {
      final LodString str = new LodString(length);

      for(int i = 0; i < length; i++) {
        str.charAt(i, this.charAt(index + i));
      }

      return str;
    }

    return this.ref.offset(2, index * 0x2L).cast(LodString::new);
  }

  public LodString slice(final int index) {
    if(this.ref == null) {
      return this.slice(index, this.chars.length - index);
    }

    return this.ref.offset(2, index * 0x2L).cast(LodString::new);
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
