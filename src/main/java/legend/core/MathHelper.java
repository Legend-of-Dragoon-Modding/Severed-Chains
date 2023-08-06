package legend.core;

import org.joml.Vector3f;

public final class MathHelper {
  private MathHelper() { }

  public static short clamp(final short value, final short min, final short max) {
    return (short)Math.max(min, Math.min(value, max));
  }

  public static int clamp(final int value, final int min, final int max) {
    return Math.max(min, Math.min(value, max));
  }

  public static long clamp(final long value, final long min, final long max) {
    return Math.max(min, Math.min(value, max));
  }

  public static float clamp(final float value, final float min, final float max) {
    return Math.max(min, Math.min(value, max));
  }

  public static Vector3f clamp(final Vector3f value, final float min, final float max) {
    value.x = clamp(value.x, min, max);
    value.y = clamp(value.y, min, max);
    value.z = clamp(value.z, min, max);
    return value;
  }

  public static boolean inBox(final int x, final int y, final int left, final int top, final int width, final int height) {
    return x >= left && x < left + width && y >= top && y < top + height;
  }

  public static boolean contains(final int value, final int min, final int max) {
    return value >= min && value < max;
  }

  public static int colour15To24(final int colour) {
    final byte r = (byte)((colour        & 0b1_1111) * 8);
    final byte g = (byte)((colour >>>  5 & 0b1_1111) * 8);
    final byte b = (byte)((colour >>> 10 & 0b1_1111) * 8);
    final byte a = (byte)((colour >>> 15) * 255);

    return (a & 0xff) << 24 | (b & 0xff) << 16 | (g & 0xff) << 8 | r & 0xff;
  }

  public static int colour15To24Bgr(final int colour) {
    final byte r = (byte)((colour        & 0b1_1111) * 8);
    final byte g = (byte)((colour >>>  5 & 0b1_1111) * 8);
    final byte b = (byte)((colour >>> 10 & 0b1_1111) * 8);
    final byte a = (byte)((colour >>> 15) * 255);

    return (a & 0xff) << 24 | (r & 0xff) << 16 | (g & 0xff) << 8 | b & 0xff;
  }

  public static int colour24To15(final int colour) {
    final byte m = (byte)((colour & 0xff000000) >>> 24);
    final byte r = (byte)((colour & 0x00ff0000) >>> 16 + 3);
    final byte g = (byte)((colour & 0x0000ff00) >>> 8 + 3);
    final byte b = (byte)((colour & 0x000000ff) >>> 3);

    return (m & 0xff) << 15 | (b & 0xff) << 10 | (g & 0xff) << 5 | r & 0xff;
  }

  public static int leadingZeroBits(final short num) {
    for(int i = 0; i < 16; i++) {
      if((num & 1 << 15 - i) != 0) {
        return i;
      }
    }

    return 16;
  }

  public static int digitCount(final int number) {
    if(number == 0) {
      return 1;
    }

    return (int)(Math.log10(number) + 1);
  }

  public static int assertPositive(final int val) {
    assert val >= 0 : "Value must be positive";
    return val;
  }

  public static long get(final byte[] data, final int offset, final int size) {
    long value = 0;

    for(int i = 0; i < size; i++) {
      value |= (long)(data[offset + i] & 0xff) << i * 8;
    }

    return value;
  }

  public static void set(final byte[] data, final int offset, final int size, final long value) {
    for(int i = 0; i < size; i++) {
      data[offset + i] = (byte)(value >>> i * 8 & 0xff);
    }
  }

  public static byte getByte(final byte[] data, final int index) {
    return data[index];
  }

  public static int getUbyte(final byte[] data, final int index) {
    return data[index] & 0xff;
  }

  public static short getShort(final byte[] data, final int index) {
    return (short)((data[index + 1] & 0xff) << 8 | data[index] & 0xff);
  }

  public static int getUshort(final byte[] data, final int index) {
    return (data[index + 1] & 0xff) << 8 | data[index] & 0xff;
  }

  public static int getInt(final byte[] data, final int index) {
    return (data[index + 3] & 0xff) << 24 | (data[index + 2] & 0xff) << 16 | (data[index + 1] & 0xff) << 8 | data[index] & 0xff;
  }

  public static long sign(final long value, final int numberOfBytes) {
    if((value & 1L << numberOfBytes * 8 - 1) != 0) {
      return value | -(1L << numberOfBytes * 8);
    }

    return value;
  }

  public static long unsign(final long value, final int numberOfBytes) {
    return value & (1L << numberOfBytes * 8) - 1;
  }

  public static final float PI = (float)Math.PI;
  public static final float TWO_PI = (float)(Math.PI * 2);

  private static final float PSX_DEG_TO_DEG = 360.0f / 4096.0f;
  private static final float DEG_TO_RAD = (float)(Math.PI / 180.0f);
  private static final float PSX_DEG_TO_RAD = PSX_DEG_TO_DEG * DEG_TO_RAD;

  private static final float DEG_TO_PSX_DEG = 4096.0f / 360.0f;
  private static final float RAD_TO_DEG = (float)(180.0f / Math.PI);
  private static final float RAD_TO_PSX_DEG = RAD_TO_DEG * DEG_TO_PSX_DEG;

  public static float psxDegToRad(final int psxDeg) {
    return psxDeg * PSX_DEG_TO_RAD;
  }

  public static float psxDegToRad(final float psxDeg) {
    return psxDeg * PSX_DEG_TO_RAD;
  }

  public static int radToPsxDeg(final float rads) {
    return (int)(rads * RAD_TO_PSX_DEG);
  }

  /** LOD uses this a lot */
  public static float positiveAtan2(final float y, final float x) {
    // Cast to double to use joml's fast atan2
    return floorMod(-(float)org.joml.Math.atan2((double)y, x) + 0.75f * MathHelper.TWO_PI, MathHelper.TWO_PI);
  }

  public static float atan2(final float y, final float x) {
    if(y == 0.0f && x == 0.0f) {
      return 0.0f;
    }

    return (float)org.joml.Math.atan2((double)y, x);
  }

  public static float sin(final float angle) {
    return org.joml.Math.sin(angle);
  }

  public static float cos(final float angle) {
    return org.joml.Math.cos(angle);
  }

  public static float cosFromSin(final float sin, final float angle) {
    return org.joml.Math.cosFromSin(sin, angle);
  }

  public static float floorMod(final float numerator, final float denominator) {
    return (numerator - org.joml.Math.floor(numerator / denominator) * denominator);
  }

  public static void floorMod(final Vector3f numerator, final float denominator) {
    numerator.x = floorMod(numerator.x, denominator);
    numerator.y = floorMod(numerator.y, denominator);
    numerator.z = floorMod(numerator.z, denominator);
  }

  public static int roundUp(final int val, final int step) {
    return val + step - 1 & -step;
  }

  public static long roundUp(final long val, final long step) {
    return val + step - 1 & -step;
  }

  /**
   * Handles /0 like the PS1 - if num is positive, result will be -1; if num is negative, result will be +1; if num is 0, result will be 0
   */
  public static long safeDiv(final long num, final long div) {
    if(div == 0) {
      return Long.compare(0, num);
    }

    return num / div;
  }

  /**
   * Handles /0 like the PS1 - if num is positive, result will be -1; if num is negative, result will be +1; if num is 0, result will be 0
   */
  public static int safeDiv(final int num, final int div) {
    if(div == 0) {
      return Integer.compare(0, num);
    }

    return num / div;
  }

  public static int safeDiv(final int num, final float div) {
    if(div == 0.0f) {
      return Integer.compare(0, num);
    }

    return (int)(num / div);
  }

  public static float safeDiv(final float num, final float div) {
    if(div == 0.0f) {
      return Float.compare(0, num);
    }

    return num / div;
  }

  public static long shrRound(final long val, final int shr) {
    if(shr == 0 || val == 0) {
      return val;
    }
    final long i = (val >> (shr - 1)) & 1;
    return (val >> shr) + i;
  }

  public static boolean flEq(final float a, final float b, final float epsilon) {
    return Math.abs(a - b) < epsilon;
  }
}
