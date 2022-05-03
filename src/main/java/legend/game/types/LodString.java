package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;

public class LodString implements MemoryRef {
  private final Value ref;

  public LodString(final Value ref) {
    this.ref = ref;
  }

  public String get() {
    final StringBuilder sb = new StringBuilder();

    for(int offset = 0; offset < 0xff; offset += 2) {
      final long c = this.ref.offset(2, offset).get();

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

  public long charAt(final int index) {
    return this.ref.offset(2, index * 0x2L).get();
  }

  public void charAt(final int index, final long c) {
    this.ref.offset(2, index * 0x2L).setu(c);
  }

  public LodString slice(final int index) {
    return this.ref.offset(2, index * 0x2L).cast(LodString::new);
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
