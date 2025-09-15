package legend.core.audio;

public final class Constants {
  public static final int BASE_SAMPLE_RATE = 44_100;
  public static final int ENGINE_SAMPLE_RATE = 48_000;
  public static final double SAMPLE_RATE_RATIO = BASE_SAMPLE_RATE / (double)ENGINE_SAMPLE_RATE;

  /**
   * <ul>
   *   <li>
   *     <b>Bit shift of 26</b> — Supports approximately 5 octaves and 3.5 semitones.
   *     <pre>
   *       (28 << 26) - 1 + (1 << 31) * (44_100 / 48_000) * 2^(14/48) ≈ 0xFFF2_DDC8
   *     </pre>
   *     This is well beyond the two-octave shift limit of the retail engine and even the theoretical
   *     limit of ~4 octaves and 9.5 semitones (you can't shift further than the buffer length).
   *     <p>
   *     Dropping down to 26 might be beneficial if we want to support higher quality sound banks
   *     (e.g., modding). The two-octave shift will still fit with a 96 kHz sound bank.
   *     Higher sample rate sound banks often result in much cleaner pitch modulation because
   *     interpolated values are not only closer to the original sound bank values, but interpolation
   *     also occurs over smaller sections of the sound wave.
   *   </li>
   *
   *   <li>
   *     <b>Bit shift of 27</b> — Supports approximately 2 octaves and 1.25 semitones.
   *     <pre>
   *       (28 << 27) - 1 + (1 << 29) * (44_100 / 48_000) * 2^(5/48) ≈ 0xFF99_ED3C
   *     </pre>
   *     This is the absolute limit of how far it can be pushed without converting the pitch counter to a {@code long}.
   *   </li>
   *
   *   <li>
   *     <b>Bit shift of 31</b> — Can be used if a {@code long} is used for accumulation.
   *     This does not require more memory for lookups since only a single octave is stored,
   *     hence the maximal multiplier remains just below 2.
   *   </li>
   * </ul>
   */
  public static final int PITCH_BIT_SHIFT = 31;
  public static final long PITCH_MAX_VALUE = 28L << PITCH_BIT_SHIFT;

  /** There are 60 values in the original breath wave */
  private static final int BASE_BREATH_COUNT = 60;
  /**
   * 240 seems to be the sweet spot. The interpolation is pretty good at approximating these,
   * but 240 gives a significant accuracy boost over 120, most likely due to the fact that
   * the curvature of the sine wave that's being interpolated over gets significantly reduced
   * by the increase of samples.
   */
  private static final int BREATH_COUNT_SHIFT = 2;
  public static final int BREATH_COUNT = BASE_BREATH_COUNT << BREATH_COUNT_SHIFT;

  /** The maximum speed for traversing the breath wave is x30.
   * <pre>
   *   (60 << 25) - 1 + (30 << 25) = 0xB3FF_FFFF
   * </pre>
   * Any more than 25 and you could overflow the counter.
   * <p>
   * However, something interesting happens when you try to process at least 480 times a second (x8 the retail rate).
   * On every tick, you'd have to add 1/8th of the value.
   * <pre>
   *   (60 << 26) - 1 + (30 << 23) = 0xFEFF_FFFF
   * </pre>
   * So when going from a x4 to x8, instead of halving the amount, we could double the max value.
   */
  private static final int BASE_BREATH_BIT_SHIFT = 47;
  public static final int BREATH_BIT_SHIFT = BASE_BREATH_BIT_SHIFT - BREATH_COUNT_SHIFT;
  public static final long BREATH_MAX_VALUE = (long)BASE_BREATH_COUNT << BASE_BREATH_BIT_SHIFT;

  private Constants() {}
}
