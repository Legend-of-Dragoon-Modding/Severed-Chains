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
  public static final int PITCH_BIT_SHIFT = 27;
  private Constants() {}
}
