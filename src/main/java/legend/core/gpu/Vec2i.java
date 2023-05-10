package legend.core.gpu;

public record Vec2i(int x, int y) {
  /** Unpacks two 16-bit signed shorts */
  public static Vec2i unpack(final int packed) {
    return new Vec2i((short)packed, packed >> 16);
  }
}
