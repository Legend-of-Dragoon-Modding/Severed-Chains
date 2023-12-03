package legend.core;

public class Random extends java.util.Random {
  /**
   * Returns the 32 high bits of Stafford variant 4 mix64 function as int.
   * http://zimbry.blogspot.com/2011/09/better-bit-mixing-improving-on.html
   */
  private static int mix32(long z) {
    z = (z ^ (z >>> 33)) * 0x62a9d9ed799705f5L;
    return (int)(((z ^ (z >>> 28)) * 0xcb24d0a5c88c35b3L) >>> 32);
  }

  @Override
  public int nextInt() {
    return mix32(super.nextInt());
  }
}
